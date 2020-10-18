package pers.byx.parser.test;

import org.junit.Test;
import pers.byx.parser.core.CharSequence;
import pers.byx.parser.core.Sequence;

import static org.junit.Assert.*;

public class SequenceTest
{
    @Test
    public void testCharSequence()
    {
        Sequence sequence = new CharSequence("abc123");
        assertEquals(sequence.length(), 6);
        assertEquals(sequence.charAt(0), 'a');
        assertEquals(sequence.charAt(3), '1');
        assertEquals(sequence.charAt(5), '3');

        Sequence subSequence = sequence.subSequence(2, 4);
        assertEquals(subSequence.length(), 2);
        assertEquals(subSequence.charAt(0), 'c');
        assertEquals(subSequence.charAt(1), '1');

        subSequence = subSequence.subSequence(1, 2);
        assertEquals(subSequence.length(), 1);
        assertEquals(subSequence.charAt(0), '1');
    }
}
