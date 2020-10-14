package pers.byx.parser.core;

public class ParseResult
{
    private final boolean success;
    private final Sequence remain;

    private ParseResult(boolean success, Sequence remain)
    {
        this.success = success;
        this.remain = remain;
    }

    public boolean isSuccess()
    {
        return success;
    }

    public Sequence remain()
    {
        return remain;
    }

    public static ParseResult success(Sequence remain)
    {
        return new ParseResult(true, remain);
    }

    public static ParseResult fail(Sequence remain)
    {
        return new ParseResult(false, remain);
    }
}
