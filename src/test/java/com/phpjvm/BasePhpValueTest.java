package com.phpjvm;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class BasePhpValueTest {

    // ---------- Helpers ----------
    private static BasePhpValue n() {
        return BasePhpValue.NULL_VALUE;
    }

    private static BasePhpValue b(boolean v) {
        return BasePhpValue.of(v);
    }

    private static BasePhpValue i(long v) {
        return BasePhpValue.of(v);
    }

    private static BasePhpValue f(double v) {
        return BasePhpValue.of(v);
    }

    private static BasePhpValue s(String v) {
        return BasePhpValue.of(v);
    }

    private static BasePhpValue arr() {
        return BasePhpValue.array();
    }

    // ---------- Factories / types ----------
    @Test
    @DisplayName("Factories set correct types")
    void factories_setCorrectTypes() {
        assertEquals(BasePhpValue.Type.NULL, n().getType());
        assertEquals(BasePhpValue.Type.BOOL, b(true).getType());
        assertEquals(BasePhpValue.Type.INT, i(123).getType());
        assertEquals(BasePhpValue.Type.FLOAT, f(1.25).getType());
        assertEquals(BasePhpValue.Type.STRING, s("hi").getType());
        assertEquals(BasePhpValue.Type.ARRAY, arr().getType());
    }

    @Test
    @DisplayName("create(Object) wraps common host types")
    void create_wrapsHostTypes() {
        assertSame(n(), BasePhpValue.create(null));
        assertEquals(BasePhpValue.Type.BOOL, BasePhpValue.create(true).getType());
        assertEquals(BasePhpValue.Type.INT, BasePhpValue.create(5).getType());
        assertEquals(BasePhpValue.Type.INT, BasePhpValue.create(5L).getType());
        assertEquals(BasePhpValue.Type.FLOAT, BasePhpValue.create(1.5).getType());
        assertEquals(BasePhpValue.Type.FLOAT, BasePhpValue.create(1.5f).getType());
        assertEquals(BasePhpValue.Type.STRING, BasePhpValue.create("x").getType());
    }

    @Test
    @DisplayName("create(Map) builds array with coerced keys/values")
    void create_mapBuildsArray() {
        Map<Object, Object> m = new LinkedHashMap<>();
        m.put("a", 1);
        m.put(2, "b");
        m.put(true, 7);
        BasePhpValue v = BasePhpValue.create(m);
        assertTrue(v.isArray());

        // "a" => 1
        assertEquals("1", BasePhpValue.arrayGet(v, s("a")).toPhpString());

        // 2 => "b"
        assertEquals("b", BasePhpValue.arrayGet(v, i(2)).toPhpString());

        // true key coerces to 1
        assertEquals("7", BasePhpValue.arrayGet(v, i(1)).toPhpString());
    }

    // ---------- toBool / toPhpString ----------
    static Stream<Arguments> truthiness_cases() {
        return Stream.of(
                Arguments.of(n(), false),
                Arguments.of(b(false), false),
                Arguments.of(b(true), true),
                Arguments.of(i(0), false),
                Arguments.of(i(1), true),
                Arguments.of(f(0.0), false),
                Arguments.of(f(0.0001), true),
                Arguments.of(s(""), false),
                Arguments.of(s("0"), false),
                Arguments.of(s("00"), true),
                Arguments.of(s("hi"), true),
                Arguments.of(arr(), false) // empty array
        );
    }

    @ParameterizedTest(name = "toBool({0}) == {1}")
    @MethodSource("truthiness_cases")
    @DisplayName("toBool() implements PHP-ish truthiness")
    void toBool_phpishTruthiness(BasePhpValue v, boolean expected) {
        assertEquals(expected, v.toBool());
    }

    static Stream<Arguments> phpString_cases() {
        return Stream.of(
                Arguments.of(n(), ""),
                Arguments.of(b(false), ""),
                Arguments.of(b(true), "1"),
                Arguments.of(i(123), "123"),
                Arguments.of(f(2.0), "2"),
                Arguments.of(f(2.5), "2.5"),
                Arguments.of(s("hi"), "hi"),
                Arguments.of(arr(), "Array")
        );
    }

    @ParameterizedTest(name = "toPhpString({0}) == \"{1}\"")
    @MethodSource("phpString_cases")
    @DisplayName("toPhpString() matches your runtime rules")
    void toPhpString_phpish(BasePhpValue v, String expected) {
        assertEquals(expected, v.toPhpString());
    }

    // ---------- Arithmetic ----------
    static Stream<Arguments> arithmetic_ok_cases() {
        return Stream.of(
                // int + int
                Arguments.of("add", i(2), i(3), "5"),
                Arguments.of("sub", i(10), i(7), "3"),
                Arguments.of("mul", i(6), i(7), "42"),
                Arguments.of("mod", i(10), i(4), "2"),

                // int + float => float
                Arguments.of("add", i(2), f(0.5), "2.5"),
                Arguments.of("mul", f(1.5), i(2), "3"),

                // division always float
                Arguments.of("div", i(7), i(2), "3.5"),

                // numeric strings (STRICT for arithmetic in your code)
                Arguments.of("add", s("  12 "), i(3), "15"),
                Arguments.of("mul", s("-2"), s("3"), "-6"),
                Arguments.of("add", s("1e3"), i(1), "1001"),
                Arguments.of("add", s("1.2E-1"), i(1), "1.12")
        );
    }

    @ParameterizedTest(name = "{0}({1}, {2}) => {3}")
    @MethodSource("arithmetic_ok_cases")
    @DisplayName("Arithmetic ops work for ints/floats/strict-numeric strings")
    void arithmetic_ok(String op, BasePhpValue a, BasePhpValue b, String expectedPhpString) {
        BasePhpValue r = switch (op) {
            case "add" -> BasePhpValue.add(a, b);
            case "sub" -> BasePhpValue.sub(a, b);
            case "mul" -> BasePhpValue.mul(a, b);
            case "div" -> BasePhpValue.div(a, b);
            case "mod" -> BasePhpValue.mod(a, b);
            default -> throw new IllegalArgumentException("bad op: " + op);
        };
        assertEquals(expectedPhpString, r.toPhpString());
    }

    @Test
    @DisplayName("Division by zero throws PhpRuntimeException")
    void divByZero_throws() {
        BasePhpValue.PhpRuntimeException ex =
                assertThrows(BasePhpValue.PhpRuntimeException.class, () -> BasePhpValue.div(i(1), i(0)));
        assertEquals("Division by zero", ex.getMessage());
    }

    @Test
    @DisplayName("Modulo by zero throws PhpRuntimeException")
    void modByZero_throws() {
        BasePhpValue.PhpRuntimeException ex =
                assertThrows(BasePhpValue.PhpRuntimeException.class, () -> BasePhpValue.mod(i(1), i(0)));
        assertEquals("Modulo by zero", ex.getMessage());
    }

    static Stream<Arguments> arithmetic_typeError_cases() {
        return Stream.of(
                Arguments.of("add", s("dfjkewlfkjlefe"), i(2), "Unsupported operand types: string + int"),
                Arguments.of("sub", arr(), i(1), "Unsupported operand types: array - int"),
                Arguments.of("mul", i(1), arr(), "Unsupported operand types: array * int"),
                Arguments.of("div", s("12abc"), i(2), "Unsupported operand types: string / int"), // junk rejected
                Arguments.of("mod", s(" "), i(2), "Unsupported operand types: string % int") // strict => not numeric
        );
    }

    @ParameterizedTest(name = "{0} type error: {1} and {2}")
    @MethodSource("arithmetic_typeError_cases")
    @DisplayName("Arithmetic throws PhpTypeError with correct message for unsupported types")
    void arithmetic_typeErrors(String op, BasePhpValue a, BasePhpValue b, String expectedMessage) {
        BasePhpValue.PhpTypeError ex = assertThrows(BasePhpValue.PhpTypeError.class, () -> {
            switch (op) {
                case "add" -> BasePhpValue.add(a, b);
                case "sub" -> BasePhpValue.sub(a, b);
                case "mul" -> BasePhpValue.mul(a, b);
                case "div" -> BasePhpValue.div(a, b);
                case "mod" -> BasePhpValue.mod(a, b);
                default -> throw new IllegalArgumentException("bad op: " + op);
            }
        });
        assertEquals(expectedMessage, ex.getMessage());
    }

    // ---------- concat ----------
    @Test
    @DisplayName("concat uses toPhpString() and returns STRING")
    void concat_basic() {
        BasePhpValue r = BasePhpValue.concat(s("a"), i(2));
        assertEquals(BasePhpValue.Type.STRING, r.getType());
        assertEquals("a2", r.toPhpString());
    }

    // ---------- Equality vs identity ----------
    static Stream<Arguments> eq_cases() {
        return Stream.of(
                Arguments.of(i(2), s("2"), true),
                Arguments.of(s("foo"), i(0), true), // your eq uses toNumberForArithmetic() with lenient fallback => "foo" -> 0
                Arguments.of(n(), s(""), true),      // null -> 0, "" -> 0
                Arguments.of(b(false), s("0"), true),
                Arguments.of(b(true), s("1"), true)
        );
    }

    @ParameterizedTest
    @MethodSource("eq_cases")
    @DisplayName("eq (==) follows your numeric-ish coercion path")
    void eq_works(BasePhpValue a, BasePhpValue b, boolean expected) {
        assertEquals(expected, BasePhpValue.eqBool(a, b));
        assertEquals(expected ? "1" : "", BasePhpValue.eq(a, b).toPhpString());
    }

    static Stream<Arguments> identical_cases() {
        return Stream.of(
                Arguments.of(i(2), i(2), true),
                Arguments.of(i(2), s("2"), false),
                Arguments.of(n(), n(), true),
                Arguments.of(b(true), b(true), true),
                Arguments.of(b(true), i(1), false)
        );
    }

    @ParameterizedTest
    @MethodSource("identical_cases")
    @DisplayName("identical (===) requires same type and same value")
    void identical_works(BasePhpValue a, BasePhpValue b, boolean expected) {
        assertEquals(expected, BasePhpValue.identicalBool(a, b));
        assertEquals(expected ? "1" : "", BasePhpValue.identical(a, b).toPhpString());
    }

    // ---------- Relational comparisons (<, <=, >, >=, <=>) ----------
    static Stream<Arguments> relational_cases() {
        return Stream.of(
                // numeric compare when both numeric-ish (including strict numeric strings)
                Arguments.of("lt", s("2"), i(3), true),
                Arguments.of("lt", s(" 2 "), i(3), true),
                Arguments.of("gt", s(" 2 "), i(3), false),
                Arguments.of("ge", i(3), s("3"), true),

                // non-numeric string vs number => string compare using toPhpString
                Arguments.of("lt", s("dsd "), i(3), false),
                Arguments.of("gt", s("dsd "), i(3), true),

                // string vs string
                Arguments.of("lt", s("a"), s("b"), true),
                Arguments.of("gt", s("a"), s("b"), false)
        );
    }

    @ParameterizedTest(name = "{0}({1}, {2}) == {3}")
    @MethodSource("relational_cases")
    @DisplayName("Relational comparisons match compareForRelational() behavior")
    void relational_ops(String op, BasePhpValue a, BasePhpValue b, boolean expected) {
        boolean actual = switch (op) {
            case "lt" -> BasePhpValue.ltBool(a, b);
            case "le" -> BasePhpValue.leBool(a, b);
            case "gt" -> BasePhpValue.gt(a, b).toBool();
            case "ge" -> BasePhpValue.ge(a, b).toBool();
            default -> throw new IllegalArgumentException("bad op: " + op);
        };
        assertEquals(expected, actual);
    }

    static Stream<Arguments> spaceship_cases() {
        return Stream.of(
                Arguments.of(i(1), i(2), -1),
                Arguments.of(i(2), i(2), 0),
                Arguments.of(i(3), i(2), 1),
                Arguments.of(s("dsd "), i(3), 1), // "dsd " vs "3" => "dsd " > "3"
                Arguments.of(s("2"), i(3), -1)    // numeric-ish -> 2 < 3
        );
    }

    @ParameterizedTest
    @MethodSource("spaceship_cases")
    @DisplayName("spaceship returns -1/0/1 (INT) according to compareForRelational")
    void spaceship_works(BasePhpValue a, BasePhpValue b, long expected) {
        BasePhpValue r = BasePhpValue.spaceship(a, b);
        assertEquals(BasePhpValue.Type.INT, r.getType());
        assertEquals(expected, r.asLongStrict());
    }

    @Test
    @DisplayName("Relational comparisons involving arrays throw PhpTypeError")
    void relational_arraysThrow() {
        BasePhpValue.PhpTypeError ex =
                assertThrows(BasePhpValue.PhpTypeError.class, () -> BasePhpValue.ltBool(arr(), i(1)));
        assertEquals("Cannot compare arrays", ex.getMessage());
    }

    // ---------- Array operations ----------
    @Test
    @DisplayName("arraySet/arrayGet basic and missing key returns NULL_VALUE")
    void array_set_get_and_missing() {
        BasePhpValue a = arr();

        // missing
        assertSame(n(), BasePhpValue.arrayGet(a, s("nope")));

        // set + get
        BasePhpValue.arraySet(a, s("x"), i(7));
        assertEquals("7", BasePhpValue.arrayGet(a, s("x")).toPhpString());
    }

    @Test
    @DisplayName("array key coercion: numeric string key '123' becomes int key 123")
    void array_key_numericStringBecomesIntKey() {
        BasePhpValue a = arr();
        BasePhpValue.arraySet(a, s("123"), i(7));

        // access with int key should find the same slot
        assertEquals("7", BasePhpValue.arrayGet(a, i(123)).toPhpString());
    }

    @Test
    @DisplayName("arrayAppend appends with auto-increment keys 0..n")
    void array_append_autoIndex() {
        BasePhpValue a = arr();
        BasePhpValue.arrayAppend(a, s("a"));
        BasePhpValue.arrayAppend(a, s("b"));
        BasePhpValue.arrayAppend(a, s("c"));

        assertEquals("a", BasePhpValue.arrayGet(a, i(0)).toPhpString());
        assertEquals("b", BasePhpValue.arrayGet(a, i(1)).toPhpString());
        assertEquals("c", BasePhpValue.arrayGet(a, i(2)).toPhpString());
    }

    @Test
    @DisplayName("Indexing non-array throws PhpRuntimeException")
    void array_indexing_nonArrayThrows() {
        BasePhpValue.PhpRuntimeException ex =
                assertThrows(BasePhpValue.PhpRuntimeException.class, () -> BasePhpValue.arrayGet(i(1), s("x")));
        assertTrue(ex.getMessage().contains("Cannot index non-array type"));
    }

    // ---------- Logical helpers ----------
    static Stream<Arguments> not_cases() {
        return Stream.of(
                Arguments.of(n(), true),
                Arguments.of(b(false), true),
                Arguments.of(b(true), false),
                Arguments.of(s(""), true),
                Arguments.of(s("0"), true),
                Arguments.of(s("x"), false),
                Arguments.of(i(0), true),
                Arguments.of(i(1), false)
        );
    }

    @ParameterizedTest
    @MethodSource("not_cases")
    @DisplayName("not() matches !toBool()")
    void not_works(BasePhpValue v, boolean expectedBool) {
        assertEquals(expectedBool, BasePhpValue.not(v).toBool());
    }

    @Test
    @DisplayName("and/or are based on toBool()")
    void and_or_work() {
        assertFalse(BasePhpValue.and(s(""), i(1)).toBool());
        assertTrue(BasePhpValue.and(s("x"), i(1)).toBool());
        assertTrue(BasePhpValue.or(s(""), i(1)).toBool());
        assertFalse(BasePhpValue.or(s(""), i(0)).toBool());
    }
}
