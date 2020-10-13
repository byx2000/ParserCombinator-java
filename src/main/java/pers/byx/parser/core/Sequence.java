package pers.byx.parser.core;

public interface Sequence
{
    char charAt(int index);
    int length();
    Sequence subSequence(int begin, int end);
}
