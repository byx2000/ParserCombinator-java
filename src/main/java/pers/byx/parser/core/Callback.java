package pers.byx.parser.core;

import java.util.function.Consumer;

public class Callback extends Parser
{
    private final Parser p;
    private final Consumer<ParseResult> callback;

    public Callback(Parser p, Consumer<ParseResult> callback)
    {
        this.p = p;
        this.callback = callback;
    }

    @Override
    protected ParseResult parse(Sequence input)
    {
        ParseResult result = p.parse(input);
        if (result.isSuccess())
        {
            return ParseResult.success(result.recognized(), result.remain(), this, result);
        }
        return ParseResult.fail(input);
    }

    public void invoke(ParseResult result)
    {
        if (callback != null)
        {
            callback.accept(result);
        }
    }
}
