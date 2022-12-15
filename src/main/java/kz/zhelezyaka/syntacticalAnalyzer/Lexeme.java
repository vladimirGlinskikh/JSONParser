package kz.zhelezyaka.syntacticalAnalyzer;

import java.util.ArrayList;
import java.util.List;

public class Lexeme {
    LexemeType type;
    String value;

    public Lexeme(LexemeType type, String value) {
        this.type = type;
        this.value = value;
    }

    public Lexeme(LexemeType type, Character value) {
        this.type = type;
        this.value = value.toString();
    }

    @Override
    public String toString() {
        return "Lexeme{" +
                "type=" + type +
                ", value='" + value + '\'' +
                '}';
    }

    public static List<Lexeme> lexAnalyzer(String expText) {
        ArrayList<Lexeme> lexemes = new ArrayList<>();
        int pos = 0;
        while (pos < expText.length()) {
            char ch = expText.charAt(pos);
            switch (ch) {
                case '(' -> {
                    lexemes.add(new Lexeme(LexemeType.LEFT_BRACKET, ch));
                    pos++;
                }
                case ')' -> {
                    lexemes.add(new Lexeme(LexemeType.RIGHT_BRACKET, ch));
                    pos++;
                }
                case '+' -> {
                    lexemes.add(new Lexeme(LexemeType.PLUS, ch));
                    pos++;
                }
                case '-' -> {
                    lexemes.add(new Lexeme(LexemeType.MINUS, ch));
                    pos++;
                }
                case '/' -> {
                    lexemes.add(new Lexeme(LexemeType.DIVIDE, ch));
                    pos++;
                }
                case '*' -> {
                    lexemes.add(new Lexeme(LexemeType.MULTIPLE, ch));
                    pos++;
                }
                default -> {
                    if (ch <= '9' && ch >= '0') {
                        StringBuilder stringBuilder = new StringBuilder();
                        do {
                            stringBuilder.append(ch);
                            pos++;
                            if (pos >= expText.length()) {
                                break;
                            }
                            ch = expText.charAt(pos);
                        } while (ch <= '9' && ch >= '0');
                        lexemes.add(new Lexeme(LexemeType.NUMBER, stringBuilder.toString()));
                    } else {
                        if (ch != ' ') {
                            throw new RuntimeException("Unexpected character: " + ch);
                        }
                        pos++;
                    }
                }
            }
        }
        lexemes.add(new Lexeme(LexemeType.EOF, ""));
        return lexemes;
    }
}