package com.blueapogee.data;


public class DataValueType<T>  {
  final private T fill;
  final private String name;
  final private String comment;

  public DataValueType(final String name, final String comment, final T fill) {
    this.name = name;
    this.comment = comment;
    this.fill = fill;
  }

  public Class getType() {
    return fill.getClass();
  }

  public T getFill() {
    return fill;
  }

  public String getName() {
    return name;
  }

  public String getComment() {
    return comment;
  }

}
