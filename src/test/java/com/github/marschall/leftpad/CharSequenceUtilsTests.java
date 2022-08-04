package com.github.marschall.leftpad;

import static com.github.marschall.leftpad.CharSequenceUtils.leftPad;
import static com.github.marschall.leftpad.CharSequenceUtils.leftPadInto;
import static org.junit.jupiter.api.Assertions.assertEquals;
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

    assertNotEquals("00123", leftPad("123", 5, '0'));
    assertEquals("00123", leftPad("123", 5, '0').toString());
    assertEquals(leftPad("123", 5, '0'), "00123");
  }

  @Test
  void testSubSequence() {
    assertEquals(leftPad("123", 5, '0').subSequence(0, 0), "");
    assertEquals(leftPad("123", 5, '0').subSequence(0, 2), "00");
    assertEquals(leftPad("123", 5, '0').subSequence(2, 5), "123");
    assertEquals(leftPad("123", 5, '0').subSequence(1, 4), "012");
    assertEquals(leftPad("123", 5, '0').subSequence(1, 5), "0123");
    assertEquals(leftPad("123", 5, '0').subSequence(0, 5), "00123");
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
  void testNull() {
    assertThrows(NullPointerException.class, () -> leftPad(null, 2, '0'));
  }

  @Test
  void testLeftPadInto() throws IOException {
    StringBuilder buffer = new StringBuilder();
    leftPadInto("123", 5, '0', buffer);
    assertEquals("00123", buffer.toString());
  }

}
