package com.github.marschall.leftpad;

import java.util.Objects;

public final class StringUtils {

  private StringUtils() {
    throw new AssertionError("not instantiable");
  }

  public static String leftPad(String s, char padding, int length) {
    Objects.requireNonNull(s);
    if ((length < 0) || (length < s.length())) {
      throw new IllegalArgumentException();
    }
    if (Character.isSurrogate(padding)) {
      throw new IllegalArgumentException();
    }
    if (length == s.length()) {
      return s;
    }
    StringBuilder buffer = new StringBuilder(length);
    int padLength = length - s.length();
    for (int i = 0; i < padLength; i++) {
      buffer.append(padding);
    }
    buffer.append(s);
    return buffer.toString();
  }

}
