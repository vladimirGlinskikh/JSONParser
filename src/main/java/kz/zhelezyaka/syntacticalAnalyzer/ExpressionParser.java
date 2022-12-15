package kz.zhelezyaka.syntacticalAnalyzer;

import java.util.ArrayList;
import java.util.List;

import static kz.zhelezyaka.syntacticalAnalyzer.ExpressionParser.Lexeme.*;
import static kz.zhelezyaka.syntacticalAnalyzer.ExpressionParser.Lexeme.lexAnalyzer;

public class ExpressionParser {
    public static void main(String[] args) {
        String expText = "(55 + 5 * (3 - 2)) * 2";
        List<Lexeme> lexemes = lexAnalyzer(expText);
        LexemeBuffer lexemeBuffer = new LexemeBuffer(lexemes);
        System.out.println(expr(lexemeBuffer));
    }

    public enum LexemeType {
        LEFT_BRACKET, RIGHT_BRACKET,
        PLUS, MINUS, MULTIPLE, DIVIDE,
        NUMBER,
        EOF;
    }

    public static class Lexeme {
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

        public static class LexemeBuffer {
            private int pos;
            public List<Lexeme> lexemes;

            public LexemeBuffer(List<Lexeme> lexemes) {
                this.lexemes = lexemes;
            }

            public Lexeme next() {
                return lexemes.get(pos++);
            }

            public void back() {
                pos--;
            }

            public int getPos() {
                return pos;
            }
        }

        public static List<Lexeme> lexAnalyzer(String expText) {
            ArrayList<Lexeme> lexemes = new ArrayList<>();
            int pos = 0;
            while (pos < expText.length()) {
                char ch = expText.charAt(pos);
                switch (ch) {
                    case '(':
                        lexemes.add(new Lexeme(LexemeType.LEFT_BRACKET, ch));
                        pos++;
                        continue;
                    case ')':
                        lexemes.add(new Lexeme(LexemeType.RIGHT_BRACKET, ch));
                        pos++;
                        continue;
                    case '+':
                        lexemes.add(new Lexeme(LexemeType.PLUS, ch));
                        pos++;
                        continue;
                    case '-':
                        lexemes.add(new Lexeme(LexemeType.MINUS, ch));
                        pos++;
                        continue;
                    case '/':
                        lexemes.add(new Lexeme(LexemeType.DIVIDE, ch));
                        pos++;
                        continue;
                    case '*':
                        lexemes.add(new Lexeme(LexemeType.MULTIPLE, ch));
                        pos++;
                        continue;
                    default:
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
            lexemes.add(new Lexeme(LexemeType.EOF, ""));
            return lexemes;
        }
    }

    public static int expr(LexemeBuffer lexemes) {
        Lexeme lexeme = lexemes.next();
        if (lexeme.type == LexemeType.EOF) {
            return 0;
        } else {
            lexemes.back();
            return plusminus(lexemes);
        }
    }

    public static int plusminus(LexemeBuffer lexemes) {
        int value = multdiv(lexemes);
        while (true) {
            Lexeme lexeme = lexemes.next();
            switch (lexeme.type) {
                case PLUS:
                    value += multdiv(lexemes);
                    break;
                case MINUS:
                    value -= multdiv(lexemes);
                    break;
                default:
                    lexemes.back();
                    return value;
            }
        }
    }

    public static int multdiv(LexemeBuffer lexemes) {
        int value = factor(lexemes);
        while (true) {
            Lexeme lexeme = lexemes.next();
            switch (lexeme.type) {
                case MULTIPLE:
                    value *= factor(lexemes);
                    break;
                case DIVIDE:
                    value /= factor(lexemes);
                    break;
                default:
                    lexemes.back();
                    return value;
            }
        }
    }

    public static int factor(LexemeBuffer lexemes) {
        Lexeme lexeme = lexemes.next();
        switch (lexeme.type) {
            case NUMBER:
                return Integer.parseInt(lexeme.value);
            case LEFT_BRACKET:
                int value = expr(lexemes);
                lexeme = lexemes.next();
                if (lexeme.type != LexemeType.RIGHT_BRACKET) {
                    throw new RuntimeException("Unexpected token: " +
                            lexeme.value +
                            " at position: " +
                            lexemes.getPos());
                }
                return value;
            default:
                throw new RuntimeException("Unexpected token: " +
                        lexeme.value +
                        " at position: " +
                        lexemes.getPos());
        }
    }
}
