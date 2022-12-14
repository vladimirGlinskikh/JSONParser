package kz.zhelezyaka.jsonparser.json;

public interface CharSource {
    boolean hasNext();

    char next();

    IllegalArgumentException error(String message);
}
