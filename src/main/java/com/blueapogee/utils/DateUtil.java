package com.blueapogee.utils;


import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


public class DateUtil {

  public static boolean isLeapYear(int year) {
    boolean isLeapYear;
    // divisible by 4
    isLeapYear = (year % 4 == 0);
    // divisible by 4 and not 100
    isLeapYear = isLeapYear && (year % 100 != 0);
    // divisible by 4 and not 100 unless divisible by 400
    isLeapYear = isLeapYear || (year % 400 == 0);
    return isLeapYear;
  }


  public static LocalDateTime convertDecimalDateToDate(double decimalDate) {
    int year = (int) decimalDate;
    double reminder = decimalDate - year;
    int daysPerYear = DateUtil.isLeapYear(year) ? 366 : 365;
    long milliseconds = (long)(reminder * daysPerYear * 24 * 60 * 60 * 1000);
    LocalDateTime yearDate = LocalDateTime.of(year, 1 ,1, 0, 0);
    return yearDate.plus(milliseconds, ChronoUnit.MILLIS);
  }


}
