package com.blueapogee.data;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataRow {

  private final List<DataValue> values = new ArrayList<>();

  public DataRow() {
  }

  public DataRow(DataValue ... valuesIn) {
    values.addAll(Arrays.asList(valuesIn));
  }

  public void addDataValue(DataValue dataValue) {
    values.add(dataValue);
  }

  public List<DataValue> getValues() {
    return values;
  }
}
