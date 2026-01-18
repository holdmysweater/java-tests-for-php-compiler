package com.phpjvm;

import java.util.LinkedHashMap;
import java.util.Map;

public final class PhpRuntime {
    private PhpRuntime() {
    }

    public static BasePhpValue callMethod(PhpObject obj, String methodName, BasePhpValue[] args) {
        if (obj == null) throw new BasePhpValue.PhpRuntimeException("Call to a member function on null");
        if (methodName == null) methodName = "";
        if (args == null) args = new BasePhpValue[0];

        PhpMethod m = obj.getPhpClass().findMethod(methodName);
        if (m == null) {
            throw new BasePhpValue.PhpRuntimeException(
                    "Call to undefined method " + obj.getPhpClass().getName() + "::" + methodName + "()"
            );
        }

        BasePhpValue r = m.invoke(obj, args);
        return (r == null) ? BasePhpValue.NULL_VALUE : r;
    }

    public static BasePhpValue callStatic(PhpClass calledClass, String methodName, BasePhpValue[] args) {
        if (calledClass == null) throw new BasePhpValue.PhpRuntimeException("Static call on null class");
        if (methodName == null) methodName = "";
        if (args == null) args = new BasePhpValue[0];

        PhpStaticMethod m = calledClass.findStaticMethod(methodName);
        if (m == null) {
            throw new BasePhpValue.PhpRuntimeException(
                    "Call to undefined method " + calledClass.getName() + "::" + methodName + "()"
            );
        }

        BasePhpValue r = m.invoke(calledClass, args);
        return (r == null) ? BasePhpValue.NULL_VALUE : r;
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

    public static BasePhpValue newObject(String className, BasePhpValue[] args) {
        PhpClass c = requireClass(className);
        PhpObject obj = c.newInstance();

        // PHP: call __construct if present
        PhpMethod ctor = c.findMethod("__construct");
        if (ctor != null) {
            callMethod(obj, "__construct", (args == null ? new BasePhpValue[0] : args));
        }

        return BasePhpValue.object(obj);
    }
}
