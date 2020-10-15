package pers.byx.parser.core;

import java.util.*;

public class ParseResult
{
    private final boolean success;
    private final Sequence recognized;
    private final Sequence remain;
    private final Parser parser;
    private final List<ParseResult> resultChain;

    private ParseResult(boolean success, Sequence recognized, Sequence remain, Parser parser, List<ParseResult> resultChain)
    {
        this.success = success;
        this.recognized = recognized;
        this.remain = remain;
        this.parser = parser;
        this.resultChain = resultChain;
    }

    public boolean isSuccess()
    {
        return success;
    }

    public Sequence recognized()
    {
        return recognized;
    }

    public Sequence remain()
    {
        return remain;
    }

    public Parser getParser()
    {
        return parser;
    }

    public List<ParseResult> getResultChain()
    {
        return resultChain;
    }

    public static ParseResult success(Sequence recognized, Sequence remain, Parser parser, ParseResult... resultChain)
    {
        return new ParseResult(true, recognized, remain, parser, Arrays.asList(resultChain));
    }

    public static ParseResult success(Sequence recognized, Sequence remain, Parser parser, List<ParseResult> resultChain)
    {
        return new ParseResult(true, recognized, remain, parser, resultChain);
    }

    public static ParseResult success(Sequence recognized, Sequence remain, Parser parser)
    {
        return new ParseResult(true, recognized, remain, parser, null);
    }

    public static ParseResult fail(Sequence remain)
    {
        return new ParseResult(false, null, remain, null, null);
    }
}
