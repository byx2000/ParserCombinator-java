package pers.byx.parser.sample;

import pers.byx.parser.core.Lazy;
import pers.byx.parser.core.ParseResult;
import pers.byx.parser.core.Parser;

import java.util.Stack;
import java.util.function.Consumer;

import static pers.byx.parser.core.Parsers.*;

public class Calculator
{
    public static double calculate(String input)
    {
        Stack<Double> stack = new Stack<>();

        Consumer<ParseResult> push = r -> stack.push(Double.parseDouble(r.recognized().toString()));

        Consumer<ParseResult> calc = r ->
        {
            double rhs = stack.pop();
            double lhs = stack.pop();
            switch (r.recognized().charAt(0))
            {
                case '+' -> stack.push(lhs + rhs);
                case '-' -> stack.push(lhs - rhs);
                case '*' -> stack.push(lhs * rhs);
                case '/' -> stack.push(lhs / rhs);
            }
        };

        Parser digit = range('0', '9');
        Parser dot = ch('.');
        Parser add = ch('+');
        Parser sub = ch('-');
        Parser mul = ch('*');
        Parser div = ch('/');
        Parser lp = ch('(');
        Parser rp = ch(')');

        Parser integer = digit.oneOrMore();
        Parser decimal = digit.oneOrMore().concat(dot).concat(digit.oneOrMore());
        Lazy expr = lazy();
        Parser factor = or(decimal.callback(push),
                           integer.callback(push),
                           lp.concat(expr).concat(rp));
        Parser term = factor.concat(mul.or(div).concat(factor).callback(calc).zeroOrMore());
        expr.set(term.concat(add.or(sub).concat(term).callback(calc).zeroOrMore()));
        Parser p = expr.end();

        stack.clear();
        p.parse(input.replace(" ", ""));

        return stack.pop();
    }

    public static void main(String[] args)
    {
        System.out.println(calculate("123")); // 123
        System.out.println(calculate("1+2")); // 3
        System.out.println(calculate("(11+6)*5")); // 85
        System.out.println(calculate("(2+3)*(4+5)")); // 45
        System.out.println(calculate("(6/2)/(3*2)")); // 0.5
    }
}
