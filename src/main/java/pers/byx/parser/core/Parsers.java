package pers.byx.parser.core;

public class Parsers
{
    public static Parser ch(char c)
    {
        return new Char(c);
    }

    public static Parser range(char c1, char c2)
    {
        return new Range(c1, c2);
    }

    public static Parser option(Parser p)
    {
        return new Option(p);
    }

    public static Lazy lazy()
    {
        return new Lazy();
    }

    public static Parser literal(String str)
    {
        return new Literal(str);
    }

    public static Parser charset(Character... chs)
    {
        return new Charset(chs);
    }

    public static Parser or(Parser p1, Parser p2, Parser... others)
    {
        Parser ret = p1.or(p2);
        for (Parser p : others)
        {
            ret = ret.or(p);
        }
        return ret;
    }

    public static Parser concat(Parser p1, Parser p2, Parser... others)
    {
        Parser ret = p1.concat(p2);
        for (Parser p : others)
        {
            ret = ret.concat(p);
        }
        return ret;
    }

    public static final Parser empty = new Empty();
}
