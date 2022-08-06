package com.github.marschall.leftpad;

import static com.github.marschall.leftpad.CharSequenceUtils.leftPad;
import static com.github.marschall.leftpad.CharSequenceUtils.leftPadInto;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;

class CharSequenceUtilsTests {

  @Test
  void testHashCode() {
    assertEquals("00123".hashCode(), leftPad("123", 5, '0').hashCode());
    assertEquals("123".hashCode(), leftPad("123", 3, '0').hashCode());
    assertEquals("".hashCode(), leftPad("", 0, '0').hashCode());
    assertEquals("000".hashCode(), leftPad("", 3, '0').hashCode());
  }

  @Test
  void testEquals() {
    assertEquals(leftPad("123", 5, '0'), "00123");
    assertEquals(leftPad("123", 3, '0'), "123");
    assertEquals(leftPad("", 0, '0'), "");
    assertEquals(leftPad("", 3, '0'), "000");

    CharSequence padded = leftPad("123", 5, '0');
    assertEquals(padded, padded);
    assertNotEquals(padded, padded.subSequence(1, 5));

    assertNotEquals("00123", leftPad("123", 5, '0'));
    assertEquals("00123", leftPad("123", 5, '0').toString());
    assertEquals(leftPad("123", 5, '0'), "00123");
    assertNotEquals(leftPad("123", 5, '0'), Integer.valueOf(123));
    assertNotEquals(leftPad("123", 5, '0'), "00122");
    assertNotEquals(leftPad("123", 5, '0'), "0O123");
    assertNotEquals(leftPad("123", 5, '0'), leftPad("1234", 5, '0'));
    assertNotEquals(leftPad("1", 3, '0'), leftPad("1", 4, '0').subSequence(0, 3));
  }

  @Test
  void testChars() {
    assertArrayEquals(new int[]{'0', '0', '1', '2', '3'}, leftPad("123", 5, '0').chars().toArray());
    assertArrayEquals(new int[]{'0', '0'}, leftPad("123", 5, '0').subSequence(0, 2).chars().toArray());
  }

  @Test
  void testIsEmpty() {
    assertFalse(leftPad("123", 5, '0').isEmpty());
    assertFalse(leftPad("123", 5, '0').subSequence(0, 2).isEmpty());
  }
  
  @Test
  void testLength() {
    assertEquals(5, leftPad("123", 5, '0').length());
    assertEquals(2, leftPad("123", 5, '0').subSequence(0, 2).length());
  }

  @Test
  void testSubSequence() {
    assertEquals(leftPad("123", 5, '0').subSequence(0, 0), "");
    assertEquals(leftPad("123", 5, '0').subSequence(0, 2), "00");
    assertEquals(leftPad("123", 5, '0').subSequence(2, 5), "123");
    assertEquals(leftPad("123", 5, '0').subSequence(1, 4), "012");
    assertEquals(leftPad("123", 5, '0').subSequence(1, 5), "0123");
    assertEquals(leftPad("123", 5, '0').subSequence(0, 5), "00123");
    assertNotEquals(leftPad("123", 5, '0').subSequence(0, 1), Integer.valueOf(0));

    CharSequence repeating = leftPad("123", 5, '0').subSequence(0, 2);
    assertEquals(repeating, repeating);
    assertNotEquals(repeating, repeating.subSequence(0, 1));
  }
  
  @Test
  void testSubSequenceEquals() {
    CharSequence sequence1 = leftPad("123", 6, '0'); // 000123
    CharSequence sequence2 = leftPad("124", 5, '0'); // 00124
    assertEquals(sequence1.subSequence(1, 5), sequence2.subSequence(0, 4));
  }

  @Test
  void testPrefixSubSequence() {
    assertEquals("00", leftPad("123", 5, '0').subSequence(0, 2).toString());
    assertEquals("0", leftPad("123", 5, '0').subSequence(0, 2).subSequence(1, 2).toString());
    assertEquals("00".hashCode(), leftPad("123", 5, '0').subSequence(0, 2).hashCode());
    assertEquals(2, leftPad("123", 5, '0').subSequence(0, 2).length());
  }

  @Test
  void testEqualsCombined() {
    assertEquals(leftPad(leftPad("123", 5, '0'), 7, ' '), "  00123");
  }

  @Test
  void testContentEquals() {
    assertTrue("00123".contentEquals(leftPad("123", 5, '0')));
  }

  @Test
  void testIllegalArguments() {
    assertThrows(IllegalArgumentException.class, () -> leftPad("123", 2, '0'));
    assertThrows(IllegalArgumentException.class, () -> leftPad("123", -5, '0'));

    assertThrows(IllegalArgumentException.class, () -> leftPad("123", 5, "😞".charAt(0)));
    assertThrows(IllegalArgumentException.class, () -> leftPad("123", 5, "😞".charAt(1)));
  }

  @Test
  void testIndexOutOfBounds() {
    CharSequence padded = leftPad("123", 5, '0');
    assertThrows(IndexOutOfBoundsException.class, () -> padded.charAt(-1));
    assertThrows(IndexOutOfBoundsException.class, () -> padded.charAt(5));
  }

  @Test
  void testPrefixIndexOutOfBounds() {
    CharSequence prefix = leftPad("123", 5, '0').subSequence(0, 2);
    assertThrows(IndexOutOfBoundsException.class, () -> prefix.charAt(-1));
    assertThrows(IndexOutOfBoundsException.class, () -> prefix.charAt(2));
  }

  @Test
  void testPrefixCharAt() {
    CharSequence prefix = leftPad("123", 5, '0').subSequence(0, 2);
    assertEquals('0', prefix.charAt(0));
    assertEquals('0', prefix.charAt(1));
  }

  @Test
  void testNull() {
    assertThrows(NullPointerException.class, () -> leftPad(null, 2, '0'));
  }

  @Test
  void testLeftPadInto() throws IOException {
    StringBuilder buffer = new StringBuilder();
    leftPadInto("123", 5, '0', buffer);
    assertEquals("00123", buffer.toString());
  }

  @Test
  void testLeftPadIntoIllegalArguments() throws IOException {
    StringBuilder buffer = new StringBuilder();

    assertThrows(IllegalArgumentException.class, () -> leftPadInto("123", 2, '0', buffer));
    assertThrows(IllegalArgumentException.class, () -> leftPadInto("123", -5, '0', buffer));

    assertThrows(IllegalArgumentException.class, () -> leftPadInto("123", 5, "😞".charAt(0), buffer));
    assertThrows(IllegalArgumentException.class, () -> leftPadInto("123", 5, "😞".charAt(1), buffer));
  }

}
