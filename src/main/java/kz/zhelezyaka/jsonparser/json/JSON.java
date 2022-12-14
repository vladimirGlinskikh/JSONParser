package kz.zhelezyaka.jsonparser.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class JSON {

    private JSON() {
    }

    static Object parse(final CharSource source) {
        return new JSONParser(source).parseJSON();
    }

    static Object parse(final String string) {
        return parse(new CharSourceImpl(string));
    }

    private static class JSONParser extends BaseParser {

        public JSONParser(CharSource source) {
            super(source);
        }

        public Object parseJSON() {
            final Object result = parseElement();
            checkEOF();
            return result;
        }

        private Object parseElement() {
            skipWhiteSpace();
            final Object result = parseValue();
            skipWhiteSpace();
            return result;
        }

        private Object parseValue() {
            if (take('t')) {
                expect("rue");
                return true;
            } else if (take('f')) {
                expect("alse");
                return false;
            } else if (take('n')) {
                expect("ull");
                return null;
            } else if (test('"')) {
                return parseString();
            } else if (test('-') || between('0', '9')) {
                return parseInteger();
            } else if (take('[')) {
                return parseArray();
            } else if (take('{')) {
                return parseObject();
            } else {
                throw error("Unsupported input");
            }
        }

        private Map<String, Object> parseObject() {
            skipWhiteSpace();
            if (take('}')) {
                return Map.of();
            }
            final Map<String, Object> objectMap = new HashMap<>();
            do {
                skipWhiteSpace();
                final String key = parseString();
                skipWhiteSpace();
                expect(':');
                final Object value = parseElement();
                objectMap.put(key, value);
            } while (take(','));
            expect('}');
            return objectMap;
        }

        private List<Object> parseArray() {
            skipWhiteSpace();
            if (take(']')) {
                return List.of();
            }
            final List<Object> elements = new ArrayList<>();
            do {
                elements.add(parseElement());
            } while (take(','));
            expect(']');
            return elements;
        }

        private Integer parseInteger() {
            final StringBuilder number = new StringBuilder();
            if (take('-')) {
                number.append('-');
            }
            if (take('0')) {
                return 0;
            }
            while (between('0', '9')) {
                number.append(take());
            }
            return Integer.parseInt(number.toString());
        }

        private String parseString() {
            final StringBuilder sb = new StringBuilder();
            expect('"');
            while (!eof() && !test('"')) {
                if (take('\\')) {
                    sb.append(parseEscape());
                }
                sb.append(take());
            }
            expect('"');
            return sb.toString();
        }

        private char parseEscape() {
            if (take('r')) {
                return '\r';
            } else if (take('n')) {
                return '\n';
            } else if (take('t')) {
                return '\t';
            } else if (take('"')) {
                return '"';
            } else if (take('\\')) {
                return '\\';
            } else if (take('/')) {
                return '/';
            } else if (take('b')) {
                return '\b';
            } else if (take('f')) {
                return '\f';
            } else {
                throw error("Expected escape symbol, found " + errorChar());
            }
        }

        private void skipWhiteSpace() {
            while (take(' ') ||
                    take('\r') ||
                    take('\n') ||
                    take('\t')) {
            }
        }
    }
}
