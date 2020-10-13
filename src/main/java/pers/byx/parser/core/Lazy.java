package pers.byx.parser.core;

public class Lazy extends Parser
{
    private Parser parser;

    public void set(Parser parser)
    {
        this.parser = parser;
    }

    @Override
    protected ParseResult parse(Sequence input)
    {
        return parser.parse(input);
    }
}
