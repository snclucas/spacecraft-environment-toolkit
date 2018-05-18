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

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    final DataValueType<?> that = (DataValueType<?>) o;

    return getName() != null ? getName().equals(that.getName()) : that.getName() == null;
  }

  @Override
  public int hashCode() {
    return getName() != null ? getName().hashCode() : 0;
  }
}
