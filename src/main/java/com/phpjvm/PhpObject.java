package com.phpjvm;

import java.util.LinkedHashMap;
import java.util.Map;

public class PhpObject {
    private final PhpClass phpClass;

    // Optional reference target (used for pass-by-reference params)
    private final PhpRef ref;

    // instance fields / dynamic properties
    private final Map<String, BasePhpValue> properties = new LinkedHashMap<>();

    public PhpObject(PhpClass phpClass) {
        this(phpClass, null);
    }

    public PhpObject(PhpClass phpClass, PhpRef ref) {
        if (phpClass == null) {
            throw new BasePhpValue.PhpRuntimeException("Object created with null class");
        }
        this.phpClass = phpClass;
        this.ref = ref;
    }

    public static PhpObject reference(PhpRef ref) {
        // Special internal class name; it will never be reflected into userland.
        return new PhpObject(new PhpClass("__ref"), ref);
    }

    public boolean isReference() {
        return ref != null;
    }

    public BasePhpValue refGet() {
        if (ref == null) return BasePhpValue.NULL_VALUE;
        BasePhpValue v = ref.get();
        return v == null ? BasePhpValue.NULL_VALUE : v;
    }

    public void refSet(BasePhpValue v) {
        if (ref == null) {
            throw new BasePhpValue.PhpRuntimeException("Attempt to write to non-reference object");
        }
        ref.set(v == null ? BasePhpValue.NULL_VALUE : v);
    }

    public PhpClass getPhpClass() {
        return phpClass;
    }

    // --- properties ---
    public BasePhpValue getProperty(String name) {
        if (name == null) name = "";
        BasePhpValue v = properties.get(name);
        return v == null ? BasePhpValue.NULL_VALUE : v;
    }

    public void setProperty(String name, BasePhpValue value) {
        if (name == null) name = "";
        properties.put(name, value == null ? BasePhpValue.NULL_VALUE : value);
    }

    public boolean hasProperty(String name) {
        if (name == null) name = "";
        return properties.containsKey(name);
    }

    public Map<String, BasePhpValue> debugPropertiesView() {
        return properties;
    }
}
