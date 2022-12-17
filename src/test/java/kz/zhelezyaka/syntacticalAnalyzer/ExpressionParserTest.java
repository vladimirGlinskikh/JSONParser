package kz.zhelezyaka.syntacticalAnalyzer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static kz.zhelezyaka.syntacticalAnalyzer.ExpressionParser.expr;
import static kz.zhelezyaka.syntacticalAnalyzer.Lexeme.lexAnalyzer;

class ExpressionParserTest {
    private static Object expect(String expected) {
        List<Lexeme> lexemes = lexAnalyzer(expected);
        LexemeBuffer lexemeBuffer = new LexemeBuffer(lexemes);
        return expr(lexemeBuffer);
    }

    @Test
    public void expressionTest() {
        Assertions.assertEquals(expect("(55 + 5 * (3 - 2)) * 2"), 120);
        Assertions.assertEquals(expect("(-55 + -5 * (3 - 2)) * -2"), 120);
        Assertions.assertEquals(expect("((-55 + -5) * 3 - 2) * 2"), -364);
    }
}