package pers.byx.parser.core;

public class Empty extends Parser
{
    @Override
    protected ParseResult parse(Sequence input)
    {
        return ParseResult.success(input.subSequence(0, 0), input, this);
    }
}
