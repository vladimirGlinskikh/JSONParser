package kz.zhelezyaka.jsonparser.json;

public class BaseParser {
    public static final char END = 0;
    protected CharSource source;
    protected char ch;

    public BaseParser(CharSource source) {
        this.source = source;
        take();
    }

    protected char take() {
        final char result = ch;
        ch = source.hasNext() ? source.next() : END;
        return result;
    }

    protected boolean take(char expected) {
        if (test(expected)) {
            take();
            return true;
        } else {
            return false;
        }
    }

    protected void expect(String chars) {
        for (char ch : chars.toCharArray()) {
            expect(ch);
        }
    }

    protected void expect(char expected) throws IllegalArgumentException {
        if (!take(expected)) {
            throw error("Expected '" +
                    expected + "', found '" +
                    errorChar());
        }
    }

    protected IllegalArgumentException error(String message) {
        return source.error(message);
    }

    protected void checkEOF() {
        if (!eof()) {
            throw error("Expected EOF, found " + errorChar());
        }
    }

    protected boolean eof() {
        return ch == END;
    }

    protected boolean test(char expected) {
        return ch == expected;
    }

    protected String errorChar() {
        return ch == END ? "EOF" : "'" + ch + "'";
    }

    protected boolean between(char min, char max) {
        return min <= ch && ch <= max;
    }
}
