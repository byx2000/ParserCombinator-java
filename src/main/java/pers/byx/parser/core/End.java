package pers.byx.parser.core;

public class End extends Parser
{
    @Override
    protected ParseResult parse(Sequence input)
    {
        if (input.length() > 0) return ParseResult.fail(input);
        return ParseResult.success(input.subSequence(0, 0), input, this);
    }
}
