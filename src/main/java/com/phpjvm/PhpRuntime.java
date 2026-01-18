package com.phpjvm;

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
}
