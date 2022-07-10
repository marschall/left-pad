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
    assertEquals(leftPad("123", '0', 5).hashCode(), "00123".hashCode());
    assertEquals(leftPad("123", '0', 3).hashCode(), "123".hashCode());
    assertEquals(leftPad("", '0', 0).hashCode(), "".hashCode());
    assertEquals(leftPad("", '0', 3).hashCode(), "000".hashCode());
  }

  @Test
  void testEquals() {
    assertEquals(leftPad("123", '0', 5), "00123");
    assertEquals(leftPad("123", '0', 3), "123");
    assertEquals(leftPad("", '0', 0), "");
    assertEquals(leftPad("", '0', 3), "000");

    assertNotEquals("00123", leftPad("123", '0', 5));
    assertEquals(leftPad("123", '0', 5), "00123");
  }

  @Test
  void testEqualsCombined() {
    assertEquals(leftPad(leftPad("123", '0', 5), ' ', 7), "  00123");
  }

  @Test
  void testContentEquals() {
    assertTrue("00123".contentEquals(leftPad("123", '0', 5)));
  }

  @Test
  void testIllegalArguments() {
    assertThrows(IllegalArgumentException.class, () -> leftPad("123", '0', 2));
    assertThrows(IllegalArgumentException.class, () -> leftPad("123", '0', -5));

    assertThrows(IllegalArgumentException.class, () -> leftPad("123", "😞".charAt(0), 5));
    assertThrows(IllegalArgumentException.class, () -> leftPad("123", "😞".charAt(1), 5));
  }

  @Test
  void testNull() {
    assertThrows(NullPointerException.class, () -> leftPad(null, '0', 2));
  }

  @Test
  void testLeftPadInto() throws IOException {
    StringBuilder buffer = new StringBuilder();
    leftPadInto("123", '0', 5, buffer);
    assertEquals("00123", buffer.toString());
  }

}
