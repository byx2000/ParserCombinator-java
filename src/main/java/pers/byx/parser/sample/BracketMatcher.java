package pers.byx.parser.sample;

import pers.byx.parser.core.*;

import java.util.Scanner;

import static pers.byx.parser.core.Parsers.*;

public class BracketMatcher
{
    public static void main(String[] args)
    {
        Lazy S = new Lazy();
        Parser T = or(ch('(').concat(S).concat(ch(')')),
                      ch('[').concat(S).concat(ch(']')),
                      ch('{').concat(S).concat(ch('}')));
        S.set(T.concat(S).or(empty));
        Parser p = S.end();

        Scanner scanner = new Scanner(System.in);
        String line;
        while (!(line = scanner.nextLine()).equals("q"))
        {
            ParseResult result = p.parse(line);
            if (result.isSuccess())
                System.out.println("valid");
            else
                System.out.println("invalid");
        }
    }
}
