package com.blueapogee.data;


public class DataValue<T>  {
  private T data;


  public DataValue(final T data) {
    this.data = data;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

  @Override
  public String toString() {
    return data.toString();
  }
}