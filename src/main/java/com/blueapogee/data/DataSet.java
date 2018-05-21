package com.blueapogee.data;


import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface DataSet {
  void addColumns(List<DataValueType> columns);
  void addRow(DataRow dataRow);
  void processRow(String dataRow);
  Collection<DataRow> getDataRows();
  DataRow getRow(int index);
  int getColumnCount();
  int getRowCount();
  boolean isFillValue(Object value, int fromColumn);
  DataValue<LocalDateTime> dateProvider(DataRow dataRow);
  String describe();
  List<DataValue> getColumn(int columnIndex);
}
