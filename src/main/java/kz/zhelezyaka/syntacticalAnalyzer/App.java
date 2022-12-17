package kz.zhelezyaka.syntacticalAnalyzer;

import java.util.List;

import static kz.zhelezyaka.syntacticalAnalyzer.ExpressionParser.expr;
import static kz.zhelezyaka.syntacticalAnalyzer.Lexeme.lexAnalyzer;

public class App {
    public static void main(String[] args) {
        String expText = "(55 + 5 * (3 - 2)) * 2";
//        String expText = "-(5 + 2) + -5 * 3";
        List<Lexeme> lexemes = lexAnalyzer(expText);
        LexemeBuffer lexemeBuffer = new LexemeBuffer(lexemes);
        System.out.println(expr(lexemeBuffer));
    }
}
