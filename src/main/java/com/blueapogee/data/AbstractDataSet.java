package com.blueapogee.data;


import java.util.*;

public abstract class AbstractDataSet implements DataSet {

  private int rowCount = 0;
  private int columnCount = 0;
  private final Map<Integer, DataValueType> columnTypes = new HashMap<>();
  private final Map<Integer, DataRow> rowValues = new HashMap<>();
  private final Map<Integer, Integer> columnFillCount = new HashMap<>();


  @Override
  public boolean isFillValue(Object value, int fromColumn) {
    return value.equals(columnTypes.get(fromColumn).getFill());
  }


  public void describe() {

  }

  @Override
  public void addRow(final DataRow dataRow) {

    if(dataRow.getValues().size() != columnTypes.size()) {
      throw new IllegalArgumentException("Values and columns size must be the same size");
    }

    int columnIndex = 0;
    for(DataValue value : dataRow.getValues()) {
      if(!columnTypes.get(columnIndex).getFill().getClass().getSimpleName().equalsIgnoreCase(value.getData().getClass().getSimpleName())) {
        throw new IllegalArgumentException("Column " + columnIndex + " type not what it should be ");
      }
      columnIndex++;
    }
    rowValues.put(rowCount, dataRow);
    rowCount++;
  }

  public void addColumn(DataValueType type) {
    columnTypes.put(columnCount, type);
    columnCount++;
  }

  public void setColumnTypes(DataValueType[] types) {
    for(DataValueType type : types) {
      addColumn(type);
    }
  }

  @Override
  public void addColumns(final List<DataValueType> columns) {
    columns.forEach(this::addColumn);
  }

  public DataValueType getColumn(int index) {
    return columnTypes.get(index);
  }

  @Override
  public int getColumnCount() {
    return columnCount;
  }

  @Override
  public int getRowCount() {
    return rowCount;
  }

  @Override
  public void processRow(final String dataRowStr) {
    DataRow dataRow = new DataRow();
    StringTokenizer tokenizer = new StringTokenizer(dataRowStr);

    if(getColumnCount() != tokenizer.countTokens()) {
      throw new IllegalArgumentException("String data tokens not equal to column count");
    }

    int col = 0;
    while(tokenizer.hasMoreTokens()) {
      String token = tokenizer.nextToken();
      Class type = getColumn(col).getType();
      DataValue val = null;

      if(type.equals(Integer.class)) {
        int value = Integer.parseInt(token);
        val = new DataValue<>(value);
        setFillCounts(col, value);
      } else if(type.equals(Double.class)) {
        double value = Double.parseDouble(token);
        val = new DataValue<>(value);
        setFillCounts(col, value);
      } else if(type.equals(String.class)) {
        val = new DataValue<>(token);
        setFillCounts(col, token);
      }
      dataRow.addDataValue(val);
      col++;
    }
    addRow(dataRow);
  }

  @Override
  public Collection<DataRow> getDataRows() {
    return rowValues.values();
  }

  private void setFillCounts(int columnIndex, Object parsedValue) {
    if(getColumn(columnIndex).getFill().equals(parsedValue)) {
      if(columnFillCount.containsKey(columnIndex)) {
        columnFillCount.put(columnIndex, columnFillCount.get(columnIndex) + 1);
      }
      else {
        columnFillCount.put(columnIndex, 1);
      }
    }
  }

}
