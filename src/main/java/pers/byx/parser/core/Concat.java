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
        ParseResult result1 = p1.parse(input);
        if (!result1.isSuccess()) return ParseResult.fail(input);
        ParseResult result2 = p2.parse(result1.remain());
        if (!result2.isSuccess()) return ParseResult.fail(input);
        Sequence recognized = input.subSequence(0, input.length() - result2.remain().length());
        return ParseResult.success(recognized, result2.remain(), this, result1, result2);
    }
}
