package pers.byx.parser.core;

public class Concat extends Parser
{
    private final Parser p1, p2;

    public Concat(Parser p1, Parser p2)
    {
        this.p1 = p1;
        this.p2 = p2;
    }

    @Override
    protected ParseResult parse(Sequence input)
    {
        ParseResult result = p1.parse(input);
        if (!result.isSuccess()) return ParseResult.fail(input);
        result = p2.parse(result.remain());
        if (!result.isSuccess()) return ParseResult.fail(input);
        return result;
    }
}
