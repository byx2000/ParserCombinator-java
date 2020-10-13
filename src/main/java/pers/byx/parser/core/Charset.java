package pers.byx.parser.core;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Charset extends Parser
{
    private final Set<Character> charset = new HashSet<>();

    public Charset(Character... chs)
    {
        charset.addAll(Arrays.asList(chs));
    }

    @Override
    protected ParseResult parse(Sequence input)
    {
        if (input.length() <= 0) return ParseResult.fail(input);
        if (!charset.contains(input.charAt(0))) return ParseResult.fail(input);
        return ParseResult.success(input.subSequence(1, input.length()));
    }
}
