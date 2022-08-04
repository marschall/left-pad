package com.github.marschall.leftpad;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

class CommonsLangTests {

  @Test
  void leftPad() {
    assertEquals("00123", StringUtils.leftPad("123", 5, '0'));
  }

}
