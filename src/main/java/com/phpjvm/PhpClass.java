package com.phpjvm;

import java.util.LinkedHashMap;
import java.util.Map;

public final class PhpClass {
    private final String name;
    private final PhpClass parent;

    // dynamic dispatch tables
    private final Map<String, PhpMethod> methods = new LinkedHashMap<>();
    private final Map<String, PhpStaticMethod> staticMethods = new LinkedHashMap<>();

    public PhpClass(String name) {
        this(name, null);
    }

    public PhpClass(String name, PhpClass parent) {
        this.name = (name == null ? "" : name);
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public PhpClass getParent() {
        return parent;
    }

    // --- instance methods ---
    public void addMethod(String methodName, PhpMethod method) {
        if (methodName == null) methodName = "";
        if (method == null) throw new BasePhpValue.PhpRuntimeException("Null method body: " + name + "::" + methodName);
        methods.put(methodName.toLowerCase(), method);
    }

    public PhpMethod findMethod(String methodName) {
        if (methodName == null) methodName = "";
        String key = methodName.toLowerCase();

        PhpMethod m = methods.get(key);
        if (m != null) return m;

        // inheritance lookup
        if (parent != null) return parent.findMethod(methodName);
        return null;
    }

    // --- static methods ---
    public void addStaticMethod(String methodName, PhpStaticMethod method) {
        if (methodName == null) methodName = "";
        if (method == null) throw new BasePhpValue.PhpRuntimeException("Null static method body: " + name + "::" + methodName);
        staticMethods.put(methodName.toLowerCase(), method);
    }

    public PhpStaticMethod findStaticMethod(String methodName) {
        if (methodName == null) methodName = "";
        String key = methodName.toLowerCase();

        PhpStaticMethod m = staticMethods.get(key);
        if (m != null) return m;

        // inheritance lookup
        if (parent != null) return parent.findStaticMethod(methodName);
        return null;
    }

    // object creation
    public PhpObject newInstance() {
        return new PhpObject(this);
    }
}
