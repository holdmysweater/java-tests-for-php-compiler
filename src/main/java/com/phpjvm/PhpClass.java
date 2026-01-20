package com.phpjvm;

import java.util.LinkedHashMap;
import java.util.Map;

public final class PhpClass {
    public enum Visibility {
        PUBLIC,
        PROTECTED,
        PRIVATE
    }

    public static final class DeclaredProperty {
        public final String name;               // case-sensitive
        public final boolean isStatic;
        public final Visibility visibility;
        public final BasePhpValue defaultValue; // for instance props, also initial for static props

        public DeclaredProperty(String name, boolean isStatic, Visibility visibility, BasePhpValue defaultValue) {
            this.name = (name == null) ? "" : name;
            this.isStatic = isStatic;
            this.visibility = (visibility == null) ? Visibility.PUBLIC : visibility;
            this.defaultValue = (defaultValue == null) ? BasePhpValue.NULL_VALUE : defaultValue;
        }
    }

    public static final class DeclaredConst {
        public final String name;          // case-sensitive
        public final Visibility visibility;
        public final BasePhpValue value;

        public DeclaredConst(String name, Visibility visibility, BasePhpValue value) {
            this.name = (name == null) ? "" : name;
            this.visibility = (visibility == null) ? Visibility.PUBLIC : visibility;
            this.value = (value == null) ? BasePhpValue.NULL_VALUE : value;
        }
    }

    private final String name;
    private final PhpClass parent;

    // dynamic dispatch tables
    private final Map<String, PhpMethod> methods = new LinkedHashMap<>();
    private final Map<String, PhpStaticMethod> staticMethods = new LinkedHashMap<>();

    // declared members (case-sensitive keys, exactly as written in source)
    private final Map<String, DeclaredProperty> declaredProps = new LinkedHashMap<>();
    private final Map<String, BasePhpValue> staticPropValues = new LinkedHashMap<>();
    private final Map<String, DeclaredConst> declaredConsts = new LinkedHashMap<>();

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

    // --- declared properties / constants ---

    public void declareProperty(String propName, boolean isStatic, Visibility vis, BasePhpValue defaultValue) {
        String n = (propName == null) ? "" : propName;
        DeclaredProperty p = new DeclaredProperty(n, isStatic, vis, defaultValue);
        declaredProps.put(n, p);

        // For static props, store the class-wide storage in the DECLARING class.
        if (isStatic) {
            // do not overwrite if already initialized (allows later code to mutate before redeclare guard)
            staticPropValues.putIfAbsent(n, p.defaultValue);
        }
    }

    public void defineConst(String constName, Visibility vis, BasePhpValue value) {
        String n = (constName == null) ? "" : constName;
        DeclaredConst c = new DeclaredConst(n, vis, value);
        declaredConsts.put(n, c);
    }

    public DeclaredProperty getDeclaredPropertyHere(String propName) {
        String n = (propName == null) ? "" : propName;
        return declaredProps.get(n);
    }

    public DeclaredConst getDeclaredConstHere(String constName) {
        String n = (constName == null) ? "" : constName;
        return declaredConsts.get(n);
    }

    public BasePhpValue getStaticPropValueHere(String propName) {
        String n = (propName == null) ? "" : propName;
        BasePhpValue v = staticPropValues.get(n);
        return (v == null) ? BasePhpValue.NULL_VALUE : v;
    }

    public void setStaticPropValueHere(String propName, BasePhpValue value) {
        String n = (propName == null) ? "" : propName;
        staticPropValues.put(n, (value == null) ? BasePhpValue.NULL_VALUE : value);
    }

    // object creation
    public PhpObject newInstance() {
        PhpObject o = new PhpObject(this);
        initInstancePropsRecursive(o);
        return o;
    }

    private void initInstancePropsRecursive(PhpObject o) {
        if (parent != null) parent.initInstancePropsRecursive(o);

        // parent first, then child overrides if same name redeclared
        for (DeclaredProperty p : declaredProps.values()) {
            if (!p.isStatic) {
                o.setProperty(p.name, p.defaultValue);
            }
        }
    }
}
