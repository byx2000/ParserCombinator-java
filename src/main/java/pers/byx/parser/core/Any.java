package pers.byx.parser.core;

public class Any extends Parser
{
    @Override
    protected ParseResult parse(Sequence input)
    {
        if (input.length() == 0) return ParseResult.fail(input);
        Sequence recognized = input.subSequence(0, 1);
        Sequence remain = input.subSequence(1, input.length());
        return ParseResult.success(recognized, remain, this);
    }
}
