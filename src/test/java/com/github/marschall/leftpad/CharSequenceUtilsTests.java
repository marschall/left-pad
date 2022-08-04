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
    assertEquals(leftPad("123", 5, '0'), "00123");
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
