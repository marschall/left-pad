package com.github.marschall.leftpad;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class StringUtilsTests {

  @Test
  void leftPad() {
    assertEquals("00123", StringUtils.leftPad("123", '0', 5));
    assertEquals("123", StringUtils.leftPad("123", '0', 3));
    assertEquals("", StringUtils.leftPad("", '0', 0));
  }

  @Test
  void testNull() {
    assertThrows(NullPointerException.class, () -> StringUtils.leftPad(null, '0', 2));
  }

  @Test
  void testIllegalArguments() {
    assertThrows(IllegalArgumentException.class, () -> StringUtils.leftPad("123", '0', 2));
    assertThrows(IllegalArgumentException.class, () -> StringUtils.leftPad("123", '0', -5));

    assertThrows(IllegalArgumentException.class, () -> StringUtils.leftPad("123", "😞".charAt(0), 5));
    assertThrows(IllegalArgumentException.class, () -> StringUtils.leftPad("123", "😞".charAt(1), 5));
  }

}
