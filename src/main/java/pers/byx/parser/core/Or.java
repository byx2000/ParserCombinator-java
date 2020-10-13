package pers.byx.parser.core;

public class Or extends Parser
{
    private final Parser p1, p2;

    public Or(Parser p1, Parser p2)
    {
        this.p1 = p1;
        this.p2 = p2;
    }

    @Override
    protected ParseResult parse(Sequence input)
    {
        ParseResult result = p1.parse(input);
        if (result.isSuccess()) return result;
        result = p2.parse(input);
        if (result.isSuccess()) return result;
        return ParseResult.fail(input);
    }
}
