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
        if (result.isSuccess()) return result;
        return ParseResult.success(input);
    }
}
