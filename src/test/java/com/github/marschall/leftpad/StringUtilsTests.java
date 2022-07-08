package com.github.marschall.leftpad;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class StringUtilsTests {

  @Test
  void leftPad() {
    assertEquals("00123", StringUtils.leftPad("123", '0', 5));
    assertEquals("123", StringUtils.leftPad("123", '0', 3));
    assertEquals("", StringUtils.leftPad("", '0', 0));
  }

}
