package pers.byx.parser.core;

public class Option extends Parser
{
    private final Parser p;

    public Option(Parser p)
    {
        this.p = p;
    }

    @Override
    protected ParseResult parse(Sequence input)
    {
        ParseResult result = p.parse(input);
        if (result.isSuccess())
            return ParseResult.success(result.recognized(), result.remain(), this, result);
        return ParseResult.success(input.subSequence(0, 0), input, this);
    }
}
