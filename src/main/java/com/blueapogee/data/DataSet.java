package com.blueapogee.data;


import java.util.Collection;
import java.util.List;

public interface DataSet {
  void addColumns(List<DataValueType> columns);
  void addRow(DataRow dataRow);
  void processRow(String dataRow);
  Collection<DataRow> getDataRows();
  int getColumnCount();
  int getRowCount();
  boolean isFillValue(Object value, int fromColumn);
}
