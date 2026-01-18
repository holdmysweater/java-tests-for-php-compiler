package com.phpjvm;

import java.util.LinkedHashMap;
import java.util.Map;

public class PhpObject {
    private final PhpClass phpClass;

    // instance fields / dynamic properties
    private final Map<String, BasePhpValue> properties = new LinkedHashMap<>();

    public PhpObject(PhpClass phpClass) {
        if (phpClass == null) {
            throw new BasePhpValue.PhpRuntimeException("Object created with null class");
        }
        this.phpClass = phpClass;
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
