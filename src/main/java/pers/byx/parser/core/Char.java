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
        Sequence recognized = input.subSequence(0, 1);
        Sequence remain = input.subSequence(1, input.length());
        return ParseResult.success(recognized, remain, this);
    }
}
