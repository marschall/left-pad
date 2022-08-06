package com.github.marschall.leftpad;

import java.io.IOException;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * Utility methods for dealing with {@link CharSequence}.
 */
public final class CharSequenceUtils {

  private CharSequenceUtils() {
    throw new AssertionError("not instantiable");
  }

  /**
   * Pads a given {@link CharSequence} with the given {@code char} from the left/start.
   * 
   * @param s the {@link CharSequence} to pad, not {@code null}
   * @param length the desired total length in code units
   * @param padding the padding character to apply
   * @return a {@link CharSequence} of length {@code length} padded with {@code padding}
   *         applied to the start of {@code s},
   *         no guarantees are made about the implementation class
   * @throws NullPointerException if {@code s} is {@code null}
   * @throws IllegalArgumentException if {@code length} is negative,
   *                                  if {@code padding} is a surrogate character,
   *                                  if {@code length} is less than the length of {@code s}
   * @see Character#isSurrogate(char)
   */
  public static CharSequence leftPad(CharSequence s, int length, char padding) {
    Objects.requireNonNull(s);
    if ((length < 0) || (length < s.length())) {
      throw new IllegalArgumentException();
    }
    if (Character.isSurrogate(padding)) {
      throw new IllegalArgumentException();
    }
    if (s.isEmpty()) {
      if (length == 0) {
        return "";
      }
      return new RepeatingCharSequence(padding, length);
    }
    if (length == s.length()) {
      return s;
    }
    return new PrefixCharSequence(padding, length - s.length(), s);
  }

  /**
   * Pads a given {@link CharSequence} with the given {@code char} from the left/start
   * into a target {@link Appendable}.
   * 
   * @param s the {@link CharSequence} to pad, not {@code null}
   * @param length the desired total length in code units
   * @param padding the padding character to apply
   * @param target the target {@link Appendable} into which to pad, not {@code null}
   * @throws NullPointerException if {@code s} is {@code null},
   *                              if {@code target} is {@code null}
   * @throws IllegalArgumentException if {@code length} is negative,
   *                                  if {@code padding} is a surrogate character,
   *                                  if {@code length} is less than the length of {@code s}
   * @see Character#isSurrogate(char)
   */
  public static void leftPadInto(CharSequence s, int length, char padding, Appendable target) throws IOException {
    Objects.requireNonNull(s);
    Objects.requireNonNull(target);
    if ((length < 0) || (length < s.length())) {
      throw new IllegalArgumentException();
    }
    if (Character.isSurrogate(padding)) {
      throw new IllegalArgumentException();
    }
    int padLength = length - s.length();
    for (int i = 0; i < padLength; i++) {
      target.append(padding);
    }
    target.append(s);
  }

  /**
   * A {@link CharSequence} made of a prefix and a suffix.
   * The prefix is a single {@code char} repeated n-times and the suffix is an other {@link CharSequence}.
   */
  static final class PrefixCharSequence implements CharSequence, Comparable<CharSequence> {

    private final char padding;

    // using a short instead would allow the us to move
    // #padding and #padLength into the header
    // however we still have a 4 byte alignment gap for #tail
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
        return new RepeatingCharSequence(this.padding, end - start);
      }
      if (start == 0 && end == this.length()) {
        return this;
      }
      return new PrefixCharSequence(this.padding, this.padLength - start, this.tail.subSequence(0, end - this.padLength));
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
      int prefixHash = 0;
      for (int i = 0; i < this.padLength; i++) {
        prefixHash = (31 * prefixHash) + this.padding;
      }
      for (int i = 0; i < this.tail.length(); i++) {
        prefixHash = 31 * prefixHash;
      }
      int suffixHash = this.tail.hashCode();
      return prefixHash + suffixHash;
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
          if (this.tail instanceof String t) {
            for (int i = 0; i < this.padLength; i++) {
              if (s.charAt(i) != this.padding) {
                return false;
              }
            }
            return s.regionMatches(this.padLength, t, 0, t.length());
          } else {
            return s.contentEquals(this);
          }
        } else {
          for (int i = 0; i < this.padLength; i++) {
            if (c.charAt(i) != this.padding) {
              return false;
            }
          }
          for (int i = this.padLength; i < this.length(); i++) {
            if (c.charAt(i) != this.tail.charAt(i - this.padLength)) {
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

  /**
   * A {@link CharSequence} that is just a single {@code char} repeated n-times.
   */
  static final class RepeatingCharSequence implements CharSequence, Comparable<CharSequence> {

    private final char content;

    // if we specialize this into a short we save 8 bytes on JDK 17+
    // due to a 6 byte object alignment gap
    private final int length;

    RepeatingCharSequence(char content, int length) {
      if (length <= 0) {
        throw new IllegalArgumentException();
      }
      this.content = content;
      this.length = length;
    }

    @Override
    public int length() {
      return this.length;
    }

    @Override
    public char charAt(int index) {
      Objects.checkIndex(index, this.length);
      return this.content;
    }

    @Override
    public boolean isEmpty() {
      return false;
    }

    @Override
    public CharSequence subSequence(int start, int end) {
      Objects.checkFromToIndex(start, end, this.length);
      if (start == end) {
        return "";
      }
      if ((end - start) == this.length) {
        return this;
      }
      return new RepeatingCharSequence(this.content, end - start);
    }

    @Override
    public IntStream chars() {
      return IntStream.range(0, this.length).map(x -> this.content);
    }

    @Override
    public String toString() {
      StringBuilder buffer = new StringBuilder(this.length);
      for (int i = 0; i < this.length; i++) {
        buffer.append(this.content);
      }
      return buffer.toString();
      // alternative implementation
//      return String.valueOf(this.content).repeat(this.length);
    }

    @Override
    public int hashCode() {
      int hash = 0;
      for (int i = 0; i < this.length; i++) {
        hash = (31 * hash) + this.content;
      }
      return hash;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == this) {
        return true;
      }
      if (obj instanceof CharSequence c) {
        if (this.length != c.length()) {
          return false;
        }
        for (int i = 0; i < this.length; i++) {
          if (c.charAt(i) != this.content) {
            return false;
          }
        }
        return true;
      } else {
        return false;
      }
    }

    @Override
    public int compareTo(CharSequence o) {
      for (int i = 0, len = Math.min(this.length, o.length()); i < len; i++) {
        char c = o.charAt(i);
        if (this.content != c) {
          return this.content - c;
        }
      }
      return this.length - o.length();
    }

  }

}
