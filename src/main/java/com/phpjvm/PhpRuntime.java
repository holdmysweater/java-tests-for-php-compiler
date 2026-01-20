package com.phpjvm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

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

        // Force JVM load only if it's not already a registered (built-in) PhpClass
        if (!CLASSES.containsKey(phpName)) {
            jvmClassFromPhpName(phpName);
        }

        PhpClass cls = requireClass(phpName);
        PhpObject obj = cls.newInstance();
        LIVE_OBJECTS.add(obj);

        try {
            callMethod(obj, "__construct", args);
        } catch (BasePhpValue.PhpRuntimeException ex) {
            String msg = ex.getMessage();
            if (msg == null || !msg.toLowerCase().contains("undefined method")) throw ex;
        }

        return BasePhpValue.object(obj);
    }

    public static BasePhpValue callMethod(PhpObject obj, String methodName, BasePhpValue[] args) {
        return callMethodCtx(obj, methodName, args, "");
    }

    public static BasePhpValue callMethodCtx(PhpObject obj, String methodName, BasePhpValue[] args, String callerClassName) {
        if (obj == null) throw new BasePhpValue.PhpRuntimeException("Call to a member function on null");
        if (methodName == null) methodName = "";
        if (args == null) args = new BasePhpValue[0];

        // If later you register PhpMethod objects, enforce visibility there too.
        PhpMethod m = obj.getPhpClass().findMethod(methodName);
        if (m != null) {
            BasePhpValue r = m.invoke(obj, args);
            return (r == null) ? BasePhpValue.NULL_VALUE : r;
        }

        String mn = norm(methodName);
        Class<?> host = tryJvmClassFromPhpName(obj.getPhpClass().getName());
        if (host == null) {
            throw new BasePhpValue.PhpRuntimeException(
                    "Call to undefined method " + obj.getPhpClass().getName() + "::" + methodName + "()"
            );
        }

        try {
            Method javaM = findDeclaredMethodInHierarchy(host, mn, PhpObject.class, BasePhpValue[].class);

            // NEW: PHP visibility check BEFORE reflective invoke
            assertPhpMethodVisibility(javaM, callerClassName, methodName);

            javaM.setAccessible(true);
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
        return callStaticCtx(calledClass, methodName, args, "");
    }

    public static BasePhpValue callStaticCtx(PhpClass calledClass, String methodName, BasePhpValue[] args, String callerClassName) {
        if (calledClass == null) throw new BasePhpValue.PhpRuntimeException("Static call on null class");
        if (methodName == null) methodName = "";
        if (args == null) args = new BasePhpValue[0];

        PhpStaticMethod m = calledClass.findStaticMethod(methodName);
        if (m != null) {
            BasePhpValue r = m.invoke(calledClass, args);
            return (r == null) ? BasePhpValue.NULL_VALUE : r;
        }

        String mn = norm(methodName);
        Class<?> host = tryJvmClassFromPhpName(calledClass.getName());
        if (host == null) {
            throw new BasePhpValue.PhpRuntimeException(
                    "Call to undefined method " + calledClass.getName() + "::" + methodName + "()"
            );
        }

        try {
            Method javaM = findDeclaredMethodInHierarchy(host, mn, PhpClass.class, BasePhpValue[].class);

            // NEW: PHP visibility check for static methods too
            assertPhpMethodVisibility(javaM, callerClassName, methodName);

            javaM.setAccessible(true);
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

    static {
        registerBuiltinExceptions();
    }

    private static void registerBuiltinExceptions() {
        // Only once
        if (CLASSES.containsKey("exception")) return;

        // Throwable -> Exception -> (RuntimeException, LogicException)
        PhpClass throwable = new PhpClass("throwable", null);
        PhpClass exception = new PhpClass("exception", throwable);
        PhpClass runtimeEx = new PhpClass("runtimeexception", exception);
        PhpClass logicEx = new PhpClass("logicexception", exception);

        registerClass(throwable);
        registerClass(exception);
        registerClass(runtimeEx);
        registerClass(logicEx);

        // Minimal API: __construct($message = ""), getMessage()
        PhpMethod ctor = (self, args) -> {
            BasePhpValue msg = (args != null && args.length > 0 && args[0] != null) ? args[0] : BasePhpValue.of("");
            self.setProperty("message", msg);
            return BasePhpValue.NULL_VALUE;
        };
        PhpMethod getMessage = (self, args) -> self.getProperty("message");
        PhpMethod destruct = (self, args) -> BasePhpValue.NULL_VALUE;

        for (PhpClass c : List.of(throwable, exception, runtimeEx, logicEx)) {
            c.addMethod("__construct", ctor);
            c.addMethod("getMessage", getMessage);
            c.addMethod("__destruct", destruct);
        }
    }


    public static void registerClass(PhpClass c) {
        if (c == null) throw new BasePhpValue.PhpRuntimeException("Register null class");
        String key = c.getName().toLowerCase();
        CLASSES.put(key, c);
    }

    public static PhpClass requireClass(String name) {
        if (name == null) name = "";
        String key = name.toLowerCase();

        PhpClass c = CLASSES.get(key);
        if (c == null) {
            // Force JVM load so <clinit> can register the class
            try {
                jvmClassFromPhpName(key);
            } catch (Throwable ignored) {
            }
            c = CLASSES.get(key);
        }

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
            if (!CLASSES.containsKey(pn)) {
                try {
                    jvmClassFromPhpName(pn);
                } catch (Throwable ignored) {
                }
            }
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
                return c.getDeclaredMethod(name, params);
            } catch (NoSuchMethodException ignored) {
                c = c.getSuperclass();
            }
        }
        throw new NoSuchMethodException(name);
    }

    public static BasePhpValue callParent(PhpObject obj, String currentClassName, String methodName, BasePhpValue[] args) {
        if (obj == null) throw new BasePhpValue.PhpRuntimeException("Call to a member function on null");
        if (currentClassName == null) currentClassName = "";
        if (methodName == null) methodName = "";
        if (args == null) args = new BasePhpValue[0];

        PhpClass cur = requireClass(currentClassName);
        PhpClass parent = cur.getParent();
        if (parent == null) {
            throw new BasePhpValue.PhpRuntimeException("Cannot access parent:: when current class has no parent");
        }

        PhpMethod m = parent.findMethod(methodName);
        if (m != null) {
            BasePhpValue r = m.invoke(obj, args);
            return (r == null) ? BasePhpValue.NULL_VALUE : r;
        }

        String mn = norm(methodName);
        Class<?> host = jvmClassFromPhpName(parent.getName());

        try {
            Method javaM = findDeclaredMethodInHierarchy(host, mn, PhpObject.class, BasePhpValue[].class);
            assertPhpMethodVisibility(javaM, currentClassName, methodName);
            javaM.setAccessible(true);
            Object out = javaM.invoke(null, obj, args);
            return (out instanceof BasePhpValue pv) ? pv : BasePhpValue.NULL_VALUE;
        } catch (NoSuchMethodException e) {
            throw new BasePhpValue.PhpRuntimeException(
                    "Call to undefined method " + parent.getName() + "::" + methodName + "()"
            );
        } catch (Exception e) {
            throw new BasePhpValue.PhpRuntimeException(
                    "Exception in method " + parent.getName() + "::" + methodName + "(): " + e.getMessage()
            );
        }
    }

    public static BasePhpValue callParentStatic(String currentClassName, String methodName, BasePhpValue[] args) {
        if (currentClassName == null) currentClassName = "";
        if (methodName == null) methodName = "";
        if (args == null) args = new BasePhpValue[0];

        PhpClass cur = requireClass(currentClassName);
        PhpClass parent = cur.getParent();
        if (parent == null) {
            throw new BasePhpValue.PhpRuntimeException("Cannot access parent:: when current class has no parent");
        }

        return callStaticCtx(parent, methodName, args, currentClassName);
    }

    private static String normClassCtx(String callerClassName) {
        if (callerClassName == null) return "";
        String s = callerClassName.trim();
        if (s.isEmpty()) return "";
        return norm(s);
    }

    private static void denyPrivate(String decl, String method, String callerCtx) {
        throw new BasePhpValue.PhpRuntimeException(
                "Call to private method " + decl + "::" + method + "() from context '" + callerCtx + "'"
        );
    }

    private static void denyProtected(String decl, String method, String callerCtx) {
        throw new BasePhpValue.PhpRuntimeException(
                "Call to protected method " + decl + "::" + method + "() from context '" + callerCtx + "'"
        );
    }

    private static void assertPhpMethodVisibility(Method target, String callerClassName, String invokedName) {
        if (target == null) return;

        int mod = target.getModifiers();
        if (Modifier.isPublic(mod)) return;

        // For now, treat package-private as public (PHP has no package visibility)
        boolean isPriv = Modifier.isPrivate(mod);
        boolean isProt = Modifier.isProtected(mod);

        String callerCtx = normClassCtx(callerClassName);
        String decl = norm(target.getDeclaringClass().getName());

        if (isPriv) {
            if (callerCtx.isEmpty()) {
                denyPrivate(decl, invokedName, "");
            }
            Class<?> caller;
            try {
                caller = jvmClassFromPhpName(callerCtx);
            } catch (BasePhpValue.PhpRuntimeException ex) {
                denyPrivate(decl, invokedName, callerCtx);
                return;
            }
            if (caller != target.getDeclaringClass()) {
                denyPrivate(decl, invokedName, callerCtx);
            }
            return;
        }

        if (isProt) {
            if (callerCtx.isEmpty()) {
                denyProtected(decl, invokedName, "");
            }
            Class<?> caller;
            try {
                caller = jvmClassFromPhpName(callerCtx);
            } catch (BasePhpValue.PhpRuntimeException ex) {
                denyProtected(decl, invokedName, callerCtx);
                return;
            }
            // Allow if caller is declaring class or subclass
            if (!target.getDeclaringClass().isAssignableFrom(caller)) {
                denyProtected(decl, invokedName, callerCtx);
            }
        }
    }

    // -------------------------------------------------------------------------
    // Declared properties + constants (with PHP-ish visibility checks)
    // -------------------------------------------------------------------------

    private static PhpClass.Visibility visFromInt(int v) {
        return switch (v) {
            case 2 -> PhpClass.Visibility.PROTECTED;
            case 3 -> PhpClass.Visibility.PRIVATE;
            default -> PhpClass.Visibility.PUBLIC;
        };
    }

    private static boolean isSameOrSubclass(PhpClass maybeChild, PhpClass maybeParent) {
        if (maybeChild == null || maybeParent == null) return false;
        PhpClass c = maybeChild;
        while (c != null) {
            if (c == maybeParent) return true;
            c = c.getParent();
        }
        return false;
    }

    private static PhpClass callerClassFromCtx(String callerClassName) {
        String ctx = normClassCtx(callerClassName);
        if (ctx.isEmpty()) return null;
        return CLASSES.get(ctx);
    }

    private static void denyMember(String kind, String declClass, String member, String callerCtx) {
        String ctx = (callerCtx == null) ? "" : callerCtx;
        throw new BasePhpValue.PhpRuntimeException(
                "Cannot access " + kind + " " + declClass + "::" + member + " from context '" + ctx + "'"
        );
    }

    private static void assertPhpMemberVisibility(
            PhpClass declaringClass,
            PhpClass.Visibility vis,
            String callerClassName,
            String kindForMsg,
            String memberForMsg
    ) {
        if (vis == PhpClass.Visibility.PUBLIC) return;

        PhpClass caller = callerClassFromCtx(callerClassName);
        if (caller == null) {
            denyMember(vis == PhpClass.Visibility.PRIVATE ? "private " + kindForMsg : "protected " + kindForMsg,
                    declaringClass.getName(), memberForMsg, normClassCtx(callerClassName));
            return;
        }

        if (vis == PhpClass.Visibility.PRIVATE) {
            if (caller != declaringClass) {
                denyMember("private " + kindForMsg, declaringClass.getName(), memberForMsg, caller.getName());
            }
            return;
        }

        // PROTECTED
        if (!isSameOrSubclass(caller, declaringClass)) {
            denyMember("protected " + kindForMsg, declaringClass.getName(), memberForMsg, caller.getName());
        }
    }

    public static void defineProperty(String className, String propName, boolean isStatic, int visibility, BasePhpValue defaultValue) {
        String cn = norm(className);
        if (cn.isEmpty()) throw new BasePhpValue.PhpRuntimeException("Invalid class name");

        // Ensure class exists
        PhpClass c = requireClass(cn);

        PhpClass.Visibility vis = visFromInt(visibility);
        c.declareProperty(propName, isStatic, vis, defaultValue);
    }

    public static void defineConst(String className, String constName, int visibility, BasePhpValue value) {
        String cn = norm(className);
        if (cn.isEmpty()) throw new BasePhpValue.PhpRuntimeException("Invalid class name");

        PhpClass c = requireClass(cn);
        PhpClass.Visibility vis = visFromInt(visibility);
        c.defineConst(constName, vis, value);
    }

    private static final class PropLookup {
        final PhpClass declClass;
        final PhpClass.DeclaredProperty prop;

        PropLookup(PhpClass declClass, PhpClass.DeclaredProperty prop) {
            this.declClass = declClass;
            this.prop = prop;
        }
    }

    private static final class ConstLookup {
        final PhpClass declClass;
        final PhpClass.DeclaredConst c;

        ConstLookup(PhpClass declClass, PhpClass.DeclaredConst c) {
            this.declClass = declClass;
            this.c = c;
        }
    }

    private static PropLookup findStaticProp(PhpClass start, String propName) {
        PhpClass cur = start;
        while (cur != null) {
            PhpClass.DeclaredProperty p = cur.getDeclaredPropertyHere(propName);
            if (p != null && p.isStatic) return new PropLookup(cur, p);
            cur = cur.getParent();
        }
        return null;
    }

    private static PropLookup findInstanceProp(PhpClass start, String propName) {
        PhpClass cur = start;
        while (cur != null) {
            PhpClass.DeclaredProperty p = cur.getDeclaredPropertyHere(propName);
            if (p != null && !p.isStatic) return new PropLookup(cur, p);
            cur = cur.getParent();
        }
        return null;
    }

    private static ConstLookup findConst(PhpClass start, String constName) {
        PhpClass cur = start;
        while (cur != null) {
            PhpClass.DeclaredConst c = cur.getDeclaredConstHere(constName);
            if (c != null) return new ConstLookup(cur, c);
            cur = cur.getParent();
        }
        return null;
    }

    public static BasePhpValue getStaticPropCtx(PhpClass calledClass, String propName, String callerClassName) {
        if (calledClass == null) throw new BasePhpValue.PhpRuntimeException("Static access on null class");
        if (propName == null) propName = "";

        PropLookup hit = findStaticProp(calledClass, propName);
        if (hit == null) {
            throw new BasePhpValue.PhpRuntimeException("Access to undeclared static property " + calledClass.getName() + "::$" + propName);
        }

        assertPhpMemberVisibility(hit.declClass, hit.prop.visibility, callerClassName, "property", "$" + propName);
        return hit.declClass.getStaticPropValueHere(propName);
    }

    public static BasePhpValue setStaticPropCtx(PhpClass calledClass, String propName, BasePhpValue value, String callerClassName) {
        if (calledClass == null) throw new BasePhpValue.PhpRuntimeException("Static access on null class");
        if (propName == null) propName = "";

        PropLookup hit = findStaticProp(calledClass, propName);
        if (hit == null) {
            throw new BasePhpValue.PhpRuntimeException("Access to undeclared static property " + calledClass.getName() + "::$" + propName);
        }

        assertPhpMemberVisibility(hit.declClass, hit.prop.visibility, callerClassName, "property", "$" + propName);
        BasePhpValue v = (value == null) ? BasePhpValue.NULL_VALUE : value;
        hit.declClass.setStaticPropValueHere(propName, v);
        return v;
    }

    public static BasePhpValue getConstCtx(PhpClass calledClass, String constName, String callerClassName) {
        if (calledClass == null) throw new BasePhpValue.PhpRuntimeException("Const access on null class");
        if (constName == null) constName = "";

        ConstLookup hit = findConst(calledClass, constName);
        if (hit == null) {
            throw new BasePhpValue.PhpRuntimeException("Undefined class constant " + calledClass.getName() + "::" + constName);
        }

        assertPhpMemberVisibility(hit.declClass, hit.c.visibility, callerClassName, "constant", constName);
        return (hit.c.value == null) ? BasePhpValue.NULL_VALUE : hit.c.value;
    }

    public static BasePhpValue getParentStaticPropCtx(String currentClassName, String propName, String callerClassName) {
        if (currentClassName == null) currentClassName = "";
        PhpClass cur = requireClass(currentClassName);
        PhpClass parent = cur.getParent();
        if (parent == null) throw new BasePhpValue.PhpRuntimeException("Cannot access parent:: when current class has no parent");
        return getStaticPropCtx(parent, propName, callerClassName);
    }

    public static BasePhpValue setParentStaticPropCtx(String currentClassName, String propName, BasePhpValue value, String callerClassName) {
        if (currentClassName == null) currentClassName = "";
        PhpClass cur = requireClass(currentClassName);
        PhpClass parent = cur.getParent();
        if (parent == null) throw new BasePhpValue.PhpRuntimeException("Cannot access parent:: when current class has no parent");
        return setStaticPropCtx(parent, propName, value, callerClassName);
    }

    public static BasePhpValue getParentConstCtx(String currentClassName, String constName, String callerClassName) {
        if (currentClassName == null) currentClassName = "";
        PhpClass cur = requireClass(currentClassName);
        PhpClass parent = cur.getParent();
        if (parent == null) throw new BasePhpValue.PhpRuntimeException("Cannot access parent:: when current class has no parent");
        return getConstCtx(parent, constName, callerClassName);
    }

    public static BasePhpValue getPropCtx(PhpObject obj, String propName, String callerClassName) {
        if (obj == null) throw new BasePhpValue.PhpRuntimeException("Access to property on null");
        if (propName == null) propName = "";

        // If declared, enforce visibility; else treat as dynamic public property
        PropLookup hit = findInstanceProp(obj.getPhpClass(), propName);
        if (hit != null) {
            assertPhpMemberVisibility(hit.declClass, hit.prop.visibility, callerClassName, "property", "$" + propName);
        }

        return obj.getProperty(propName);
    }

    public static BasePhpValue setPropCtx(PhpObject obj, String propName, BasePhpValue value, String callerClassName) {
        if (obj == null) throw new BasePhpValue.PhpRuntimeException("Access to property on null");
        if (propName == null) propName = "";

        PropLookup hit = findInstanceProp(obj.getPhpClass(), propName);
        if (hit != null) {
            assertPhpMemberVisibility(hit.declClass, hit.prop.visibility, callerClassName, "property", "$" + propName);
        }

        BasePhpValue v = (value == null) ? BasePhpValue.NULL_VALUE : value;
        obj.setProperty(propName, v);
        return v;
    }

    private static final List<PhpObject> LIVE_OBJECTS =
            Collections.synchronizedList(new ArrayList<>());

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            List<PhpObject> snapshot;
            synchronized (LIVE_OBJECTS) {
                snapshot = new ArrayList<>(LIVE_OBJECTS);
            }
            for (int i = snapshot.size() - 1; i >= 0; i--) {
                PhpObject o = snapshot.get(i);
                try {
                    callMethod(o, "__destruct", new BasePhpValue[0]);
                } catch (BasePhpValue.PhpRuntimeException ex) {
                    String msg = ex.getMessage();
                    if (msg == null || !msg.toLowerCase().contains("undefined method")) {
                        throw ex;
                    }
                } catch (Throwable ignored) {
                }
            }
        }, "PhpRuntimeShutdown"));
    }

    public static final class PhpThrown extends RuntimeException {
        public final BasePhpValue value;

        public PhpThrown(BasePhpValue v) {
            super(safeMsg(v));
            this.value = (v == null) ? BasePhpValue.NULL_VALUE : v;
        }

        private static String safeMsg(BasePhpValue v) {
            try {
                if (v == null) return "";
                return v.toPhpString();
            } catch (Throwable ignored) {
                return "";
            }
        }
    }

    // Used by bytecode for "throw <expr>;"
    public static Throwable toThrowable(BasePhpValue v) {
        // PHP requires throwing objects; for now wrap non-objects into Exception(message)
        BasePhpValue vv = (v == null) ? BasePhpValue.NULL_VALUE : v;
        if (!vv.isObject()) {
            BasePhpValue ex = newObject("Exception", new BasePhpValue[]{vv});
            return new PhpThrown(ex);
        }
        return new PhpThrown(vv);
    }

    // Used by catch handler to get a PHP value from any JVM throwable
    public static BasePhpValue unwrapThrowable(Throwable t) {
        if (t instanceof PhpThrown pt) {
            return pt.value == null ? BasePhpValue.NULL_VALUE : pt.value;
        }
        String msg = (t == null || t.getMessage() == null) ? "" : t.getMessage();
        // Treat “foreign” throwables as RuntimeException in PHP-space
        return newObject("RuntimeException", new BasePhpValue[]{BasePhpValue.of(msg)});
    }

    // Catch type check: is $ex instanceof <phpClassName> (via PhpClass parent chain)
    public static boolean isInstanceOf(BasePhpValue v, String className) {
        if (v == null || !v.isObject()) return false;
        if (className == null) className = "";
        String want = className.toLowerCase();

        PhpObject o = v.asObject();
        PhpClass c = o.getPhpClass();
        while (c != null) {
            if (c.getName().toLowerCase().equals(want)) return true;
            c = c.getParent();
        }
        return false;
    }

    private static Class<?> tryJvmClassFromPhpName(String phpClassName) {
        String n = norm(phpClassName).replace('/', '.');
        try {
            return Class.forName(n);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

}
