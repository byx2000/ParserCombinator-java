package pers.byx.parser.core;

import java.util.function.Consumer;

public abstract class Parser
{
    public ParseResult parse(String input)
    {
        return parse(new CharSequence(input));
    }

    protected abstract ParseResult parse(Sequence input);

    public Parser concat(Parser p)
    {
        return new Concat(this, p);
    }

    public Parser or(Parser p)
    {
        return new Or(this, p);
    }

    public Parser zeroOrMore()
    {
        return new ZeroOrMore(this);
    }

    public Parser oneOrMore()
    {
        return new OneOrMore(this);
    }

    public Parser end()
    {
        return new Concat(this, new End());
    }

    public Parser any()
    {
        return new Concat(this, new Any());
    }

    public Parser peek(Parser p)
    {
        return new Concat(this, new Peek(p));
    }

    public Parser onSuccess(Consumer<Sequence> callback)
    {
        return new ActionOnSuccess(this, callback);
    }

    public Parser onFail(Consumer<Sequence> callback)
    {
        return new ActionOnFail(this, callback);
    }

    public Parser option(Parser p)
    {
        return new Concat(this, new Option(p));
    }
}
