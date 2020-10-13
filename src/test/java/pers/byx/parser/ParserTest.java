package pers.byx.parser;

import org.junit.Test;
import pers.byx.parser.core.*;
import static pers.byx.parser.core.Parsers.*;

import java.util.*;

import static org.junit.Assert.*;

public class ParserTest
{
    @Test
    public void testEmpty()
    {
        Parser p = new Empty();
        ParseResult result = p.parse("");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("abc");
        assertTrue(result.isSuccess());
        assertEquals("abc", result.remain().toString());
    }

    @Test
    public void testAny()
    {
        Parser p = new Any();
        ParseResult result = p.parse("");
        assertFalse(result.isSuccess());
        assertEquals(result.remain().toString(), "");

        result = p.parse("a");
        assertTrue(result.isSuccess());
        assertEquals(result.remain().toString(), "");

        result = p.parse("sdfsgsa");
        assertTrue(result.isSuccess());
        assertEquals(result.remain().toString(), "dfsgsa");
    }

    @Test
    public void testChar()
    {
        Parser p = new Char('a');
        ParseResult result = p.parse("a");
        assertTrue(result.isSuccess());
        assertEquals(result.remain().toString(), "");

        result = p.parse("b");
        assertFalse(result.isSuccess());
        assertEquals(result.remain().toString(), "b");

        result = p.parse("ab");
        assertTrue(result.isSuccess());
        assertEquals(result.remain().toString(), "b");

        result = p.parse("bc");
        assertFalse(result.isSuccess());
        assertEquals(result.remain().toString(), "bc");

        result = p.parse("");
        assertFalse(result.isSuccess());
        assertEquals(result.remain().toString(), "");
    }

    @Test
    public void testEnd()
    {
        Parser p = new End();
        ParseResult result = p.parse("");
        assertTrue(result.isSuccess());
        assertEquals(result.remain().toString(), "");

        result = p.parse("a");
        assertFalse(result.isSuccess());
        assertEquals(result.remain().toString(), "a");

        result = p.parse("ab");
        assertFalse(result.isSuccess());
        assertEquals(result.remain().toString(), "ab");

        result = p.parse(" ");
        assertFalse(result.isSuccess());
        assertEquals(result.remain().toString(), " ");
    }

    @Test
    public void testOnSuccess()
    {
        final boolean[] flag = {false};
        Parser p = new ActionOnSuccess(new Char('a'), sequence ->
        {
            assertEquals(sequence.toString(), "a");
            flag[0] = true;
        });

        ParseResult result = p.parse("abc");
        assertTrue(result.isSuccess());
        assertEquals(result.remain().toString(), "bc");
        assertTrue(flag[0]);

        flag[0] = false;
        result = p.parse("xyz");
        assertFalse(result.isSuccess());
        assertEquals(result.remain().toString(), "xyz");
        assertFalse(flag[0]);
    }

    @Test
    public void testOnFail()
    {
        final boolean[] flag = {false};
        Parser p = new ActionOnFail(new Char('a'), remain ->
        {
            assertEquals(remain.toString(), "xyz");
            flag[0] = true;
        });

        ParseResult result = p.parse("abc");
        assertTrue(result.isSuccess());
        assertEquals(result.remain().toString(), "bc");
        assertFalse(flag[0]);

        result = p.parse("xyz");
        assertFalse(result.isSuccess());
        assertEquals(result.remain().toString(), "xyz");
        assertTrue(flag[0]);
    }

    @Test
    public void testConcat()
    {
        Parser p1 = new Char('a');
        Parser p2 = new Char('b');
        Parser p = new Concat(p1, p2);
        ParseResult result = p.parse("");
        assertFalse(result.isSuccess());
        assertEquals(result.remain().toString(), "");

        result = p.parse("a");
        assertFalse(result.isSuccess());
        assertEquals(result.remain().toString(), "a");

        result = p.parse("b");
        assertFalse(result.isSuccess());
        assertEquals(result.remain().toString(), "b");

        result = p.parse("ab");
        assertTrue(result.isSuccess());
        assertEquals(result.remain().toString(), "");

        result = p.parse("bc");
        assertFalse(result.isSuccess());
        assertEquals(result.remain().toString(), "bc");

        result = p.parse("ac");
        assertFalse(result.isSuccess());
        assertEquals(result.remain().toString(), "ac");

        result = p.parse("abc");
        assertTrue(result.isSuccess());
        assertEquals(result.remain().toString(), "c");
    }

    @Test
    public void testOr()
    {
        Parser p1 = new Char('a');
        Parser p2 = new Char('b');
        Parser p = new Or(p1, p2);
        ParseResult result = p.parse("");
        assertFalse(result.isSuccess());
        assertEquals(result.remain().toString(), "");

        result = p.parse("a");
        assertTrue(result.isSuccess());
        assertEquals(result.remain().toString(), "");

        result = p.parse("b");
        assertTrue(result.isSuccess());
        assertEquals(result.remain().toString(), "");

        result = p.parse("x");
        assertFalse(result.isSuccess());
        assertEquals(result.remain().toString(), "x");

        result = p.parse("am");
        assertTrue(result.isSuccess());
        assertEquals(result.remain().toString(), "m");

        result = p.parse("bc");
        assertTrue(result.isSuccess());
        assertEquals(result.remain().toString(), "c");

        result = p.parse("xy");
        assertFalse(result.isSuccess());
        assertEquals(result.remain().toString(), "xy");
    }

    @Test
    public void testZeroOrMore()
    {
        Parser p = new ZeroOrMore(new Char('a'));
        ParseResult result = p.parse("");
        assertTrue(result.isSuccess());
        assertEquals(result.remain().toString(), "");

        result = p.parse("a");
        assertTrue(result.isSuccess());
        assertEquals(result.remain().toString(), "");

        result = p.parse("b");
        assertTrue(result.isSuccess());
        assertEquals(result.remain().toString(), "b");

        result = p.parse("aaaaa");
        assertTrue(result.isSuccess());
        assertEquals(result.remain().toString(), "");

        result = p.parse("aaaaabbb");
        assertTrue(result.isSuccess());
        assertEquals(result.remain().toString(), "bbb");

        result = p.parse("bbb");
        assertTrue(result.isSuccess());
        assertEquals(result.remain().toString(), "bbb");
    }

    @Test
    public void testOneOrMore()
    {
        Parser p = new OneOrMore(new Char('a'));
        ParseResult result = p.parse("");
        assertFalse(result.isSuccess());
        assertEquals(result.remain().toString(), "");

        result = p.parse("a");
        assertTrue(result.isSuccess());
        assertEquals(result.remain().toString(), "");

        result = p.parse("b");
        assertFalse(result.isSuccess());
        assertEquals(result.remain().toString(), "b");

        result = p.parse("aaaaa");
        assertTrue(result.isSuccess());
        assertEquals(result.remain().toString(), "");

        result = p.parse("aaaaabbb");
        assertTrue(result.isSuccess());
        assertEquals(result.remain().toString(), "bbb");

        result = p.parse("bbb");
        assertFalse(result.isSuccess());
        assertEquals(result.remain().toString(), "bbb");
    }

    @Test
    public void testLazy()
    {
        Lazy p = new Lazy();
        p.set(new Char('a'));
        ParseResult result = p.parse("abc");
        assertTrue(result.isSuccess());
        assertEquals(result.remain().toString(), "bc");

        result = p.parse("xyz");
        assertFalse(result.isSuccess());
        assertEquals(result.remain().toString(), "xyz");
    }

    @Test
    public void testCharset()
    {
        Parser p = new Charset('a', 'b', 'c');
        ParseResult result = p.parse("");
        assertFalse(result.isSuccess());
        assertEquals(result.remain().toString(), "");

        result = p.parse("a");
        assertTrue(result.isSuccess());
        assertEquals(result.remain().toString(), "");

        result = p.parse("d");
        assertFalse(result.isSuccess());
        assertEquals(result.remain().toString(), "d");

        result = p.parse("apple");
        assertTrue(result.isSuccess());
        assertEquals(result.remain().toString(), "pple");

        result = p.parse("banana");
        assertTrue(result.isSuccess());
        assertEquals(result.remain().toString(), "anana");

        result = p.parse("cat");
        assertTrue(result.isSuccess());
        assertEquals(result.remain().toString(), "at");

        result = p.parse("dress");
        assertFalse(result.isSuccess());
        assertEquals(result.remain().toString(), "dress");
    }

    @Test
    public void testRange()
    {
        Parser p = new Range('u', 'w');
        ParseResult result = p.parse("");
        assertFalse(result.isSuccess());
        assertEquals(result.remain().toString(), "");

        result = p.parse("u");
        assertTrue(result.isSuccess());
        assertEquals(result.remain().toString(), "");

        result = p.parse("w");
        assertTrue(result.isSuccess());
        assertEquals(result.remain().toString(), "");

        result = p.parse("t");
        assertFalse(result.isSuccess());
        assertEquals(result.remain().toString(), "t");

        result = p.parse("x");
        assertFalse(result.isSuccess());
        assertEquals(result.remain().toString(), "x");

        p = new Range('w', 'u');

        result = p.parse("");
        assertFalse(result.isSuccess());
        assertEquals(result.remain().toString(), "");

        result = p.parse("u");
        assertTrue(result.isSuccess());
        assertEquals(result.remain().toString(), "");

        result = p.parse("w");
        assertTrue(result.isSuccess());
        assertEquals(result.remain().toString(), "");

        result = p.parse("t");
        assertFalse(result.isSuccess());
        assertEquals(result.remain().toString(), "t");

        result = p.parse("x");
        assertFalse(result.isSuccess());
        assertEquals(result.remain().toString(), "x");
    }

    @Test
    public void testLiteral()
    {
        Parser p = new Literal("double");
        ParseResult result = p.parse("");
        assertFalse(result.isSuccess());
        assertEquals(result.remain().toString(), "");

        result = p.parse("double x = 3.14;");
        assertTrue(result.isSuccess());
        assertEquals(result.remain().toString(), " x = 3.14;");

        result = p.parse("int i = 100;");
        assertFalse(result.isSuccess());
        assertEquals(result.remain().toString(), "int i = 100;");

        result = p.parse("abc");
        assertFalse(result.isSuccess());
        assertEquals(result.remain().toString(), "abc");

        result = p.parse("doub");
        assertFalse(result.isSuccess());
        assertEquals(result.remain().toString(), "doub");
    }

    @Test
    public void testPeek()
    {
        Parser p = new Peek(new Char('a'));
        ParseResult result;

        result = p.parse("");
        assertFalse(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("b");
        assertFalse(result.isSuccess());
        assertEquals("b", result.remain().toString());

        result = p.parse("a");
        assertTrue(result.isSuccess());
        assertEquals("a", result.remain().toString());

        result = p.parse("abc");
        assertTrue(result.isSuccess());
        assertEquals("abc", result.remain().toString());

        result = p.parse("bcd");
        assertFalse(result.isSuccess());
        assertEquals("bcd", result.remain().toString());
    }

    @Test
    public void testOption()
    {
        Parser p = new Option(new Char('+').or(new Char('-'))).concat(new Char('a'));

        ParseResult result = p.parse("+a");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("-a");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("a");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("*a");
        assertFalse(result.isSuccess());
        assertEquals("*a", result.remain().toString());

        result = p.parse("+b");
        assertFalse(result.isSuccess());
        assertEquals("+b", result.remain().toString());
    }

    @Test
    public void testTraceback()
    {
        Parser a = ch('a'), b = ch('b'), c = ch('c'), d = ch('d'), e = ch('e');
        Parser p = a.concat(b).concat(c).or(a.concat(b).concat(d));
        ParseResult result = p.parse("abc");
        assertTrue(result.isSuccess());
        assertEquals(result.remain().toString(), "");

        result = p.parse("abd");
        assertTrue(result.isSuccess());
        assertEquals(result.remain().toString(), "");

        result = p.parse("abc123");
        assertTrue(result.isSuccess());
        assertEquals(result.remain().toString(), "123");

        result = p.parse("abd123");
        assertTrue(result.isSuccess());
        assertEquals(result.remain().toString(), "123");

        result = p.parse("abx");
        assertFalse(result.isSuccess());
        assertEquals(result.remain().toString(), "abx");

        result = p.parse("axy");
        assertFalse(result.isSuccess());
        assertEquals(result.remain().toString(), "axy");

        p = a.concat(b.zeroOrMore()).peek(d).or(a.concat(c.zeroOrMore()).peek(e));

        result = p.parse("abbbbbd");
        assertTrue(result.isSuccess());
        assertEquals("d", result.remain().toString());

        result = p.parse("ad");
        assertTrue(result.isSuccess());
        assertEquals("d", result.remain().toString());

        result = p.parse("accce");
        assertTrue(result.isSuccess());
        assertEquals("e", result.remain().toString());

        result = p.parse("ae");
        assertTrue(result.isSuccess());
        assertEquals("e", result.remain().toString());

        result = p.parse("abbbbbe");
        assertFalse(result.isSuccess());
        assertEquals("abbbbbe", result.remain().toString());

        result = p.parse("acccd");
        assertFalse(result.isSuccess());
        assertEquals("acccd", result.remain().toString());
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
        assertEquals("", result.remain().toString());

        result = p.parse("()");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("(())");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("()()");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("(())()");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("()(())");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("()()()");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("(()()())");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("(()(()))(())()(((()())))");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("(");
        assertFalse(result.isSuccess());
        assertEquals("(", result.remain().toString());

        result = p.parse(")");
        assertFalse(result.isSuccess());
        assertEquals(")", result.remain().toString());

        result = p.parse("((");
        assertFalse(result.isSuccess());
        assertEquals("((", result.remain().toString());

        result = p.parse("))");
        assertFalse(result.isSuccess());
        assertEquals("))", result.remain().toString());

        result = p.parse(")(");
        assertFalse(result.isSuccess());
        assertEquals(")(", result.remain().toString());

        result = p.parse(")))(((");
        assertFalse(result.isSuccess());
        assertEquals(")))(((", result.remain().toString());

        result = p.parse("(()");
        assertFalse(result.isSuccess());
        assertEquals("(()", result.remain().toString());

        result = p.parse("())");
        assertFalse(result.isSuccess());
        assertEquals("())", result.remain().toString());

        result = p.parse("()()(");
        assertFalse(result.isSuccess());
        assertEquals("()()(", result.remain().toString());

        result = p.parse("(()()");
        assertFalse(result.isSuccess());
        assertEquals("(()()", result.remain().toString());

        result = p.parse("(()(())");
        assertFalse(result.isSuccess());
        assertEquals("(()(())", result.remain().toString());

        result = p.parse("(()(()))(())((((()())))");
        assertFalse(result.isSuccess());
        assertEquals("(()(()))(())((((()())))", result.remain().toString());

        result = p.parse("aab(())");
        assertFalse(result.isSuccess());
        assertEquals("aab(())", result.remain().toString());

        result = p.parse("(())aab");
        assertFalse(result.isSuccess());
        assertEquals("(())aab", result.remain().toString());
    }

    @Test
    public void testListParsing()
    {
        List<Integer> list = new ArrayList<>();

        Parser digit = range('0', '9');
        Lazy LR = new Lazy();
        LR.set(ch(',').concat(digit.onSuccess(s -> list.add(Integer.parseInt(s.toString()))))
                .concat(LR)
                .or(empty));
        Parser p = digit.onSuccess(s -> list.add(Integer.parseInt(s.toString())))
                .concat(LR)
                .or(empty)
                .end()
                .onFail(s -> list.clear());

        ParseResult result = p.parse("");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());
        assertEquals(new ArrayList<Integer>(), list);

        list.clear();
        result = p.parse("1");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());
        assertEquals(Collections.singletonList(1), list);

        list.clear();
        result = p.parse("1,2");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());
        assertEquals(Arrays.asList(1, 2), list);

        list.clear();
        result = p.parse("1,2,3,4,5");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());
        assertEquals(Arrays.asList(1, 2, 3, 4, 5), list);

        list.clear();
        result = p.parse("1, 2, 3");
        assertFalse(result.isSuccess());
        assertEquals("1, 2, 3", result.remain().toString());
        assertEquals(new ArrayList<>(), list);

        list.clear();
        result = p.parse(",1,2,3");
        assertFalse(result.isSuccess());
        assertEquals(",1,2,3", result.remain().toString());
        assertEquals(new ArrayList<>(), list);

        list.clear();
        result = p.parse("1,2,3,a,4");
        assertFalse(result.isSuccess());
        assertEquals("1,2,3,a,4", result.remain().toString());
        assertEquals(new ArrayList<>(), list);
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
        assertEquals("", result.remain().toString());

        result = p.parse("110");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("1100");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("101110001");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("110111100011100101010001");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("10");
        assertFalse(result.isSuccess());
        assertEquals("10", result.remain().toString());

        result = p.parse("100");
        assertFalse(result.isSuccess());
        assertEquals("100", result.remain().toString());

        result = p.parse("1101");
        assertFalse(result.isSuccess());
        assertEquals("1101", result.remain().toString());

        result = p.parse("110111100011100101010010");
        assertFalse(result.isSuccess());
        assertEquals("110111100011100101010010", result.remain().toString());

        result = p.parse("110111100011100101010011");
        assertFalse(result.isSuccess());
        assertEquals("110111100011100101010011", result.remain().toString());

        result = p.parse("110111100011100101010100");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("110000001110100001");
        assertFalse(result.isSuccess());
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
        assertEquals("", result.remain().toString());

        result = p.parse("3*4");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("3*4/5/6*7");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("1+2");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("1+2-3-4+5");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("1*2+3*4-5*6+7*8");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("1*2+3*4*5*6-7-8+9*0");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("(1)");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("(1*2)");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("(1*2/3/4*5)");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("(1+2)");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("(1+2-3-4+5)");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("(1*2-3*4*5+6+7)");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("(1+2)*3");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("3*(1+2)");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("(3+4)*(1+2)");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("((3+5/6)+4)*(1+(5*(7-(1/2))))+3");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("");
        assertFalse(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("()");
        assertFalse(result.isSuccess());
        assertEquals("()", result.remain().toString());

        result = p.parse("1+");
        assertFalse(result.isSuccess());
        assertEquals("1+", result.remain().toString());

        result = p.parse("+1");
        assertFalse(result.isSuccess());
        assertEquals("+1", result.remain().toString());

        result = p.parse("1/2/3*");
        assertFalse(result.isSuccess());
        assertEquals("1/2/3*", result.remain().toString());

        result = p.parse("1+2-3-");
        assertFalse(result.isSuccess());
        assertEquals("1+2-3-", result.remain().toString());

        result = p.parse("(3+4)*1+2)");
        assertFalse(result.isSuccess());
        assertEquals("(3+4)*1+2)", result.remain().toString());

        result = p.parse("(3+4)*(1+2)/(5-6");
        assertFalse(result.isSuccess());
        assertEquals("(3+4)*(1+2)/(5-6", result.remain().toString());

        result = p.parse("1+2 abc");
        assertFalse(result.isSuccess());
        assertEquals("1+2 abc", result.remain().toString());

        result = p.parse("abc1+2");
        assertFalse(result.isSuccess());
        assertEquals("abc1+2", result.remain().toString());

        result = p.parse("3*4*5*a");
        assertFalse(result.isSuccess());
        assertEquals("3*4*5*a", result.remain().toString());

        result = p.parse("a*3*4*5");
        assertFalse(result.isSuccess());
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
        assertEquals("", result.remain().toString());

        result = p.parse("3*4");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = term.parse("3/4");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = term.parse("3/4*5");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = term.parse("3/4/5*6/7*8");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("1+2");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("1+2-3-4+5");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("1-2+3+4+5");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("1*2+3*4-5*6/7+7/8*9");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("1*2+3*4*5*6-7-8+9*0");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("(1)");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = factor.parse("(1*2)");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("(1+2)");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("(1*2/3/4*5)");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("(1+2-3-4+5)");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("(1*2-3*4/5+6+7)");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("(1+2)*3");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("3*(1+2)");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("(3+4)*(1+2)");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("((3+5/6*3)+4)*(1+(5*(7-(1/2)+4)))+3");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("");
        assertFalse(result.isSuccess());
        assertEquals("", result.remain().toString());

        result = p.parse("()");
        assertFalse(result.isSuccess());
        assertEquals("()", result.remain().toString());

        result = p.parse("1+");
        assertFalse(result.isSuccess());
        assertEquals("1+", result.remain().toString());

        result = p.parse("+1");
        assertFalse(result.isSuccess());
        assertEquals("+1", result.remain().toString());

        result = p.parse("1/2/3*");
        assertFalse(result.isSuccess());
        assertEquals("1/2/3*", result.remain().toString());

        result = p.parse("1+2-3-");
        assertFalse(result.isSuccess());
        assertEquals("1+2-3-", result.remain().toString());

        result = p.parse("(3+4)*1+2)");
        assertFalse(result.isSuccess());
        assertEquals("(3+4)*1+2)", result.remain().toString());

        result = p.parse("(3+4)*(1+2)/(5-6");
        assertFalse(result.isSuccess());
        assertEquals("(3+4)*(1+2)/(5-6", result.remain().toString());

        result = p.parse("1+2 abc");
        assertFalse(result.isSuccess());
        assertEquals("1+2 abc", result.remain().toString());

        result = p.parse("abc1+2");
        assertFalse(result.isSuccess());
        assertEquals("abc1+2", result.remain().toString());

        result = p.parse("3*4*5*a");
        assertFalse(result.isSuccess());
        assertEquals("3*4*5*a", result.remain().toString());

        result = p.parse("a*3*4*5");
        assertFalse(result.isSuccess());
        assertEquals("a*3*4*5", result.remain().toString());
    }

    @Test
    public void testExprEval()
    {
        Stack<Integer> stack = new Stack<>();

        Parser digit = range('0', '9');
        Parser add = ch('+');
        Parser sub = ch('-');
        Parser mul = ch('*');
        Parser div = ch('/');
        Parser lp = ch('(');
        Parser rp = ch(')');

        Lazy expr = new Lazy();

        Parser factor = digit.onSuccess(sequence -> stack.push(Integer.parseInt(sequence.toString())))
                             .or(lp.concat(expr).concat(rp));

        Parser term = factor.concat(mul.or(div).concat(factor)
                            .onSuccess(sequence ->
                            {
                                int rhs = stack.pop();
                                int lhs = stack.pop();
                                switch (sequence.charAt(0))
                                {
                                    case '*' -> stack.push(lhs * rhs);
                                    case '/' -> stack.push(lhs / rhs);
                                }
                            })
                            .zeroOrMore());

        expr.set(term.concat(add.or(sub).concat(term)
                          .onSuccess(sequence ->
                          {
                              int rhs = stack.pop();
                              int lhs = stack.pop();
                              switch (sequence.charAt(0))
                              {
                                  case '+' -> stack.push(lhs + rhs);
                                  case '-' -> stack.push(lhs - rhs);
                              }
                          })
                         .zeroOrMore()));

        Parser p = expr.end();

        ParseResult result;

        stack.clear();
        result = p.parse("2");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());
        assertEquals(2, (int)stack.pop());

        stack.clear();
        result = p.parse("2*3");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());
        assertEquals(6, (int)stack.pop());

        stack.clear();
        result = p.parse("9/3");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());
        assertEquals(3, (int)stack.pop());

        stack.clear();
        result = p.parse("7+8");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());
        assertEquals(15, (int)stack.pop());

        stack.clear();
        result = p.parse("2+3*4");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());
        assertEquals(14, (int)stack.pop());

        stack.clear();
        result = p.parse("3*5+7");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());
        assertEquals(22, (int)stack.pop());

        stack.clear();
        result = p.parse("(3)");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());
        assertEquals(3, (int)stack.pop());

        stack.clear();
        result = p.parse("((5))");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());
        assertEquals(5, (int)stack.pop());

        stack.clear();
        result = p.parse("(1+2)");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());
        assertEquals(3, (int)stack.pop());

        stack.clear();
        result = p.parse("(2+3)*4");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());
        assertEquals(20, (int)stack.pop());

        stack.clear();
        result = p.parse("2+(3*4)");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());
        assertEquals(14, (int)stack.pop());

        stack.clear();
        result = p.parse("(2+3)*(4+5)");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());
        assertEquals(45, (int)stack.pop());

        stack.clear();
        result = p.parse("2+3*4+5");
        assertTrue(result.isSuccess());
        assertEquals("", result.remain().toString());
        assertEquals(19, (int)stack.pop());
    }
}
