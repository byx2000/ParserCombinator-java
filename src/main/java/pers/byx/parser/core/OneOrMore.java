package pers.byx.parser.core;

public class OneOrMore extends Parser
{
    private final Parser p;

    public OneOrMore(Parser p)
    {
        this.p = p;
    }

    @Override
    protected ParseResult parse(Sequence input)
    {
        ParseResult result = p.parse(input);
        if (!result.isSuccess()) return result;
        while (result.isSuccess())
        {
            result = p.parse(result.remain());
        }
        return ParseResult.success(result.remain());
    }
}