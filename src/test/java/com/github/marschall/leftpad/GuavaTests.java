package com.github.marschall.leftpad;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.google.common.base.Strings;

class GuavaTests {
  
  @Test
  void padStart() {
    assertEquals("00123", Strings.padStart("123", 5, '0'));
  }

}
