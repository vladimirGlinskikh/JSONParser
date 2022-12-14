package kz.zhelezyaka.jsonparser.json;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static kz.zhelezyaka.jsonparser.json.JSON.parse;
import static org.junit.jupiter.api.Assertions.*;

public class JSONTest {
    @Test
    void testBoolean() {
        assertEquals(true, parse("true"));
        assertEquals(false, parse("false"));
    }

    @Test
    void testNull() {
        assertEquals(null, parse("null"));
    }

    @Test
    void testSpace() {
        assertEquals(null, parse("\r\n\tnull"));
        assertEquals(null, parse("\r\n\tnull \n\r\t"));
        assertEquals(null, parse("\r\n\tnull \r\n\t"));
    }

    @Test
    void testString() {
        assertEquals("", parse("\"\""));
        assertEquals("abc", parse("\"abc\""));
        assertEquals("123", parse("\"123\""));
    }

    @Test
    void testInteger() {
        assertEquals(0, parse("0"));
        assertEquals(0, parse("-0"));
        assertEquals(1234, parse("1234"));
    }

    @Test
    void testArray() {
        assertEquals(List.of(), parse("[]"));
        assertEquals(List.of(), parse("[ ]"));
        assertEquals(List.of(true), parse("[ true ]"));
        assertEquals(List.of(true, false, 123), parse("[true, false,123]"));
        assertEquals(List.of(true, false, 123), parse("[ true , false , 123 ]"));
    }

    @Test
    void testNestedArray() {
        assertEquals(List.of(List.of()), parse("[[]]"));
        assertEquals(List.of(List.of(), List.of()), parse("[[], []]"));
        assertEquals(
                List.of(List.of(true, false), List.of(1234), "good parse"),
                parse("[[true, false], [1234], \"good parse\"]"));
    }

    @Test
    void testMap() {
        assertEquals(Map.of(), parse("{}"));
        assertEquals(Map.of(), parse("{ }"));

        assertEquals(Map.of("good", true), parse("{\"good\": true}"));

        assertEquals(Map.of("good", true, "parse", false),
                parse("{\"good\": true, \"parse\": false}"));

        assertEquals(Map.of("good", true, "parse", false),
                parse("{ \"good\" : true , \"parse\" : false }"));

        assertEquals(Map.of("good", true, "parse", false),
                parse("{\"good\":true,\"parse\":false}"));
    }

    @Test
    void testEscape() {
//        System.out.println(JSON.parse("\"\\\"\\\\/\\n\\r\\t\\f\\b\""));
//        assertEquals("\"\\/\n\r\t\f\b", "\"\\\"\\\\/\\n\\r\\t\\f\\b\"");
//        assertEquals("\"\\/\\\n\r\t\f\b", "\"\\/\\\n\r\t\f\b");
        assertEquals("\"", "\"");
        assertEquals("\n", "\n");
//        assertEquals("\"\n\r", "\"\\\"\\n\\r\"");
    }
}