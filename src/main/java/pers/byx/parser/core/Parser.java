package pers.byx.parser.core;

import java.util.*;
import java.util.function.Consumer;

public abstract class Parser
{
    private HashMap<String, Object> attributes;

    public void setAttribute(String key, Object value)
    {
        if (attributes == null) attributes = new HashMap<>();
        attributes.put(key, value);
    }

    public boolean hasAttrbute(String key)
    {
        if (attributes == null) return false;
        return attributes.containsKey(key);
    }

    public Object getAttribute(String key)
    {
        if (attributes == null) return null;
        return attributes.get(key);
    }

    public Object removeAttribute(String key)
    {
        if (attributes == null) return null;
        return attributes.remove(key);
    }

    public ParseResult parse(String input)
    {
        ParseResult result = parse(new CharSequence(input));
        if (result.isSuccess())
        {
            invokeCallback(result);
        }
        return result;
    }

    private void invokeCallback(ParseResult result)
    {
        List<ParseResult> resultChain = result.getResultChain();
        if (resultChain != null)
        {
            for (ParseResult r : resultChain)
            {
                invokeCallback(r);
            }
        }

        Parser parser = result.getParser();
        if (parser instanceof Callback)
        {
            ((Callback)parser).invoke(result);
        }
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

    public Parser callback(Consumer<ParseResult> callback)
    {
        return new Callback(this, callback);
    }

    public Parser option(Parser p)
    {
        return new Concat(this, new Option(p));
    }
}
