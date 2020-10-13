package pers.byx.parser.core;

public abstract class ParseResult
{
    public abstract boolean isSuccess();
    public abstract Sequence remain();

    public static ParseResult success(Sequence remain)
    {
        return new ParseResult()
        {

            @Override
            public boolean isSuccess()
            {
                return true;
            }

            @Override
            public Sequence remain()
            {
                return remain;
            }
        };
    }

    public static ParseResult fail(Sequence remain)
    {
        return new ParseResult()
        {
            @Override
            public boolean isSuccess()
            {
                return false;
            }

            @Override
            public Sequence remain()
            {
                return remain;
            }
        };
    }
}
