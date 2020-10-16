package pers.byx.parser.sample;

import pers.byx.parser.core.*;

import java.util.Scanner;

import static pers.byx.parser.core.Parsers.*;

public class BracketMatcher
{
    public static boolean match(String input)
    {
        Lazy S = new Lazy();
        Parser T = or(ch('(').concat(S).concat(ch(')')),
                      ch('[').concat(S).concat(ch(']')),
                      ch('{').concat(S).concat(ch('}')));
        S.set(T.concat(S).or(empty));
        Parser p = S.end();

        return p.parse(input).isSuccess();
    }

    public static void main(String[] args)
    {
        System.out.println(match("")); // true
        System.out.println(match("()")); // true
        System.out.println(match("[]")); // true
        System.out.println(match("{}")); // true
        System.out.println(match("()[]{}")); // true
        System.out.println(match("{()}[]()")); // true
        System.out.println(match("[")); // false
        System.out.println(match("(]{}[}")); // false
        System.out.println(match("{([))}")); // false
    }
}
