package pers.byx.parser;

import org.junit.Test;
import pers.byx.parser.core.*;
import static pers.byx.parser.core.Parsers.*;

import java.util.*;
import java.util.function.Consumer;

import static org.junit.Assert.*;

public class ParserTest
{
    @Test
    public void testEmpty()
    {
        Parser p = new Empty();
        ParseResult result = p.parse("");
        assertTrue(result.isSuccess());
        assertEquals("", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("abc");
        assertTrue(result.isSuccess());
        assertEquals("", result.recognized().toString());
        assertEquals("abc", result.remain().toString());
    }

    @Test
    public void testAny()
    {
        Parser p = new Any();
        ParseResult result = p.parse("");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("", result.remain().toString());

        result = p.parse("a");
        assertTrue(result.isSuccess());
        assertEquals("a", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("sdfsgsa");
        assertTrue(result.isSuccess());
        assertEquals("s", result.recognized().toString());
        assertEquals("dfsgsa", result.remain().toString());
    }

    @Test
    public void testChar()
    {
        Parser p = new Char('a');
        ParseResult result = p.parse("a");
        assertTrue(result.isSuccess());
        assertEquals("a", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("b");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("b", result.remain().toString());

        result = p.parse("ab");
        assertTrue(result.isSuccess());
        assertEquals("a", result.recognized().toString());
        assertEquals("b", result.remain().toString());

        result = p.parse("bc");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("bc", result.remain().toString());

        result = p.parse("");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("", result.remain().toString());
    }

    @Test
    public void testEnd()
    {
        Parser p = new End();
        ParseResult result = p.parse("");
        assertTrue(result.isSuccess());
        assertEquals("", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("a");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("a", result.remain().toString());

        result = p.parse("ab");
        assertFalse(result.isSuccess());
        assertEquals("ab", result.remain().toString());

        result = p.parse(" ");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals(" ", result.remain().toString());
    }

    @Test
    public void testConcat()
    {
        Parser p1 = new Char('a');
        Parser p2 = new Char('b');
        Parser p = new Concat(p1, p2);

        ParseResult result = p.parse("");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("", result.remain().toString());

        result = p.parse("a");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("a", result.remain().toString());

        result = p.parse("b");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("b", result.remain().toString());

        result = p.parse("ab");
        assertTrue(result.isSuccess());
        assertEquals("ab", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("bc");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("bc", result.remain().toString());

        result = p.parse("ac");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("ac", result.remain().toString());

        result = p.parse("abc");
        assertTrue(result.isSuccess());
        assertEquals("ab", result.recognized().toString());
        assertEquals("c", result.remain().toString());
    }

    @Test
    public void testOr()
    {
        Parser p1 = new Char('a');
        Parser p2 = new Char('b');
        Parser p = new Or(p1, p2);

        ParseResult result = p.parse("");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("", result.remain().toString());

        result = p.parse("a");
        assertTrue(result.isSuccess());
        assertEquals("a", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("b");
        assertTrue(result.isSuccess());
        assertEquals("b", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("x");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("x", result.remain().toString());

        result = p.parse("am");
        assertTrue(result.isSuccess());
        assertEquals("a", result.recognized().toString());
        assertEquals("m", result.remain().toString());

        result = p.parse("bc");
        assertTrue(result.isSuccess());
        assertEquals("b", result.recognized().toString());
        assertEquals("c", result.remain().toString());

        result = p.parse("xy");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("xy", result.remain().toString());
    }

    @Test
    public void testZeroOrMore()
    {
        Parser p = new ZeroOrMore(new Char('a'));

        ParseResult result = p.parse("");
        assertTrue(result.isSuccess());
        assertEquals("", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("a");
        assertTrue(result.isSuccess());
        assertEquals("a", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("b");
        assertTrue(result.isSuccess());
        assertEquals("", result.recognized().toString());
        assertEquals("b", result.remain().toString());

        result = p.parse("aaaaa");
        assertTrue(result.isSuccess());
        assertEquals("aaaaa", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("aaaaabbb");
        assertTrue(result.isSuccess());
        assertEquals("aaaaa", result.recognized().toString());
        assertEquals("bbb", result.remain().toString());

        result = p.parse("bbb");
        assertTrue(result.isSuccess());
        assertEquals("", result.recognized().toString());
        assertEquals("bbb", result.remain().toString());
    }

    @Test
    public void testOneOrMore()
    {
        Parser p = new OneOrMore(new Char('a'));

        ParseResult result = p.parse("");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("", result.remain().toString());

        result = p.parse("a");
        assertTrue(result.isSuccess());
        assertEquals("a", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("b");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("b", result.remain().toString());

        result = p.parse("aaaaa");
        assertTrue(result.isSuccess());
        assertEquals("aaaaa", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("aaaaabbb");
        assertTrue(result.isSuccess());
        assertEquals("aaaaa", result.recognized().toString());
        assertEquals("bbb", result.remain().toString());

        result = p.parse("bbb");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("bbb", result.remain().toString());
    }

    @Test
    public void testLazy()
    {
        Lazy p = new Lazy();
        p.set(new Char('a'));
        ParseResult result = p.parse("abc");
        assertTrue(result.isSuccess());
        assertEquals("a", result.recognized().toString());
        assertEquals("bc", result.remain().toString());

        result = p.parse("xyz");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("xyz", result.remain().toString());

        final boolean[] flag = {false};
        Parser p1 = p.callback(r ->
                flag[0] = true);
        p1.parse("a");
        assertTrue(flag[0]);
    }

    @Test
    public void testCharset()
    {
        Parser p = new Charset('a', 'b', 'c');
        ParseResult result = p.parse("");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("", result.remain().toString());

        result = p.parse("a");
        assertTrue(result.isSuccess());
        assertEquals("a", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("d");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("d", result.remain().toString());

        result = p.parse("apple");
        assertTrue(result.isSuccess());
        assertEquals("a", result.recognized().toString());
        assertEquals("pple", result.remain().toString());

        result = p.parse("banana");
        assertTrue(result.isSuccess());
        assertEquals("b", result.recognized().toString());
        assertEquals("anana", result.remain().toString());

        result = p.parse("cat");
        assertTrue(result.isSuccess());
        assertEquals("c", result.recognized().toString());
        assertEquals("at", result.remain().toString());

        result = p.parse("dress");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("dress", result.remain().toString());
    }

    @Test
    public void testRange()
    {
        Parser p = new Range('u', 'w');
        ParseResult result = p.parse("");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("", result.remain().toString());

        result = p.parse("u");
        assertTrue(result.isSuccess());
        assertEquals("u", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("w");
        assertTrue(result.isSuccess());
        assertEquals("w", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("t");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("t", result.remain().toString());

        result = p.parse("x");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("x", result.remain().toString());

        p = new Range('w', 'u');

        result = p.parse("");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("", result.remain().toString());

        result = p.parse("u");
        assertTrue(result.isSuccess());
        assertEquals("u", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("w");
        assertTrue(result.isSuccess());
        assertEquals("w", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("t");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("t", result.remain().toString());

        result = p.parse("x");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("x", result.remain().toString());
    }

    @Test
    public void testLiteral()
    {
        Parser p = new Literal("double");
        ParseResult result = p.parse("");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("", result.remain().toString());

        result = p.parse("double x = 3.14;");
        assertTrue(result.isSuccess());
        assertEquals("double", result.recognized().toString());
        assertEquals(" x = 3.14;", result.remain().toString());

        result = p.parse("int i = 100;");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("int i = 100;", result.remain().toString());

        result = p.parse("abc");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("abc", result.remain().toString());

        result = p.parse("doub");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("doub", result.remain().toString());

        result = p.parse("doublr x = 3.14");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("doublr x = 3.14", result.remain().toString());
    }

    @Test
    public void testPeek()
    {
        Parser p = new Peek(new Char('a'));
        ParseResult result;

        result = p.parse("");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("", result.remain().toString());

        result = p.parse("b");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("b", result.remain().toString());

        result = p.parse("a");
        assertTrue(result.isSuccess());
        assertEquals("", result.recognized().toString());
        assertEquals("a", result.remain().toString());

        result = p.parse("abc");
        assertTrue(result.isSuccess());
        assertEquals("", result.recognized().toString());
        assertEquals("abc", result.remain().toString());

        result = p.parse("bcd");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("bcd", result.remain().toString());
    }

    @Test
    public void testOption()
    {
        Parser p = new Option(new Char('+').or(new Char('-'))).concat(new Char('a'));

        ParseResult result = p.parse("+a");
        assertTrue(result.isSuccess());
        assertEquals("+a", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("-a");
        assertTrue(result.isSuccess());
        assertEquals("-a", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("a");
        assertTrue(result.isSuccess());
        assertEquals("a", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("*a");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("*a", result.remain().toString());

        result = p.parse("+b");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("+b", result.remain().toString());
    }

    @Test
    public void testCallback()
    {
        final boolean[] f1 = {false};
        final boolean[] f2 = {false};

        Parser a = ch('a'), b = ch('b'), c = ch('c');
        Parser p = a.callback(r -> f1[0] = true).concat(ch('b')).callback(r -> f2[0] = true).end();
        p.parse("ab");
        assertTrue(f1[0]);
        assertTrue(f2[0]);

        f1[0] = false;
        f2[0] = false;
        p.parse("ac");
        assertFalse(f1[0]);
        assertFalse(f2[0]);

        p.parse("bd");
        assertFalse(f1[0]);
        assertFalse(f2[0]);

        f1[0] = true;
        f2[0] = true;
        p = ch('a').callback(r -> f1[0] = false).or(ch('b').callback(r -> f2[0] = false));
        p.parse("a");
        assertFalse(f1[0]);
        assertTrue(f2[0]);

        f1[0] = true;
        p.parse("b");
        assertTrue(f1[0]);
        assertFalse(f2[0]);

        final boolean[] flag = {false, false, false, false};
        final int[] cnt = new int[10];
        p = a.callback(r ->
        {
            cnt[0]++;
            assertEquals("a", r.recognized().toString());
            flag[0] = true;
        }).concat(b.callback(r ->
        {
            assertEquals("b", r.recognized().toString());
            flag[1] = true;
        })).or(a.callback(r ->
        {
            cnt[0]++;
            assertEquals("a", r.recognized().toString());
            flag[2] = true;
        }).concat(c.callback(r ->
        {
            assertEquals("c", r.recognized().toString());
            flag[3] = true;
        }))).end();

        cnt[0] = 0;
        p.parse("ab");
        assertTrue(flag[0]);
        assertTrue(flag[1]);
        assertFalse(flag[2]);
        assertFalse(flag[3]);
        assertEquals(1, cnt[0]);

        cnt[0] = 0;
        flag[0] = flag[1] = false;
        p.parse("ac");
        assertFalse(flag[0]);
        assertFalse(flag[1]);
        assertTrue(flag[2]);
        assertTrue(flag[3]);
        assertEquals(1, cnt[0]);

        cnt[0] = 0;
        flag[2] = flag[3] = false;
        p.parse("ad");
        assertFalse(flag[0]);
        assertFalse(flag[1]);
        assertFalse(flag[2]);
        assertFalse(flag[3]);
        assertEquals(0, cnt[0]);

        cnt[0] = 0;
        p.parse("db");
        assertFalse(flag[0]);
        assertFalse(flag[1]);
        assertFalse(flag[2]);
        assertFalse(flag[3]);
        assertEquals(0, cnt[0]);

        cnt[0] = 0;
        p.parse("abc");
        assertFalse(flag[0]);
        assertFalse(flag[1]);
        assertFalse(flag[2]);
        assertFalse(flag[3]);
        assertEquals(0, cnt[0]);

        p = a.callback(r -> cnt[0]++).oneOrMore().concat(b.callback(r -> cnt[1]++).zeroOrMore()).end();

        cnt[0] = cnt[1] = 0;
        p.parse("aaaaabbb");
        assertEquals(5, cnt[0]);
        assertEquals(3, cnt[1]);

        cnt[0] = cnt[1] = 0;
        p.parse("aaa");
        assertEquals(3, cnt[0]);
        assertEquals(0, cnt[1]);

        cnt[0] = cnt[1] = 0;
        p.parse("bbbbb");
        assertEquals(0, cnt[0]);
        assertEquals(0, cnt[1]);

        p = a.callback(r -> flag[0] = true).concat(b).or(a.callback(r -> flag[1] = true)).end();

        flag[0] = flag[1] = false;
        p.parse("ab");
        assertTrue(flag[0]);
        assertFalse(flag[1]);

        flag[0] = false;
        p.parse("a");
        assertFalse(flag[0]);
        assertTrue(flag[1]);

        flag[1] = false;
        p.parse("ad");
        assertFalse(flag[0]);
        assertFalse(flag[1]);

        p.parse("dx");
        assertFalse(flag[0]);
        assertFalse(flag[1]);

        p.parse("abc");
        assertFalse(flag[0]);
        assertFalse(flag[1]);

        p = a.callback(r -> flag[0] = true).end().or(a.callback(r -> flag[1] = true).concat(b).end());

        flag[0] = flag[1] = false;
        p.parse("ab");
        assertFalse(flag[0]);
        assertTrue(flag[1]);

        flag[1] = false;
        p.parse("a");
        assertTrue(flag[0]);
        assertFalse(flag[1]);

        flag[0] = false;
        p.parse("ac");
        assertFalse(flag[0]);
        assertFalse(flag[1]);

        p.parse("xy");
        assertFalse(flag[0]);
        assertFalse(flag[1]);

        p.parse("abc");
        assertFalse(flag[0]);
        assertFalse(flag[1]);
    }

    @Test
    public void testTraceback()
    {
        Parser a = ch('a'), b = ch('b'), c = ch('c'), d = ch('d'), e = ch('e');
        Parser p = a.concat(b).concat(c).or(a.concat(b).concat(d));

        ParseResult result = p.parse("abc");
        assertTrue(result.isSuccess());
        assertEquals("abc", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("abd");
        assertTrue(result.isSuccess());
        assertEquals("abd", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("abc123");
        assertTrue(result.isSuccess());
        assertEquals("abc", result.recognized().toString());
        assertEquals("123", result.remain().toString());

        result = p.parse("abd123");
        assertTrue(result.isSuccess());
        assertEquals("abd", result.recognized().toString());
        assertEquals("123", result.remain().toString());

        result = p.parse("abx");
        assertFalse(result.isSuccess());
        assertEquals(result.remain().toString(), "abx");

        result = p.parse("axy");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("axy", result.remain().toString());

        p = a.concat(b.zeroOrMore()).peek(d).or(a.concat(c.zeroOrMore()).peek(e));

        result = p.parse("abbbbbd");
        assertTrue(result.isSuccess());
        assertEquals("abbbbb", result.recognized().toString());
        assertEquals("d", result.remain().toString());

        result = p.parse("ad");
        assertTrue(result.isSuccess());
        assertEquals("a", result.recognized().toString());
        assertEquals("d", result.remain().toString());

        result = p.parse("accce");
        assertTrue(result.isSuccess());
        assertEquals("accc", result.recognized().toString());
        assertEquals("e", result.remain().toString());

        result = p.parse("ae");
        assertTrue(result.isSuccess());
        assertEquals("a", result.recognized().toString());
        assertEquals("e", result.remain().toString());

        result = p.parse("abbbbbe");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("abbbbbe", result.remain().toString());

        result = p.parse("acccd");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("acccd", result.remain().toString());

        p = a.end().or(a.concat(b).end());

        result = p.parse("ab");
        assertTrue(result.isSuccess());
        assertEquals("ab", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("a");
        assertTrue(result.isSuccess());
        assertEquals("a", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("ac");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("ac", result.remain().toString());

        result = p.parse("bd");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("bd", result.remain().toString());

        result = p.parse("abc");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("abc", result.remain().toString());
    }

    @Test
    public void testRecursive()
    {
        // S -> a S b
        //   |  a b
        Parser a = ch('a');
        Parser b = ch('b');
        Lazy s = new Lazy();
        s.set(a.concat(s).concat(b).or(a.concat(b)));
        Parser p = s.end();

        ParseResult result = p.parse("ab");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("aabb");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("aaaaabbbbb");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("");
        assertFalse(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("a");
        assertFalse(result.isSuccess());
        assertEquals("a", result.remain().toString());

        result = p.parse("b");
        assertFalse(result.isSuccess());
        assertEquals("b", result.remain().toString());

        result = p.parse("aab");
        assertFalse(result.isSuccess());
        assertEquals("aab", result.remain().toString());

        result = p.parse("abb");
        assertFalse(result.isSuccess());
        assertEquals("abb", result.remain().toString());

        result = p.parse("aaaaabbbb");
        assertFalse(result.isSuccess());
        assertEquals("aaaaabbbb", result.remain().toString());

        result = p.parse("aaaabbbbb");
        assertFalse(result.isSuccess());
        assertEquals("aaaabbbbb", result.remain().toString());

        // S -> a S
        //   |  ε
        s.set(a.concat(s).or(empty));
        p = s.end();

        result = p.parse("");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("a");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("aaaaa");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("b");
        assertFalse(result.isSuccess());
        assertEquals("b", result.remain().toString());

        result = p.parse("bbbbb");
        assertFalse(result.isSuccess());
        assertEquals("bbbbb", result.remain().toString());

        result = p.parse("aaaab");
        assertFalse(result.isSuccess());
        assertEquals("aaaab", result.remain().toString());

        result = p.parse("baaa");
        assertFalse(result.isSuccess());
        assertEquals("baaa", result.remain().toString());
    }

    @Test
    public void testIntegerParsing()
    {
        Parser digit = range('0', '9');
        Parser sign = ch('+').or(ch('-'));
        Parser integer = digit.oneOrMore().or(sign.concat(digit.oneOrMore())).end();

        ParseResult result = integer.parse("1");
        assertTrue(result.isSuccess());
        assertEquals(result.remain().toString(), "");

        result = integer.parse("+5");
        assertTrue(result.isSuccess());
        assertEquals(result.remain().toString(), "");

        result = integer.parse("-7");
        assertTrue(result.isSuccess());
        assertEquals(result.remain().toString(), "");

        result = integer.parse("123");
        assertTrue(result.isSuccess());
        assertEquals(result.remain().toString(), "");

        result = integer.parse("-995014");
        assertTrue(result.isSuccess());
        assertEquals(result.remain().toString(), "");

        result = integer.parse("");
        assertFalse(result.isSuccess());
        assertEquals(result.remain().toString(), "");

        result = integer.parse("abc");
        assertFalse(result.isSuccess());
        assertEquals(result.remain().toString(), "abc");

        result = integer.parse("3.14");
        assertFalse(result.isSuccess());
        assertEquals(result.remain().toString(), "3.14");

        result = integer.parse("123ab");
        assertFalse(result.isSuccess());
        assertEquals(result.remain().toString(), "123ab");

        result = integer.parse("-12+3");
        assertFalse(result.isSuccess());
        assertEquals(result.remain().toString(), "-12+3");
    }

    @Test
    public void testNumberParsing()
    {
        Parser digit = range('0', '9');
        Parser dot = ch('.');
        Parser pos = ch('+');
        Parser neg = ch('-');
        Parser pow = ch('e');

        Parser sign = pos.or(neg);
        Parser p1 = option(sign)
                .concat(digit.oneOrMore())
                .option(dot.option(digit.oneOrMore()))
                .or(option(sign).concat(dot)
                        .concat(digit.oneOrMore()));
        Parser p2 = pow.option(sign).concat(digit.oneOrMore());

        Parser p = p1.option(p2).end();

        ParseResult result = p.parse("3");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("+3");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("-3");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("123");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("+123");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("-123");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("");
        assertFalse(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("+");
        assertFalse(result.isSuccess());
        assertEquals("+", result.remain().toString());

        result = p.parse("-");
        assertFalse(result.isSuccess());
        assertEquals("-", result.remain().toString());

        result = p.parse("1.5");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("1.23");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("12.3");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("12.34");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("+1.5");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("-1.23");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("-12.3");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("+12.34");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("23.");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse(".6");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse(".");
        assertFalse(result.isSuccess());
        assertEquals(".", result.remain().toString());

        result = p.parse("1e2");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("12e3");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("1e23");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("12e34");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("1.2e3");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("12.34e56");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("23e");
        assertFalse(result.isSuccess());
        assertEquals("23e", result.remain().toString());

        result = p.parse("1.5e");
        assertFalse(result.isSuccess());
        assertEquals("1.5e", result.remain().toString());

        result = p.parse("e567");
        assertFalse(result.isSuccess());
        assertEquals("e567", result.remain().toString());

        result = p.parse("1.2e3.4");
        assertFalse(result.isSuccess());
        assertEquals("1.2e3.4", result.remain().toString());

        result = p.parse("-12.34e-567");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("-78e-56");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("2.e34");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse(".34e-56");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("6e-1");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("-78e-");
        assertFalse(result.isSuccess());
        assertEquals("-78e-", result.remain().toString());

        result = p.parse("12e3.14");
        assertFalse(result.isSuccess());
        assertEquals("12e3.14", result.remain().toString());

        result = p.parse("-+3");
        assertFalse(result.isSuccess());
        assertEquals("-+3", result.remain().toString());

        result = p.parse("e");
        assertFalse(result.isSuccess());
        assertEquals("e", result.remain().toString());

        result = p.parse("95a54e53");
        assertFalse(result.isSuccess());
        assertEquals("95a54e53", result.remain().toString());

        result = p.parse("1.2.3");
        assertFalse(result.isSuccess());
        assertEquals("1.2.3", result.remain().toString());

        result = p.parse("-.2");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("+12.");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());
    }

    @Test
    public void testDecimalParsing()
    {
        Parser digit = range('0', '9');
        Parser dot = ch('.');
        Parser decimal = digit.oneOrMore().concat(dot).concat(digit.oneOrMore()).end();

        ParseResult result = decimal.parse("3.14");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = decimal.parse("123.4567");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = decimal.parse("23.");
        assertFalse(result.isSuccess());
        assertEquals("23.", result.remain().toString());

        result = decimal.parse(".469");
        assertFalse(result.isSuccess());
        assertEquals(".469", result.remain().toString());

        result = decimal.parse("3");
        assertFalse(result.isSuccess());
        assertEquals("3", result.remain().toString());

        result = decimal.parse(".");
        assertFalse(result.isSuccess());
        assertEquals(".", result.remain().toString());

        result = decimal.parse("12.34ab");
        assertFalse(result.isSuccess());
        assertEquals("12.34ab", result.remain().toString());

        result = decimal.parse("ab");
        assertFalse(result.isSuccess());
        assertEquals("ab", result.remain().toString());

        result = decimal.parse("ab12.34");
        assertFalse(result.isSuccess());
        assertEquals("ab12.34", result.remain().toString());
    }

    @Test
    public void testBracketMatch()
    {
        Lazy expr = new Lazy();
        Parser term = ch('(').concat(expr).concat(ch(')'));
        expr.set(term.concat(expr).or(empty));
        Parser p = expr.end();

        ParseResult result;

        result = p.parse("");
        assertTrue(result.isSuccess());
        assertEquals("", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("()");
        assertTrue(result.isSuccess());
        assertEquals("()", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("(())");
        assertTrue(result.isSuccess());
        assertEquals("(())", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("()()");
        assertTrue(result.isSuccess());
        assertEquals("()()", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("(())()");
        assertTrue(result.isSuccess());
        assertEquals("(())()", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("()(())");
        assertTrue(result.isSuccess());
        assertEquals("()(())", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("()()()");
        assertTrue(result.isSuccess());
        assertEquals("()()()", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("(()()())");
        assertTrue(result.isSuccess());
        assertEquals("(()()())", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("(()(()))(())()(((()())))");
        assertTrue(result.isSuccess());
        assertEquals("(()(()))(())()(((()())))", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("(");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("(", result.remain().toString());

        result = p.parse(")");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals(")", result.remain().toString());

        result = p.parse("((");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("((", result.remain().toString());

        result = p.parse("))");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("))", result.remain().toString());

        result = p.parse(")(");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals(")(", result.remain().toString());

        result = p.parse(")))(((");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals(")))(((", result.remain().toString());

        result = p.parse("(()");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("(()", result.remain().toString());

        result = p.parse("())");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("())", result.remain().toString());

        result = p.parse("()()(");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("()()(", result.remain().toString());

        result = p.parse("(()()");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("(()()", result.remain().toString());

        result = p.parse("(()(())");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("(()(())", result.remain().toString());

        result = p.parse("(()(()))(())((((()())))");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("(()(()))(())((((()())))", result.remain().toString());

        result = p.parse("aab(())");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("aab(())", result.remain().toString());

        result = p.parse("(())aab");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("(())aab", result.remain().toString());
    }

    @Test
    public void testListParsing()
    {
        List<Integer> nums = new ArrayList<>();

        Consumer<ParseResult> addNumber = r -> nums.add(Integer.parseInt(r.recognized().toString()));

        Parser digit = range('0', '9');
        Parser integer = digit.oneOrMore();

        Lazy numberListTail = lazy();
        numberListTail.set(ch(',').concat(integer.callback(addNumber)).concat(numberListTail).or(empty));
        Parser numberList = integer.callback(addNumber).concat(numberListTail).or(integer.callback(addNumber));
        Parser list = ch('[').concat(ch(']')).or(ch('[').concat(numberList).concat(ch(']')).or(ch('[')));
        list = list.end();

        ParseResult result = list.parse("[]");
        assertTrue(result.isSuccess());
        assertEquals("[]", result.recognized().toString());
        assertEquals("", result.remain().toString());
        assertEquals(0, nums.size());

        nums.clear();
        result = list.parse("[3]");
        assertTrue(result.isSuccess());
        assertEquals("[3]", result.recognized().toString());
        assertEquals("", result.remain().toString());
        assertEquals(Collections.singletonList(3), nums);

        nums.clear();
        result = list.parse("[123]");
        assertTrue(result.isSuccess());
        assertEquals("[123]", result.recognized().toString());
        assertEquals("", result.remain().toString());
        assertEquals(Collections.singletonList(123), nums);

        nums.clear();
        result = list.parse("[1,2,3]");
        assertTrue(result.isSuccess());
        assertEquals("[1,2,3]", result.recognized().toString());
        assertEquals("", result.remain().toString());
        assertEquals(Arrays.asList(1, 2, 3), nums);

        nums.clear();
        result = list.parse("[12,5435,4,77]");
        assertTrue(result.isSuccess());
        assertEquals("[12,5435,4,77]", result.recognized().toString());
        assertEquals("", result.remain().toString());
        assertEquals(Arrays.asList(12, 5435, 4, 77), nums);

        nums.clear();
        result = list.parse("");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("", result.remain().toString());
        assertEquals(0, nums.size());

        nums.clear();
        result = list.parse("[3");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("[3", result.remain().toString());
        assertEquals(0, nums.size());

        nums.clear();
        result = list.parse("345]");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("345]", result.remain().toString());
        assertEquals(0, nums.size());

        nums.clear();
        result = list.parse("12,3,45");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("12,3,45", result.remain().toString());
        assertEquals(0, nums.size());

        nums.clear();
        result = list.parse("[12 3 45]");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("[12 3 45]", result.remain().toString());
        assertEquals(0, nums.size());

        nums.clear();
        result = list.parse("[12,,3,45]");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("[12,,3,45]", result.remain().toString());
        assertEquals(0, nums.size());

        nums.clear();
        result = list.parse("[,12,34]");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("[,12,34]", result.remain().toString());
        assertEquals(0, nums.size());

        nums.clear();
        result = list.parse("[12,34,]");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("[12,34,]", result.remain().toString());
        assertEquals(0, nums.size());
    }

    @Test
    public void testThreeTimesNumber()
    {
        // 1((10*1)|(01*0))*10*
        Parser one = ch('1');
        Parser zero = ch('0');
        Parser p = one.concat(one.concat(zero.zeroOrMore())
                .concat(one)
                .or(zero.concat(one.zeroOrMore())
                        .concat(zero))
                .zeroOrMore())
                .concat(one)
                .concat(zero.zeroOrMore())
                .end();

        ParseResult result;

        result = p.parse("11");
        assertTrue(result.isSuccess());
        assertEquals("11", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("110");
        assertTrue(result.isSuccess());
        assertEquals("110", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("1100");
        assertTrue(result.isSuccess());
        assertEquals("1100", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("101110001");
        assertTrue(result.isSuccess());
        assertEquals("101110001", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("110111100011100101010001");
        assertTrue(result.isSuccess());
        assertEquals("110111100011100101010001", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("10");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("10", result.remain().toString());

        result = p.parse("100");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("100", result.remain().toString());

        result = p.parse("1101");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("1101", result.remain().toString());

        result = p.parse("110111100011100101010010");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("110111100011100101010010", result.remain().toString());

        result = p.parse("110111100011100101010011");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("110111100011100101010011", result.remain().toString());

        result = p.parse("110111100011100101010100");
        assertTrue(result.isSuccess());
        assertEquals("110111100011100101010100", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("110000001110100001");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("110000001110100001", result.remain().toString());
    }

    @Test
    public void testExprValidation1()
    {
        Parser digit = range('0', '9');
        Parser add = ch('+');
        Parser sub = ch('-');
        Parser mul = ch('*');
        Parser div = ch('/');
        Parser lp = ch('(');
        Parser rp = ch(')');

        Lazy term = new Lazy();
        Lazy expr = new Lazy();

        // factor => digit
        //        |  '(' expr ')'
        Parser factor = digit
                        .or(lp.concat(expr).concat(rp));

        // term => factor '*'|'/' term
        //      |  factor
        term.set(factor.concat(mul.or(div)).concat(term)
                .or(factor));

        // expr => term '+'|'-' expr
        //      |  term
        expr.set(term.concat(add.or(sub)).concat(expr)
                .or(term));

        Parser p = expr.end();

        ParseResult result;

        result = p.parse("1");
        assertTrue(result.isSuccess());
        assertEquals("1", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("3*4");
        assertTrue(result.isSuccess());
        assertEquals("3*4", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("3*4/5/6*7");
        assertTrue(result.isSuccess());
        assertEquals("3*4/5/6*7", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("1+2");
        assertTrue(result.isSuccess());
        assertEquals("1+2", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("1+2-3-4+5");
        assertTrue(result.isSuccess());
        assertEquals("1+2-3-4+5", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("1*2+3*4-5*6+7*8");
        assertTrue(result.isSuccess());
        assertEquals("1*2+3*4-5*6+7*8", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("1*2+3*4*5*6-7-8+9*0");
        assertTrue(result.isSuccess());
        assertEquals("1*2+3*4*5*6-7-8+9*0", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("(1)");
        assertTrue(result.isSuccess());
        assertEquals("(1)", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("(1*2)");
        assertTrue(result.isSuccess());
        assertEquals("(1*2)", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("(1*2/3/4*5)");
        assertTrue(result.isSuccess());
        assertEquals("(1*2/3/4*5)", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("(1+2)");
        assertTrue(result.isSuccess());
        assertEquals("(1+2)", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("(1+2-3-4+5)");
        assertTrue(result.isSuccess());
        assertEquals("(1+2-3-4+5)", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("(1*2-3*4*5+6+7)");
        assertTrue(result.isSuccess());
        assertEquals("(1*2-3*4*5+6+7)", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("(1+2)*3");
        assertTrue(result.isSuccess());
        assertEquals("(1+2)*3", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("3*(1+2)");
        assertTrue(result.isSuccess());
        assertEquals("3*(1+2)", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("(3+4)*(1+2)");
        assertTrue(result.isSuccess());
        assertEquals("(3+4)*(1+2)", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("((3+5/6)+4)*(1+(5*(7-(1/2))))+3");
        assertTrue(result.isSuccess());
        assertEquals("((3+5/6)+4)*(1+(5*(7-(1/2))))+3", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("", result.remain().toString());

        result = p.parse("()");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("()", result.remain().toString());

        result = p.parse(")(");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals(")(", result.remain().toString());

        result = p.parse("1+");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("1+", result.remain().toString());

        result = p.parse("+1");
        assertFalse(result.isSuccess());
        assertEquals("+1", result.remain().toString());

        result = p.parse("1/2/3*");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("1/2/3*", result.remain().toString());

        result = p.parse("1+2-3-");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("1+2-3-", result.remain().toString());

        result = p.parse("(3+4)*1+2)");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("(3+4)*1+2)", result.remain().toString());

        result = p.parse("(3+4)*(1+2)/(5-6");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("(3+4)*(1+2)/(5-6", result.remain().toString());

        result = p.parse("1+2 abc");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("1+2 abc", result.remain().toString());

        result = p.parse("abc1+2");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("abc1+2", result.remain().toString());

        result = p.parse("3*4*5*a");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("3*4*5*a", result.remain().toString());

        result = p.parse("a*3*4*5");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("a*3*4*5", result.remain().toString());
    }

    @Test
    public void testExprValidation2()
    {
        Parser digit = range('0', '9');
        Parser add = ch('+');
        Parser sub = ch('-');
        Parser mul = ch('*');
        Parser div = ch('/');
        Parser lp = ch('(');
        Parser rp = ch(')');

        Lazy expr = new Lazy();
        Lazy term = new Lazy();

        // factor -> digit
        //        |  '(' expr ')'
        Parser factor = digit
                .or(lp.concat(expr).concat(rp));

        // term -> factor '*' term
        //      |  factor '/' term
        //      |  factor
        term.set(factor.concat(mul.concat(term))
                .or(factor.concat(div.concat(term)))
                .or(factor));

        // expr -> term '+' expr
        //      |  term '-' expr
        //      | term
        expr.set(term.concat(add.concat(expr))
                .or(term.concat(sub.concat(expr)))
                .or(term));

        Parser p = expr.end();

        ParseResult result;

        result = p.parse("1");
        assertTrue(result.isSuccess());
        assertEquals("1", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("3*4");
        assertTrue(result.isSuccess());
        assertEquals("3*4", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("3*4/5/6*7");
        assertTrue(result.isSuccess());
        assertEquals("3*4/5/6*7", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("1+2");
        assertTrue(result.isSuccess());
        assertEquals("1+2", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("1+2-3-4+5");
        assertTrue(result.isSuccess());
        assertEquals("1+2-3-4+5", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("1*2+3*4-5*6+7*8");
        assertTrue(result.isSuccess());
        assertEquals("1*2+3*4-5*6+7*8", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("1*2+3*4*5*6-7-8+9*0");
        assertTrue(result.isSuccess());
        assertEquals("1*2+3*4*5*6-7-8+9*0", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("(1)");
        assertTrue(result.isSuccess());
        assertEquals("(1)", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("(1*2)");
        assertTrue(result.isSuccess());
        assertEquals("(1*2)", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("(1*2/3/4*5)");
        assertTrue(result.isSuccess());
        assertEquals("(1*2/3/4*5)", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("(1+2)");
        assertTrue(result.isSuccess());
        assertEquals("(1+2)", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("(1+2-3-4+5)");
        assertTrue(result.isSuccess());
        assertEquals("(1+2-3-4+5)", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("(1*2-3*4*5+6+7)");
        assertTrue(result.isSuccess());
        assertEquals("(1*2-3*4*5+6+7)", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("(1+2)*3");
        assertTrue(result.isSuccess());
        assertEquals("(1+2)*3", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("3*(1+2)");
        assertTrue(result.isSuccess());
        assertEquals("3*(1+2)", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("(3+4)*(1+2)");
        assertTrue(result.isSuccess());
        assertEquals("(3+4)*(1+2)", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("((3+5/6)+4)*(1+(5*(7-(1/2))))+3");
        assertTrue(result.isSuccess());
        assertEquals("((3+5/6)+4)*(1+(5*(7-(1/2))))+3", result.recognized().toString());
        assertEquals("", result.remain().toString());

        result = p.parse("");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("", result.remain().toString());

        result = p.parse("()");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("()", result.remain().toString());

        result = p.parse(")(");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals(")(", result.remain().toString());

        result = p.parse("1+");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("1+", result.remain().toString());

        result = p.parse("+1");
        assertFalse(result.isSuccess());
        assertEquals("+1", result.remain().toString());

        result = p.parse("1/2/3*");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("1/2/3*", result.remain().toString());

        result = p.parse("1+2-3-");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("1+2-3-", result.remain().toString());

        result = p.parse("(3+4)*1+2)");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("(3+4)*1+2)", result.remain().toString());

        result = p.parse("(3+4)*(1+2)/(5-6");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("(3+4)*(1+2)/(5-6", result.remain().toString());

        result = p.parse("1+2 abc");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("1+2 abc", result.remain().toString());

        result = p.parse("abc1+2");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("abc1+2", result.remain().toString());

        result = p.parse("3*4*5*a");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("3*4*5*a", result.remain().toString());

        result = p.parse("a*3*4*5");
        assertFalse(result.isSuccess());
        assertNull(result.recognized());
        assertEquals("a*3*4*5", result.remain().toString());
    }

    @Test
    public void testExprEval()
    {
        Stack<Integer> stack = new Stack<>();

        Consumer<ParseResult> push = r -> stack.push(Integer.parseInt(r.recognized().toString()));

        Consumer<ParseResult> calc = r ->
        {
            int rhs = stack.pop();
            int lhs = stack.pop();
            switch (r.recognized().charAt(0))
            {
                case '+' -> stack.push(lhs + rhs);
                case '-' -> stack.push(lhs - rhs);
                case '*' -> stack.push(lhs * rhs);
                case '/' -> stack.push(lhs / rhs);
            }
        };

        Parser digit = range('0', '9');
        Parser add = ch('+');
        Parser sub = ch('-');
        Parser mul = ch('*');
        Parser div = ch('/');
        Parser lp = ch('(');
        Parser rp = ch(')');

        Lazy expr = lazy();
        Parser factor = or(digit.callback(push),
                           lp.concat(expr).concat(rp));
        Parser term = factor.concat(mul.or(div).concat(factor).callback(calc).zeroOrMore());
        expr.set(term.concat(add.or(sub).concat(term).callback(calc).zeroOrMore()));
        Parser p = expr.end();

        ParseResult result;

        stack.clear();
        result = p.parse("2");
        assertTrue(result.isSuccess());
        assertEquals("2", result.recognized().toString());
        assertEquals("", result.remain().toString());
        assertEquals(2, (int)stack.pop());

        stack.clear();
        result = p.parse("2*3");
        assertTrue(result.isSuccess());
        assertEquals("2*3", result.recognized().toString());
        assertEquals("", result.remain().toString());
        assertEquals(6, (int)stack.pop());

        stack.clear();
        result = p.parse("9/3");
        assertTrue(result.isSuccess());
        assertEquals("9/3", result.recognized().toString());
        assertEquals("", result.remain().toString());
        assertEquals(3, (int)stack.pop());

        stack.clear();
        result = p.parse("7+8");
        assertTrue(result.isSuccess());
        assertEquals("7+8", result.recognized().toString());
        assertEquals("", result.remain().toString());
        assertEquals(15, (int)stack.pop());

        stack.clear();
        result = p.parse("2+3*4");
        assertTrue(result.isSuccess());
        assertEquals("2+3*4", result.recognized().toString());
        assertEquals("", result.remain().toString());
        assertEquals(14, (int)stack.pop());

        stack.clear();
        result = p.parse("3*5+7");
        assertTrue(result.isSuccess());
        assertEquals("3*5+7", result.recognized().toString());
        assertEquals("", result.remain().toString());
        assertEquals(22, (int)stack.pop());

        stack.clear();
        result = p.parse("(3)");
        assertTrue(result.isSuccess());
        assertEquals("(3)", result.recognized().toString());
        assertEquals("", result.remain().toString());
        assertEquals(3, (int)stack.pop());

        stack.clear();
        result = p.parse("((5))");
        assertTrue(result.isSuccess());
        assertEquals("((5))", result.recognized().toString());
        assertEquals("", result.remain().toString());
        assertEquals(5, (int)stack.pop());

        stack.clear();
        result = p.parse("(1+2)");
        assertTrue(result.isSuccess());
        assertEquals("(1+2)", result.recognized().toString());
        assertEquals("", result.remain().toString());
        assertEquals(3, (int)stack.pop());

        stack.clear();
        result = p.parse("(2+3)*4");
        assertTrue(result.isSuccess());
        assertEquals("(2+3)*4", result.recognized().toString());
        assertEquals("", result.remain().toString());
        assertEquals(20, (int)stack.pop());

        stack.clear();
        result = p.parse("2+(3*4)");
        assertTrue(result.isSuccess());
        assertEquals("2+(3*4)", result.recognized().toString());
        assertEquals("", result.remain().toString());
        assertEquals(14, (int)stack.pop());

        stack.clear();
        result = p.parse("(2+3)*(4+5)");
        assertTrue(result.isSuccess());
        assertEquals("(2+3)*(4+5)", result.recognized().toString());
        assertEquals("", result.remain().toString());
        assertEquals(45, (int)stack.pop());

        stack.clear();
        result = p.parse("2+3*4+5");
        assertTrue(result.isSuccess());
        assertEquals("2+3*4+5", result.recognized().toString());
        assertEquals("", result.remain().toString());
        assertEquals(19, (int)stack.pop());

        stack.clear();
        result = p.parse("(5+3)/6+3*((4+5)/3-2*7)/(0-6*(4-3))");
        assertTrue(result.isSuccess());
        assertEquals("(5+3)/6+3*((4+5)/3-2*7)/(0-6*(4-3))", result.recognized().toString());
        assertEquals("", result.remain().toString());
        assertEquals(6, (int)stack.pop());
    }

    @Test
    public void testBracketNestedLayerWithAttributes()
    {
        Parser lp = ch('('), rp = ch(')');
        Lazy expr = lazy();
        expr.set(or(lp.concat(expr).concat(rp).callback(r -> expr.setAttribute("layer", (Integer)expr.getAttribute("layer") + 1)),
                    literal("()").callback(r -> expr.setAttribute("layer", 1))));

        ParseResult result = expr.parse("()");
        assertTrue(result.isSuccess());
        assertEquals("()", result.recognized().toString());
        assertEquals("", result.remain().toString());
        assertEquals(1, expr.getAttribute("layer"));

        result = expr.parse("(())");
        assertTrue(result.isSuccess());
        assertEquals("(())", result.recognized().toString());
        assertEquals("", result.remain().toString());
        assertEquals(2, expr.getAttribute("layer"));

        result = expr.parse("((((()))))");
        assertTrue(result.isSuccess());
        assertEquals("((((()))))", result.recognized().toString());
        assertEquals("", result.remain().toString());
        assertEquals(5, expr.getAttribute("layer"));
    }

    @Test
    public void testAddExprEvalWithAttributes()
    {
        Parser digit = range('0', '9'), add = ch('+');
        Lazy expr = lazy();
        Consumer<ParseResult> accumulate = r ->
        {
            int val = Integer.parseInt(r.recognized().toString());
            if (expr.hasAttrbute("val"))
                expr.setAttribute("val", (int)expr.getAttribute("val") + val);
            else
                expr.setAttribute("val", val);
        };
        expr.set(or(digit.callback(accumulate).concat(add).concat(expr),
                    digit.callback(accumulate)));

        ParseResult result;

        result = expr.parse("3");
        assertTrue(result.isSuccess());
        assertEquals("3", result.recognized().toString());
        assertEquals("", result.remain().toString());
        assertEquals(3, expr.removeAttribute("val"));

        result = expr.parse("3+4");
        assertTrue(result.isSuccess());
        assertEquals("3+4", result.recognized().toString());
        assertEquals("", result.remain().toString());
        assertEquals(7, expr.removeAttribute("val"));

        result = expr.parse("1+2+3");
        assertTrue(result.isSuccess());
        assertEquals("1+2+3", result.recognized().toString());
        assertEquals("", result.remain().toString());
        assertEquals(6, expr.getAttribute("val"));

        expr.setAttribute("val", 0);
        result = expr.parse("3+4+5+6+7");
        assertTrue(result.isSuccess());
        assertEquals("3+4+5+6+7", result.recognized().toString());
        assertEquals("", result.remain().toString());
        assertEquals(25, expr.getAttribute("val"));
    }

    @Test
    public void testIntegerParsingWithAttributes()
    {
        Parser digit = range('0', '9');
        Lazy integer = lazy();
        Consumer<ParseResult> accumulate = r ->
        {
            int val = Integer.parseInt(r.recognized().toString());
            integer.setAttribute("val", (Integer)integer.getAttribute("val") * 10 + val);
        };
        integer.set(or(digit.callback(accumulate).concat(integer),
                       digit.callback(accumulate)));


        integer.setAttribute("val", 0);
        ParseResult result = integer.parse("1");
        assertTrue(result.isSuccess());
        assertEquals("1", result.recognized().toString());
        assertEquals("", result.remain().toString());
        assertEquals(1, integer.getAttribute("val"));

        integer.setAttribute("val", 0);
        result = integer.parse("12");
        assertTrue(result.isSuccess());
        assertEquals("12", result.recognized().toString());
        assertEquals("", result.remain().toString());
        assertEquals(12, integer.getAttribute("val"));

        integer.setAttribute("val", 0);
        result = integer.parse("12345");
        assertTrue(result.isSuccess());
        assertEquals("12345", result.recognized().toString());
        assertEquals("", result.remain().toString());
        assertEquals(12345, integer.getAttribute("val"));
    }

    /*@Test
    public void testExprEvalWithAttributes()
    {
        Parser digit = range('0', '9');
        Parser add = ch('+');
        Parser sub = ch('-');
        Parser mul = ch('*');
        Parser div = ch('/');
        Parser lp = ch('(');
        Parser rp = ch(')');

        Lazy expr = lazy();
        Lazy term = lazy();
        Lazy factor = lazy();

        Consumer<ParseResult> setDigitToFactor = r ->
        {
            int val = Integer.parseInt(r.recognized().toString());
            factor.setAttribute("val", val);
            System.out.println("factor.val = " + val);
        };

        Consumer<ParseResult> setExprToFactor = r ->
        {
            factor.setAttribute("val", (int)expr.getAttribute("val"));
        };

        Consumer<ParseResult> mulFactorToTerm = r ->
        {
            int val = (int)factor.getAttribute("val");
            if (term.hasAttrbute("val"))
                term.setAttribute("val", (int)term.getAttribute("val") * val);
            else
                term.setAttribute("val", val);
            System.out.println("mul term.val = " + term.getAttribute("val"));
        };

        Consumer<ParseResult> divFactorToTerm = r ->
        {
            int val = (int)factor.getAttribute("val");
            if (term.hasAttrbute("val"))
                term.setAttribute("val", (int)term.getAttribute("val") / val);
            else
                term.setAttribute("val", val);
        };

        Consumer<ParseResult> setFactorToTerm = r ->
        {
            term.setAttribute("val", (int)factor.getAttribute("val"));
            System.out.println("term.val = " + term.getAttribute("val"));
        };

        Consumer<ParseResult> addTermToExpr = r ->
        {
            int val = (int)term.getAttribute("val");
            if (expr.hasAttrbute("val"))
                expr.setAttribute("val", (int)expr.getAttribute("val") + val);
            else
                expr.setAttribute("val", val);
        };

        Consumer<ParseResult> subTermToExpr = r ->
        {
            int val = (int)term.getAttribute("val");
            if (expr.hasAttrbute("val"))
                expr.setAttribute("val", (int)expr.getAttribute("val") - val);
            else
                expr.setAttribute("val", val);
        };

        Consumer<ParseResult> setTermToExpr = r ->
        {
            expr.setAttribute("val", (int)term.getAttribute("val"));
            System.out.println("expr.val = " + expr.getAttribute("val"));
        };

        factor.set(or(digit.callback(setDigitToFactor),
                      lp.concat(expr).concat(rp).callback(setExprToFactor)));
        term.set(or(factor.callback(mulFactorToTerm).concat(mul).concat(term),
                    factor.callback(divFactorToTerm).concat(div).concat(term),
                    factor.callback(setFactorToTerm)));
        expr.set(or(term.callback(addTermToExpr).concat(add).concat(expr),
                    term.callback(subTermToExpr).concat(sub).concat(expr),
                    term.callback(setTermToExpr)));

        ParseResult result;

        result = expr.parse("3*4");

    }*/
}
