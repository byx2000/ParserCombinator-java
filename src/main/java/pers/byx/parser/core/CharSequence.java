package pers.byx.parser.core;

public class CharSequence implements Sequence
{
    private final String string;
    private final int begin, end;

    public CharSequence(String string)
    {
        this(string, 0, string.length());
    }

    public CharSequence(String string, int begin, int end)
    {
        this.string = string;
        this.begin = begin;
        this.end = end;
    }

    @Override
    public char charAt(int index)
    {
        return string.charAt(begin + index);
    }

    @Override
    public int length()
    {
        return end - begin;
    }

    @Override
    public Sequence subSequence(int begin, int end)
    {
        return new CharSequence(string, this.begin + begin, this.begin + end);
    }

    @Override
    public String toString()
    {
        return string.substring(begin, end);
    }
}
