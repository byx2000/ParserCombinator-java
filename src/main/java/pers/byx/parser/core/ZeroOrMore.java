package pers.byx.parser.core;

public class ZeroOrMore extends Parser
{
    private final Parser p;

    public ZeroOrMore(Parser p)
    {
        this.p = p;
    }

    @Override
    protected ParseResult parse(Sequence input)
    {
        ParseResult result = p.parse(input);
        while (result.isSuccess())
        {
            result = p.parse(result.remain());
        }
        return ParseResult.success(result.remain());
    }
}
