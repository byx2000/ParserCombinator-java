package pers.byx.parser.core;

import java.util.function.Consumer;

public class ActionOnSuccess extends Parser
{
    private final Parser p;
    private final Consumer<Sequence> callback;

    public ActionOnSuccess(Parser p, Consumer<Sequence> callback)
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
            callback.accept(input.subSequence(0, input.length() - result.remain().length()));
        }
        return result;
    }
}
