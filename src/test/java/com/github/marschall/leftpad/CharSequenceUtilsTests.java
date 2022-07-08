package com.github.marschall.leftpad;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class CharSequenceUtilsTests {

  @Test
  void testHashCode() {
    assertEquals(CharSequenceUtils.leftPad("123", '0', 5), "00123");
    assertEquals(CharSequenceUtils.leftPad("123", '0', 3), "123");
    assertEquals(CharSequenceUtils.leftPad("", '0', 0), "");
  }

  @Test
  void testEquals() {
    assertNotEquals("00123", CharSequenceUtils.leftPad("123", '0', 5));
    assertEquals(CharSequenceUtils.leftPad("123", '0', 5), "00123");
  }

  @Test
  void testContentEquals() {
    assertTrue("00123".contentEquals(CharSequenceUtils.leftPad("123", '0', 5)));
  }

}
