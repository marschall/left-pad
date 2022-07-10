package com.github.marschall.leftpad;

import org.junit.jupiter.api.Test;
import org.openjdk.jol.info.ClassLayout;

import com.github.marschall.leftpad.CharSequenceUtils.PrefixCharSequence;

class ObjectLayoutTests {


  @Test
  void prefixCharSequence() {
    System.out.println(ClassLayout.parseClass(PrefixCharSequence.class).toPrintable());
  }

  @Test
  void stringClass() {
    System.out.println(ClassLayout.parseClass(String.class).toPrintable());
//    System.out.println(ClassLayout.parseInstance("1234").toPrintable());
  }

}
