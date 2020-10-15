package pers.byx.parser.core;

import java.util.ArrayList;
import java.util.List;

public class OneOrMore extends Parser
{
    private final Parser p;

    public OneOrMore(Parser p)
    {
        this.p = p;
    }

    @Override
    protected ParseResult parse(Sequence input)
    {
        List<ParseResult> results = new ArrayList<>();
        ParseResult result = p.parse(input);
        if (!result.isSuccess()) return ParseResult.fail(input);
        while (result.isSuccess())
        {
            results.add(result);
            result = p.parse(result.remain());
        }
        Sequence recognized = input.subSequence(0, input.length() - result.remain().length());
        return ParseResult.success(recognized, result.remain(), this, results);
    }
}
