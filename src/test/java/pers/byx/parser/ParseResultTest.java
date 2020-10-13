package pers.byx.parser;

import org.junit.Test;
import pers.byx.parser.core.CharSequence;
import pers.byx.parser.core.ParseResult;
import pers.byx.parser.core.Sequence;

import static org.junit.Assert.*;

public class ParseResultTest
{
    @Test
    public void testSuccessResult()
    {
        Sequence sequence = new CharSequence("abc");
        ParseResult result = ParseResult.success(sequence);
        assertTrue(result.isSuccess());
        assertEquals(result.remain().toString(), "abc");
    }

    @Test
    public void testFailResult()
    {
        Sequence sequence = new CharSequence("abc");
        ParseResult result = ParseResult.fail(sequence);
        assertFalse(result.isSuccess());
        assertEquals(result.remain().toString(), "abc");
    }
}
