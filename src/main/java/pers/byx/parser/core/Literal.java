package pers.byx.parser.core;

public class Literal extends Parser
{
    private final String string;

    public Literal(String string)
    {
        this.string = string;
    }

    @Override
    protected ParseResult parse(Sequence input)
    {
        if (input.length() < string.length()) return ParseResult.fail(input);
        for (int i = 0; i < string.length(); ++i)
        {
            if (string.charAt(i) != input.charAt(i)) return ParseResult.fail(input);
        }
        return ParseResult.success(input.subSequence(string.length(), input.length()));
    }
}
