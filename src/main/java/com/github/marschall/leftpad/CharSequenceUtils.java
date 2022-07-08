package com.github.marschall.leftpad;

import java.util.Objects;
import java.util.stream.IntStream;

public final class CharSequenceUtils {

  private CharSequenceUtils() {
    throw new AssertionError("not instantiable");
  }

  public static CharSequence leftPad(CharSequence s, char padding, int length) {
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
    return new PrefixCharSequence(padding, length - s.length(), s);
  }

  static final class PrefixCharSequence implements CharSequence, Comparable<CharSequence> {

    private final char padding;

    private final int padLength;

    private final CharSequence tail;

    PrefixCharSequence(char padding, int padLength, CharSequence tail) {
      if (padLength <= 0) {
        throw new IllegalArgumentException();
      }
      Objects.requireNonNull(tail, "tail");
      this.padding = padding;
      this.padLength = padLength;
      this.tail = tail;
    }

    @Override
    public int length() {
      return this.padLength + this.tail.length();
    }

    @Override
    public char charAt(int index) {
      if (index < 0) {
        throw new IndexOutOfBoundsException();
      }
      if (index < this.padLength) {
        return this.padding;
      }
      return this.tail.charAt(index - this.padLength);
    }

    @Override
    public boolean isEmpty() {
      return false;
    }

    @Override
    public CharSequence subSequence(int start, int end) {
      Objects.checkFromToIndex(end, end, this.length());
      if (start == end) {
        return "";
      }
      if (start >= this.padLength) {
        return this.tail.subSequence(start - this.padLength, end - this.padLength);
      }
      if (end <= this.padLength) {
        // FIXME
      }
      return new PrefixCharSequence(this.padding, this.padLength - start, this.tail.subSequence(0, end - this.padding));
    }

    @Override
    public IntStream chars() {
      return IntStream.concat(IntStream.range(0, this.padLength).map(x -> this.padding), this.tail.chars());
    }

    @Override
    public String toString() {
      StringBuilder buffer = new StringBuilder(this.padLength + this.tail.length());
      for (int i = 0; i < this.padLength; i++) {
        buffer.append(this.padding);
      }
      buffer.append(this.tail);
      return buffer.toString();
    }

    @Override
    public int hashCode() {
      int h = 0;
      for (int i = 0; i < this.padLength; i++) {
        h = (31 * h) + this.padding;
      }
      for (int i = this.padLength; i < this.length(); i++) {
        h = (31 * h) + this.tail.charAt(i - this.padLength);
      }
//      int prefixHash = 0;
//      for (int i = 0; i < this.padLength; i++) {
//        prefixHash = (31 * prefixHash) + this.padding;
//      }
//      int suffixHash = this.tail.hashCode();
      return h;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == this) {
        return true;
      }
      if (obj instanceof CharSequence c) {
        if (c.length() != this.length()) {
          return false;
        }
        if (c instanceof String s) {
          return s.contentEquals(this);
        } else {
          for (int i = 0; i < this.padLength; i++) {
            if (c.charAt(i) != this.padding) {
              return false;
            }
          }
          for (int i = this.padLength; i < this.length(); i++) {
            if (c.charAt(i) != this.tail.charAt(i - this.padding)) {
              return false;
            }
          }
          return true;
        }
      } else {
        return false;
      }
    }

    @Override
    public int compareTo(CharSequence o) {
      for (int i = 0, len = Math.min(this.length(), o.length()); i < len; i++) {
        char a = this.charAt(i);
        char b = o.charAt(i);
        if (a != b) {
          return a - b;
        }
      }
      return this.length() - o.length();
    }

  }

}
