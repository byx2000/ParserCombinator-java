package pers.byx.parser.core;

public class Peek extends Parser
{
    private final Parser p;

    public Peek(Parser p)
    {
        this.p = p;
    }

    @Override
    protected ParseResult parse(Sequence input)
    {
        ParseResult result = p.parse(input);
        if (!result.isSuccess()) return ParseResult.fail(input);
        return ParseResult.success(input.subSequence(0, 0), input, this);
    }
}
