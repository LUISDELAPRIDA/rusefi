package com.rusefi.test;

import com.fathzer.soft.javaluator.DoubleEvaluator;
import com.fathzer.soft.javaluator.Operator;
import org.junit.Test;

import java.text.ParseException;
import java.util.Stack;

import static org.junit.Assert.assertEquals;

public class ParserTest {
    @Test
    public void testBooleanConversion() throws ParseException {
        assertParseB("2 1 >", "2 > 1");
        assertParseB("1 2 + 3 -", "1 + 2 - 3");
        assertParseB("1 2 | 3 |", "1 | 2 | 3");

        assertParseB("1 2 + 3 4 5 + / +", "1 + 2 + 3 / (4 +5 )");

        assertParse("rpm 0 >", "rpm > false");
        assertParse("rpm 0 >", "(rpm > false)");
        assertParseB("rpm user0 > clt user2 > or", "(rpm > user0) or (clt > user2)");
        assertParseB("1 2 | 3 |", "1 | 2 | 3");
        assertParseB("rpm user0 > clt user2 > or vbatt user1 > or", "(rpm > user0) or (clt > user2) or (vbatt > user1)");
    }

    private void assertParseB(String rpn, String expression) {
        assertParse(rpn, expression);
        String h = getInfix(rpn);
        System.out.println(h);

        assertEquals(getReplace(expression).toLowerCase(), getReplace(h).toLowerCase());
    }

    private String getReplace(String h) {
        return h.replaceAll("[\\(\\)]", "").replace(" ", "");
    }

    private void assertParse(String rpn, String expression) {
        DoubleEvaluator evaluator = new DoubleEvaluator();
        evaluator.evaluate(expression.toLowerCase());

        assertEquals(rpn, evaluator.getRusEfi());
    }

    private String getInfix(String rpn) {
        String tokens[] = rpn.split(" ");
        Stack<String> stack = new Stack<>();

        for (String token : tokens) {
            if (Operator._1_OPERATORS.contains(token)) {
                String a = stack.pop();

                stack.push("(" + token + " " + a + ")");

            } else if (takesTwoParameters(token)) {
                if (stack.size() < 2)
                    throw new IllegalStateException("Not enough " + token);
                String a = stack.pop();
                String b = stack.pop();
                stack.push("(" + b + " " + token + " " + a + ")");
            } else {
                stack.push(token);
            }
        }
        if (stack.size() != 1)
            throw new IllegalStateException("Unexpected stack content: " + stack);
        return stack.pop();
    }

    private boolean takesTwoParameters(String token) {
        return Operator._2_OPERATORS.contains(token);
    }


    @Test
    public void testUnaryMinus() {
        /**
         * do we even need to and/or should to support unary minus?
         *
         * http://stackoverflow.com/questions/20246787/handling-unary-minus-for-shunting-yard-algorithm
          */
        assertValue("3 negate", "negate3", -3);
        assertValue("2 3 6 ^ negate +", "2+negate 3^6", -727.0);
    }

    @Test
    public void testBooleanNot2() throws Exception {
        assertValue("0 !", "! 0", 1);
        assertValue("0 not", "not 0", 1);
        assertValue("1 not", "not(1)", 0);
        assertValue("0 not", "not(0)", 1);
//        assertValue("1 0 | not 0 & 1 0 | |", "(((true | false) & not(false)) | (true | false))", 1);
    }

    private void assertValue(String expectedRpn, String expression, double expectedValue) {
        DoubleEvaluator evaluator = new DoubleEvaluator();
        assertEquals(expectedValue, evaluator.evaluate(expression), 0.001);
        assertParseB(expectedRpn, expression);
    }

    @Test
    public void testRusEfi() {
        assertParseB("1 4 or", "1 or 4");
        assertParseB("1 4 or", "1 OR 4");

        assertParseB("time_since_boot 4 <", "(time_since_boot < 4)");
        assertParseB("1 4 |", "1 | 4");
        assertParseB("time_since_boot 4 < rpm 0 > |", "(time_since_boot < 4) | (rpm > 0)");

        assertParseB("1 4 and", "1 and 4");
        assertParseB("1 4 and", "1 AND 4");

        assertParseB("1 4 &", "1 & 4");

        assertParseB("coolant fan_off_setting >", "(coolant > fan_off_setting)");

        assertParseB("1 coolant fan_on_setting > or", "1 OR (coolant > fan_on_setting)");
        assertParseB("fan coolant fan_off_setting > and coolant fan_on_setting > or", "(fan and (coolant > fan_off_setting)) OR (coolant > fan_on_setting)");

        assertParseB("time_since_boot 4 <= rpm 0 > |", "(time_since_boot <= 4) | (rpm > 0)");

        assertParseB("time_since_boot 4 <= rpm 0 > |", "(time_since_boot <= 4) | (rpm > 0)");

        assertParseB("time_since_boot 4 <= rpm 0 > or", "(time_since_boot <= 4) OR (rpm > 0)");

        assertParseB("self rpm 4800 >= and rpm 5000 > or", "(self and (rpm >= 4800)) OR (rpm > 5000)");
    }
}