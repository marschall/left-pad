package com.github.marschall.leftpad;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class CharSequenceUtilsTests {

  @Test
  void testHashCode() {
    assertEquals("00123".hashCode(), CharSequenceUtils.leftPad("123", '0', 5).hashCode());
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
