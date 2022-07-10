package com.github.marschall.leftpad;

import static com.github.marschall.leftpad.StringUtils.leftPad;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class StringUtilsTests {

  @Test
  void testLeftPad() {
    assertEquals("00123", leftPad("123", '0', 5));
    assertEquals("123", leftPad("123", '0', 3));
    assertEquals("", leftPad("", '0', 0));
  }

  @Test
  void testNull() {
    assertThrows(NullPointerException.class, () -> leftPad(null, '0', 2));
  }

  @Test
  void testIllegalArguments() {
    assertThrows(IllegalArgumentException.class, () -> leftPad("123", '0', 2));
    assertThrows(IllegalArgumentException.class, () -> leftPad("123", '0', -5));

    assertThrows(IllegalArgumentException.class, () -> leftPad("123", "😞".charAt(0), 5));
    assertThrows(IllegalArgumentException.class, () -> leftPad("123", "😞".charAt(1), 5));
  }

}
