package pers.byx.parser.sample;

import pers.byx.parser.core.Lazy;
import pers.byx.parser.core.Parser;

import java.util.Scanner;
import java.util.Stack;

import static pers.byx.parser.core.Parsers.*;

public class Calculator
{
    public static double calc(String input)
    {
        Stack<Double> stack = new Stack<>();

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
        Parser factor = or(integer.onSuccess(s -> stack.push(Double.parseDouble(s.toString()))),
                           decimal.onSuccess(s -> stack.push(Double.parseDouble(s.toString()))),
                           lp.concat(expr).concat(rp));
        Parser term = factor.concat((mul.or(div)).concat(factor).onSuccess(s ->
        {
            double rhs = stack.pop();
            double lhs = stack.pop();
            switch (s.charAt(0))
            {
                case '*' -> stack.push(lhs * rhs);
                case '/' -> stack.push(lhs / rhs);
            }
        }).zeroOrMore());
        expr.set(term.concat((add.or(sub).concat(term).onSuccess(s ->
        {
            double rhs = stack.pop();
            double lhs = stack.pop();
            switch (s.charAt(0))
            {
                case '+' -> stack.push(lhs + rhs);
                case '-' -> stack.push(lhs - rhs);
            }
        }).zeroOrMore())));
        Parser p = expr.end();

        stack.clear();
        p.parse(input.replace(" ", ""));

        return stack.pop();
    }

    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        String line;
        while (!(line = scanner.nextLine()).equals("q"))
        {
            System.out.println(calc(line));
        }
    }
}
