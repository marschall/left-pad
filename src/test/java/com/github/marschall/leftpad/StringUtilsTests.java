package com.github.marschall.leftpad;

import static com.github.marschall.leftpad.StringUtils.leftPad;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class StringUtilsTests {

  @Test
  void testLeftPad() {
    assertEquals("00123", leftPad("123", 5, '0'));
    assertEquals("123", leftPad("123", 3, '0'));
    assertEquals("", leftPad("", 0, '0'));
  }

  @Test
  void testNull() {
    assertThrows(NullPointerException.class, () -> leftPad(null, 2, '0'));
  }

  @Test
  void testIllegalArguments() {
    assertThrows(IllegalArgumentException.class, () -> leftPad("123", 2, '0'));
    assertThrows(IllegalArgumentException.class, () -> leftPad("123", -5, '0'));

    assertThrows(IllegalArgumentException.class, () -> leftPad("123", 5, "😞".charAt(0)));
    assertThrows(IllegalArgumentException.class, () -> leftPad("123", 5, "😞".charAt(1)));
  }

}
