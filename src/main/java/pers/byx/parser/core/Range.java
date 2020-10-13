package pers.byx.parser.core;

public class Range extends Parser
{
    private final char c1, c2;

    public Range(char c1, char c2)
    {
        if (c1 > c2)
        {
            char t = c1;
            c1 = c2;
            c2 = t;
        }

        this.c1 = c1;
        this.c2 = c2;
    }

    @Override
    protected ParseResult parse(Sequence input)
    {
        if (input.length() == 0) return ParseResult.fail(input);
        char c = input.charAt(0);
        if ((c1 - c) * (c2 - c) > 0) return ParseResult.fail(input);
        return ParseResult.success(input.subSequence(1, input.length()));
    }
}
