package com.phpjvm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

public final class PhpRuntime {
    private PhpRuntime() {
    }

    private static String norm(String s) {
        if (s == null) return "";
        return s.toLowerCase();
    }

    private static Class<?> jvmClassFromPhpName(String phpClassName) {
        // If you generate JVM classes in default package: "a"
        // If you generate with slashes: replace '/' with '.'
        String n = norm(phpClassName).replace('/', '.');
        try {
            return Class.forName(n);
        } catch (ClassNotFoundException e) {
            throw new BasePhpValue.PhpRuntimeException("Class not found: " + phpClassName);
        }
    }

    public static BasePhpValue newObject(String className, BasePhpValue[] args) {
        if (args == null) args = new BasePhpValue[0];

        // IMPORTANT: PHP class names are case-insensitive, your AST uses "A" but decl is "a"
        String phpName = norm(className);

        // Create runtime PhpClass + PhpObject (properties live here)
        PhpClass cls = new PhpClass(phpName);
        PhpObject obj = cls.newInstance();

        // Call __construct if present (ignore if missing)
        try {
            callMethod(obj, "__construct", args);
        } catch (BasePhpValue.PhpRuntimeException ex) {
            // only ignore "undefined method" for constructor
            String msg = ex.getMessage();
            if (msg == null || !msg.toLowerCase().contains("undefined method")) {
                throw ex;
            }
        }

        return BasePhpValue.object(obj);
    }

    public static BasePhpValue callMethod(PhpObject obj, String methodName, BasePhpValue[] args) {
        if (obj == null) throw new BasePhpValue.PhpRuntimeException("Call to a member function on null");
        if (methodName == null) methodName = "";
        if (args == null) args = new BasePhpValue[0];

        // 1) Try dynamic table (if you later populate it)
        PhpMethod m = obj.getPhpClass().findMethod(methodName);
        if (m != null) {
            BasePhpValue r = m.invoke(obj, args);
            return (r == null) ? BasePhpValue.NULL_VALUE : r;
        }

        // 2) Reflection fallback: call static Java method on generated JVM class
        String mn = norm(methodName);
        Class<?> host = jvmClassFromPhpName(obj.getPhpClass().getName());

        try {
            Method javaM = host.getDeclaredMethod(mn, PhpObject.class, BasePhpValue[].class);
            Object out = javaM.invoke(null, obj, args);
            return (out instanceof BasePhpValue pv) ? pv : BasePhpValue.NULL_VALUE;
        } catch (NoSuchMethodException e) {
            throw new BasePhpValue.PhpRuntimeException(
                    "Call to undefined method " + obj.getPhpClass().getName() + "::" + methodName + "()"
            );
        } catch (Exception e) {
            throw new BasePhpValue.PhpRuntimeException(
                    "Exception in method " + obj.getPhpClass().getName() + "::" + methodName + "(): " + e.getMessage()
            );
        }
    }

    public static BasePhpValue callStatic(PhpClass calledClass, String methodName, BasePhpValue[] args) {
        if (calledClass == null) throw new BasePhpValue.PhpRuntimeException("Static call on null class");
        if (methodName == null) methodName = "";
        if (args == null) args = new BasePhpValue[0];

        // 1) Try dynamic table first
        PhpStaticMethod m = calledClass.findStaticMethod(methodName);
        if (m != null) {
            BasePhpValue r = m.invoke(calledClass, args);
            return (r == null) ? BasePhpValue.NULL_VALUE : r;
        }

        // 2) Reflection fallback
        String mn = norm(methodName);
        Class<?> host = jvmClassFromPhpName(calledClass.getName());

        try {
            Method javaM = host.getDeclaredMethod(mn, PhpClass.class, BasePhpValue[].class);
            Object out = javaM.invoke(null, calledClass, args);
            return (out instanceof BasePhpValue pv) ? pv : BasePhpValue.NULL_VALUE;
        } catch (NoSuchMethodException e) {
            throw new BasePhpValue.PhpRuntimeException(
                    "Call to undefined method " + calledClass.getName() + "::" + methodName + "()"
            );
        } catch (Exception e) {
            throw new BasePhpValue.PhpRuntimeException(
                    "Exception in static method " + calledClass.getName() + "::" + methodName + "(): " + e.getMessage()
            );
        }
    }

    private static final Map<String, PhpClass> CLASSES = new LinkedHashMap<>();

    public static void registerClass(PhpClass c) {
        if (c == null) throw new BasePhpValue.PhpRuntimeException("Register null class");
        String key = c.getName().toLowerCase();
        CLASSES.put(key, c);
    }

    public static PhpClass requireClass(String name) {
        if (name == null) name = "";
        PhpClass c = CLASSES.get(name.toLowerCase());
        if (c == null) {
            throw new BasePhpValue.PhpRuntimeException("Class not found: " + name);
        }
        return c;
    }

    private static final BufferedReader STDIN_READER =
            new BufferedReader(new InputStreamReader(System.in));

    public static BasePhpValue callFunction(String name, BasePhpValue[] args) {
        if (name == null) name = "";
        if (args == null) args = new BasePhpValue[0];

        String n = norm(name);

        return switch (n) {
            case "fgets" -> builtinFgets();
            case "fgetc" -> builtinFgetc();
            default -> throw new BasePhpValue.PhpRuntimeException(
                    "Call to undefined function " + name + "()"
            );
        };
    }

    private static BasePhpValue builtinFgets() {
        try {
            String line = STDIN_READER.readLine();
            if (line == null) return BasePhpValue.of(false); // EOF -> false (PHP-like)
            // PHP fgets() keeps the trailing newline when it exists
            return BasePhpValue.of(line + "\n");
        } catch (IOException e) {
            throw new BasePhpValue.PhpRuntimeException("stdin read failed: " + e.getMessage());
        }
    }

    private static BasePhpValue builtinFgetc() {
        try {
            int ch = STDIN_READER.read();
            if (ch < 0) return BasePhpValue.of(false); // EOF -> false
            return BasePhpValue.of(String.valueOf((char) ch));
        } catch (IOException e) {
            throw new BasePhpValue.PhpRuntimeException("stdin read failed: " + e.getMessage());
        }
    }

}
