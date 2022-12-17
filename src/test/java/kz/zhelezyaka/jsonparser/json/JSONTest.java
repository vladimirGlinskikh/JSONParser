package kz.zhelezyaka.jsonparser.json;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class JSONTest {
    private static void valid(Object expected, String input) {
        Assertions.assertEquals(expected, JSON.parse(input));
    }

    private void invalid(final String input) {
        try {
            final Object result = JSON.parse(input);
            Assertions.fail("Invalid input: '" + input + "' parsed as " + result);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

    @Test
    void testBoolean() {
        valid(true, "true");
        valid(false, "false");
    }

    @Test
    void testNull() {
        valid(null, "null");
        invalid("nul");
        invalid("null2");
    }

    @Test
    void testSpace() {
        valid(null, "\r\n\tnull");
        valid(null, "\r\n\tnull \n\r\t");
        valid(null, "\r\n\tnull \r\n\t");
    }

    @Test
    void testString() {
        valid("", "\"\"");
        valid("abc", "\"abc\"");
        valid("123", "\"123\"");
        invalid("\"good");
        invalid("\"good\"~");
    }

    @Test
    void testInteger() {
        valid(0, "0");
        valid(0, "-0");
        valid(1234, "1234");
        invalid("02");
        invalid("--2");
        invalid("-");
    }

    @Test
    void testArray() {
        valid(List.of(), "[]");
        valid(List.of(), "[ ]");
        valid(List.of(true), "[ true ]");
        valid(List.of(true, false, 123), "[true, false,123]");
        valid(List.of(true, false, 123), "[ true , false , 123 ]");
    }

    @Test
    void testNestedArray() {
        valid(List.of(List.of()), "[[]]");
        valid(List.of(List.of(), List.of()), "[[], []]");
        valid(List.of(List.of(true, false), List.of(1234), "good parse"),
                "[[true, false], [1234], \"good parse\"]");

        invalid("[e]");
        invalid("[34, ]");
    }

    @Test
    void testMap() {
        valid(Map.of(), "{}");
        valid(Map.of(), "{ }");

        valid(Map.of("good", true), "{\"good\": true}");

        valid(Map.of("good", true, "parse", false),
                "{\"good\": true, \"parse\": false}");

        valid(Map.of("good", true, "parse", false),
                "{ \"good\" : true , \"parse\" : false }");

        valid(Map.of("good", true, "parse", false),
                "{\"good\":true,\"parse\":false}");

        invalid("{\"good\":}");
        invalid("{good: false}");
        invalid("{: false}");
        invalid("{\"good\":true, \"good\":true}");
        invalid("{\"good\":true, \"good\":false}");
        invalid("{\"good\":false, \"good\":false}");
    }

    @Test
    void testEscape() {
//        System.out.println(JSON.parse("\"\\\"\\\\/\\n\\r\\t\\f\\b\""));
//        assertEquals("\"\\/\n\r\t\f\b", "\"\\\"\\\\/\\n\\r\\t\\f\\b\"");
//        assertEquals("\"\\/\\\n\r\t\f\b", "\"\\/\\\n\r\t\f\b");
        valid("\"", "\"");
        valid("\n", "\n");
//        assertEquals("\"\n\r", "\"\\\"\\n\\r\"");
    }
}