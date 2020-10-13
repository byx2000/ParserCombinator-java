package pers.byx.parser.core;

public class Char extends Parser
{
    private final char ch;

    public Char(char ch)
    {
        this.ch = ch;
    }

    @Override
    protected ParseResult parse(Sequence input)
    {
        if (input.length() <= 0) return ParseResult.fail(input);
        if (input.charAt(0) != ch) return ParseResult.fail(input);
        return ParseResult.success(input.subSequence(1, input.length()));
    }
}
