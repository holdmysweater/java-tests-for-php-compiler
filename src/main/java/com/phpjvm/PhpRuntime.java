package com.phpjvm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

public final class PhpRuntime {
    private PhpRuntime() {
    }

    // Use a prefix very unlikely to appear in user strings.
    // We keep it printable for debugging.
    public static final String REF_PREFIX = "\u0000PHPREF:";

    private static String norm(String s) {
        if (s == null) return "";
        return s.toLowerCase();
    }

    private static Class<?> jvmClassFromPhpName(String phpClassName) {
        String n = norm(phpClassName).replace('/', '.');
        try {
            return Class.forName(n);
        } catch (ClassNotFoundException e) {
            throw new BasePhpValue.PhpRuntimeException("Class not found: " + phpClassName);
        }
    }

    // -------------------------------------------------------------------------
    // REF MARKERS (copy-in/copy-out by-ref for globals)
    // Marker format: REF_PREFIX + <hostClassDotName> + "#" + <staticFieldName>
    // -------------------------------------------------------------------------

    public static BasePhpValue makeGlobalRef(String hostClassDotName, String staticFieldName) {
        if (hostClassDotName == null) hostClassDotName = "";
        if (staticFieldName == null) staticFieldName = "";
        return BasePhpValue.of(REF_PREFIX + hostClassDotName + "#" + staticFieldName);
    }

    public static String requireGlobalRef(BasePhpValue v, String fnName, String paramName) {
        String s = rawStringIfString(v);
        if (s == null || !s.startsWith(REF_PREFIX)) {
            String got = typeOf(v);
            if (fnName == null) fnName = "";
            if (paramName == null) paramName = "";
            throw new BasePhpValue.PhpRuntimeException(
                    "Argument $" + paramName + " passed to " + fnName + "() must be a variable (by reference), " + got + " given"
            );
        }
        return s.substring(REF_PREFIX.length());
    }

    public static BasePhpValue getGlobalRefValue(String refDesc) {
        if (refDesc == null) refDesc = "";
        int idx = refDesc.indexOf('#');
        if (idx < 0) {
            throw new BasePhpValue.PhpRuntimeException("Invalid reference descriptor: " + refDesc);
        }
        String hostName = refDesc.substring(0, idx);
        String fieldName = refDesc.substring(idx + 1);

        try {
            Class<?> host = Class.forName(hostName);
            Field f = host.getField(fieldName);
            Object o = f.get(null);
            if (o instanceof BasePhpValue pv) {
                return pv == null ? BasePhpValue.NULL_VALUE : pv;
            }
            return BasePhpValue.NULL_VALUE;
        } catch (Exception e) {
            throw new BasePhpValue.PhpRuntimeException(
                    "Failed to read reference " + refDesc + ": " + e.getMessage()
            );
        }
    }

    public static void setGlobalRefValue(String refDesc, BasePhpValue value) {
        if (refDesc == null) refDesc = "";
        int idx = refDesc.indexOf('#');
        if (idx < 0) {
            throw new BasePhpValue.PhpRuntimeException("Invalid reference descriptor: " + refDesc);
        }
        String hostName = refDesc.substring(0, idx);
        String fieldName = refDesc.substring(idx + 1);

        try {
            Class<?> host = Class.forName(hostName);
            Field f = host.getField(fieldName);
            f.set(null, value == null ? BasePhpValue.NULL_VALUE : value);
        } catch (Exception e) {
            throw new BasePhpValue.PhpRuntimeException(
                    "Failed to write reference " + refDesc + ": " + e.getMessage()
            );
        }
    }

    // -------------------------------------------------------------------------
    // TYPE INTROSPECTION (best-effort via reflection, no BasePhpValue edits needed)
    // -------------------------------------------------------------------------

    private static Object tryInvoke0(Object obj, String methodName) {
        try {
            Method m = obj.getClass().getMethod(methodName);
            return m.invoke(obj);
        } catch (Exception ignored) {
            return null;
        }
    }

    private static Object tryGetField(Object obj, String fieldName) {
        try {
            Field f = obj.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            return f.get(obj);
        } catch (Exception ignored) {
            return null;
        }
    }

    private static String rawStringIfString(BasePhpValue v) {
        if (v == null) return null;

        // If BasePhpValue has something like asString()/rawString()/getString(), try it.
        Object s =
                tryInvoke0(v, "asString");
        if (s instanceof String) return (String) s;

        s = tryInvoke0(v, "rawString");
        if (s instanceof String) return (String) s;

        s = tryInvoke0(v, "getString");
        if (s instanceof String) return (String) s;

        // Fallback: only treat toPhpString() as "raw string" if the value is actually a string.
        String t = typeOf(v);
        if ("string".equals(t)) {
            Object ps = tryInvoke0(v, "toPhpString");
            if (ps instanceof String) return (String) ps;
        }
        return null;
    }

    public static String typeOf(BasePhpValue v) {
        if (v == null) return "null";

        // common: v.isNull()
        Object isNull = tryInvoke0(v, "isNull");
        if (isNull instanceof Boolean b && b) return "null";

        // Try a "type" field (enum) or "getType()"
        Object t = tryInvoke0(v, "getType");
        if (t == null) t = tryGetField(v, "type");
        if (t != null) {
            String tn = String.valueOf(t).toLowerCase();
            // normalize a few likely spellings
            if (tn.contains("int") || tn.contains("long")) return "int";
            if (tn.contains("double") || tn.contains("float")) return "float";
            if (tn.contains("bool")) return "bool";
            if (tn.contains("string")) return "string";
            if (tn.contains("array")) return "array";
            if (tn.contains("object")) return "object";
            if (tn.contains("null")) return "null";
        }

        // Try some "isX" helpers if present
        Object s = tryInvoke0(v, "isString");
        if (s instanceof Boolean b && b) return "string";
        Object i = tryInvoke0(v, "isInt");
        if (i instanceof Boolean b && b) return "int";
        Object bl = tryInvoke0(v, "isBool");
        if (bl instanceof Boolean b && b) return "bool";
        Object ar = tryInvoke0(v, "isArray");
        if (ar instanceof Boolean b && b) return "array";
        Object ob = tryInvoke0(v, "isObject");
        if (ob instanceof Boolean b && b) return "object";

        // Fallback: class name
        return v.getClass().getSimpleName().toLowerCase();
    }

    private static boolean acceptsType(String actual, String allowed) {
        if (allowed == null) return false;
        allowed = allowed.toLowerCase();

        if ("mixed".equals(allowed)) return true;

        // union support
        if (allowed.contains("|")) {
            String[] parts = allowed.split("\\|");
            for (String p : parts) {
                if (acceptsType(actual, p.trim())) return true;
            }
            return false;
        }

        if ("integer".equals(allowed)) allowed = "int";
        if ("boolean".equals(allowed)) allowed = "bool";
        if ("double".equals(allowed)) allowed = "float";

        if ("void".equals(allowed)) return "null".equals(actual);
        if ("null".equals(allowed)) return "null".equals(actual);

        return allowed.equals(actual);
    }

    private static String joinTypes(String[] allowed) {
        if (allowed == null || allowed.length == 0) return "mixed";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < allowed.length; i++) {
            if (i > 0) sb.append("|");
            sb.append(allowed[i]);
        }
        return sb.toString();
    }

    public static BasePhpValue assertParamType(BasePhpValue v, String[] allowed, String fnName, String paramName) {
        if (allowed == null || allowed.length == 0) return v == null ? BasePhpValue.NULL_VALUE : v;

        String actual = typeOf(v);
        for (String a : allowed) {
            if (acceptsType(actual, a)) {
                return v == null ? BasePhpValue.NULL_VALUE : v;
            }
        }

        String want = joinTypes(allowed);
        if (fnName == null) fnName = "";
        if (paramName == null) paramName = "";
        throw new BasePhpValue.PhpRuntimeException(
                "Argument $" + paramName + " passed to " + fnName + "() must be of type " + want + ", " + actual + " given"
        );
    }

    public static BasePhpValue assertReturnType(BasePhpValue v, String[] allowed, String fnName) {
        BasePhpValue vv = (v == null) ? BasePhpValue.NULL_VALUE : v;

        if (allowed == null || allowed.length == 0) return vv;

        boolean wantVoid = false;
        for (String a : allowed) {
            if (a != null && a.equalsIgnoreCase("void")) {
                wantVoid = true;
                break;
            }
        }

        String actual = typeOf(vv);

        if (wantVoid) {
            // Accept only null-ish (fall-through / `return;`)
            if (vv == BasePhpValue.NULL_VALUE || "null".equals(actual)) {
                return BasePhpValue.NULL_VALUE;
            }
            String name = (fnName == null) ? "" : fnName;
            throw new BasePhpValue.PhpRuntimeException(
                    "Return value of " + name + "() must be of type void, " + actual + " returned"
            );
        }

        for (String a : allowed) {
            if (acceptsType(actual, a)) return vv;
        }

        String want = joinTypes(allowed);
        if (fnName == null) fnName = "";
        throw new BasePhpValue.PhpRuntimeException(
                "Return value of " + fnName + "() must be of type " + want + ", " + actual + " returned"
        );
    }

    // -------------------------------------------------------------------------
    // Original runtime (unchanged behavior where possible)
    // -------------------------------------------------------------------------

    public static BasePhpValue newObject(String className, BasePhpValue[] args) {
        if (args == null) args = new BasePhpValue[0];

        String phpName = norm(className);

        // Force JVM load so <clinit> runs and calls defineClass(...)
        jvmClassFromPhpName(phpName);

        PhpClass cls = requireClass(phpName);
        PhpObject obj = cls.newInstance();

        try {
            callMethod(obj, "__construct", args);
        } catch (BasePhpValue.PhpRuntimeException ex) {
            String msg = ex.getMessage();
            if (msg == null || !msg.toLowerCase().contains("undefined method")) throw ex;
        }

        return BasePhpValue.object(obj);
    }

    public static BasePhpValue callMethod(PhpObject obj, String methodName, BasePhpValue[] args) {
        if (obj == null) throw new BasePhpValue.PhpRuntimeException("Call to a member function on null");
        if (methodName == null) methodName = "";
        if (args == null) args = new BasePhpValue[0];

        PhpMethod m = obj.getPhpClass().findMethod(methodName);
        if (m != null) {
            BasePhpValue r = m.invoke(obj, args);
            return (r == null) ? BasePhpValue.NULL_VALUE : r;
        }

        String mn = norm(methodName);
        Class<?> host = jvmClassFromPhpName(obj.getPhpClass().getName());

        try {
            Method javaM = findDeclaredMethodInHierarchy(host, mn, PhpObject.class, BasePhpValue[].class);
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

        PhpStaticMethod m = calledClass.findStaticMethod(methodName);
        if (m != null) {
            BasePhpValue r = m.invoke(calledClass, args);
            return (r == null) ? BasePhpValue.NULL_VALUE : r;
        }

        String mn = norm(methodName);
        Class<?> host = jvmClassFromPhpName(calledClass.getName());

        try {
            Method javaM = findDeclaredMethodInHierarchy(host, mn, PhpClass.class, BasePhpValue[].class);
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
            if (line == null) return BasePhpValue.of(false);
            return BasePhpValue.of(line + "\n");
        } catch (IOException e) {
            throw new BasePhpValue.PhpRuntimeException("stdin read failed: " + e.getMessage());
        }
    }

    private static BasePhpValue builtinFgetc() {
        try {
            int ch = STDIN_READER.read();
            if (ch < 0) return BasePhpValue.of(false);
            return BasePhpValue.of(String.valueOf((char) ch));
        } catch (IOException e) {
            throw new BasePhpValue.PhpRuntimeException("stdin read failed: " + e.getMessage());
        }
    }

    // Create/register a class with an optional parent.
    // Called from generated bytecode in <clinit>.
    public static PhpClass defineClass(String name, String parentName) {
        String n = norm(name);
        if (n.isEmpty()) throw new BasePhpValue.PhpRuntimeException("Invalid class name");

        PhpClass existing = CLASSES.get(n);
        if (existing != null) return existing;

        PhpClass parent = null;
        String pn = norm(parentName);
        if (!pn.isEmpty()) {
            // Force-load parent JVM class so its <clinit> can register it
            jvmClassFromPhpName(pn);

            parent = CLASSES.get(pn);
            if (parent == null) {
                throw new BasePhpValue.PhpRuntimeException("Class not found: " + parentName);
            }
        }

        PhpClass c = new PhpClass(n, parent);
        registerClass(c);
        return c;
    }

    private static Method findDeclaredMethodInHierarchy(Class<?> start, String name, Class<?>... params)
            throws NoSuchMethodException {
        Class<?> c = start;
        while (c != null) {
            try {
                Method m = c.getDeclaredMethod(name, params);
                m.setAccessible(true);
                return m;
            } catch (NoSuchMethodException ignored) {
                c = c.getSuperclass();
            }
        }
        throw new NoSuchMethodException(name);
    }
}
