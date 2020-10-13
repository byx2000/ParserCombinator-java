package pers.byx.parser.core;

import java.util.function.Consumer;

public class ActionOnFail extends Parser
{
    private final Parser p;
    private final Consumer<Sequence> callback;

    public ActionOnFail(Parser p, Consumer<Sequence> callback)
    {
        this.p = p;
        this.callback = callback;
    }

    @Override
    protected ParseResult parse(Sequence input)
    {
        ParseResult result = p.parse(input);
        if (!result.isSuccess()) callback.accept(result.remain());
        return result;
    }
}
