package pers.byx.parser;

import org.junit.Test;
import pers.byx.parser.core.Char;
import pers.byx.parser.core.CharSequence;
import pers.byx.parser.core.ParseResult;

import static org.junit.Assert.*;

public class ParseResultTest
{
    @Test
    public void testSuccessResult()
    {
        ParseResult result = ParseResult.success(new CharSequence("abc"), new CharSequence("de"), new Char('a'));
        assertTrue(result.isSuccess());
        assertEquals("abc", result.recognized().toString());
        assertEquals("de", result.remain().toString());
    }

    @Test
    public void testFailResult()
    {
        ParseResult result = ParseResult.fail(new CharSequence("abc"));
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("abc", result.remain().toString());
    }
}
