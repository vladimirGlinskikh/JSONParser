package kz.zhelezyaka.jsonparser.json;

public class CharSourceImpl implements CharSource {
    private final String string;
    private int pos;

    public CharSourceImpl(String string) {
        this.string = string;
    }

    @Override
    public boolean hasNext() {
        return pos < string.length();
    }

    @Override
    public char next() {
        return string.charAt(pos++);
    }

    @Override
    public IllegalArgumentException error(String message) {
        return new IllegalArgumentException(pos + ": " + message);
    }
}
