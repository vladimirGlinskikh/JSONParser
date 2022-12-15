package kz.zhelezyaka.syntacticalAnalyzer;

import java.util.List;

import static kz.zhelezyaka.syntacticalAnalyzer.Lexeme.lexAnalyzer;

public class ExpressionParser {
    public static void main(String[] args) {
        String expText = "(55 + 5 * (3 - 2)) * 2";
//        String expText = "-(5 + 2) + -5 * 3";
        List<Lexeme> lexemes = lexAnalyzer(expText);
        LexemeBuffer lexemeBuffer = new LexemeBuffer(lexemes);
        System.out.println(expr(lexemeBuffer));
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
                case PLUS -> value += multdiv(lexemes);
                case MINUS -> value -= multdiv(lexemes);
                default -> {
                    lexemes.back();
                    return value;
                }
            }
        }
    }

    public static int multdiv(LexemeBuffer lexemes) {
        int value = factor(lexemes);
        while (true) {
            Lexeme lexeme = lexemes.next();
            switch (lexeme.type) {
                case MULTIPLE -> value *= factor(lexemes);
                case DIVIDE -> value /= factor(lexemes);
                default -> {
                    lexemes.back();
                    return value;
                }
            }
        }
    }

    public static int factor(LexemeBuffer lexemes) {
        Lexeme lexeme = lexemes.next();
        switch (lexeme.type) {
            case MINUS:
                int value = factor(lexemes);
                return -value;
            case NUMBER:
                return Integer.parseInt(lexeme.value);
            case LEFT_BRACKET:
                value = expr(lexemes);
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
