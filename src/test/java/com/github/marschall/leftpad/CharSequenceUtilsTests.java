package com.github.marschall.leftpad;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;

class CharSequenceUtilsTests {

  @Test
  void testHashCode() {
    assertEquals(CharSequenceUtils.leftPad("123", '0', 5).hashCode(), "00123".hashCode());
    assertEquals(CharSequenceUtils.leftPad("123", '0', 3).hashCode(), "123".hashCode());
    assertEquals(CharSequenceUtils.leftPad("", '0', 0).hashCode(), "".hashCode());
  }

  @Test
  void testEquals() {
    assertEquals(CharSequenceUtils.leftPad("123", '0', 5), "00123");
    assertEquals(CharSequenceUtils.leftPad("123", '0', 3), "123");
    assertEquals(CharSequenceUtils.leftPad("", '0', 0), "");

    assertNotEquals("00123", CharSequenceUtils.leftPad("123", '0', 5));
    assertEquals(CharSequenceUtils.leftPad("123", '0', 5), "00123");
  }

  @Test
  void testContentEquals() {
    assertTrue("00123".contentEquals(CharSequenceUtils.leftPad("123", '0', 5)));
  }

  @Test
  void testIllegalArguments() {
    assertThrows(IllegalArgumentException.class, () -> CharSequenceUtils.leftPad("123", "😞".charAt(0), 5));
    assertThrows(IllegalArgumentException.class, () -> CharSequenceUtils.leftPad("123", "😞".charAt(1), 5));
  }

  @Test
  void testNull() {
    assertThrows(NullPointerException.class, () -> CharSequenceUtils.leftPad(null, '0', 2));
  }

  @Test
  void leftPadInto() throws IOException {
    StringBuilder buffer = new StringBuilder();
    CharSequenceUtils.leftPadInto("123", '0', 5, buffer);
    assertEquals("00123", buffer.toString());
  }

}
