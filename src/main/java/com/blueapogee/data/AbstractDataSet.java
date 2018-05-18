package com.blueapogee.data;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;


public abstract class AbstractDataSet implements DataSet {

  private final String description;
  private final boolean addDateColumn;
  private int rowCount = 0;
  private int columnCount = 0;
  private final Map<String, DataValueType> columnTypes;
  private final Map<Integer, DataRow> rowValues;
  private final Map<Integer, Integer> columnFillCount;


  public AbstractDataSet(final String description, final boolean addDateColumn) {
    this.description = description;
    this.addDateColumn = addDateColumn;
    this.columnTypes = new LinkedHashMap<>();
    if(addDateColumn) {
      this.columnTypes.put("Date", new DataValueType<>("Date", "The date", LocalDateTime.MIN));
    }
    this.rowValues = new LinkedHashMap<>();
    this.columnFillCount = new LinkedHashMap<>();
  }

  public void writeJSON() {
    ObjectMapper mapperObj = new ObjectMapper();
    mapperObj.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
    mapperObj.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    try {
      mapperObj.writeValue(new File("D:/dataTwo.json"), rowValues.get(0));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public boolean isFillValue(Object value, int fromColumn) {
    String[] keyArray = columnTypes.keySet().toArray(new String[0]);
    return value.equals(columnTypes.get(keyArray[fromColumn]).getFill());
  }

  @Override
  public String describe() {
    return description;
  }

  @Override
  public DataRow getRow(int index) {
    return rowValues.get(index);
  }

  @Override
  public void addRow(final DataRow dataRow) {
    int initialDateColumn = addDateColumn ? 1 : 0;
    if(dataRow.getValues().size() != (columnTypes.size()) - initialDateColumn) {
      throw new IllegalArgumentException("Values and columns size must be the same size");
    }

    int columnIndex = addDateColumn ? 1 : 0;
    for(DataValue value : dataRow.getValues().values()) {
      if(!getColumn(columnIndex).getFill().getClass().getSimpleName().equalsIgnoreCase(value.getData().getClass().getSimpleName())) {
        throw new IllegalArgumentException("Column " + columnIndex + " type not what it should be ");
      }
      columnIndex++;
    }

    if(addDateColumn) {
      dataRow.addColumn(0, "Date", dateProvider(dataRow));
    }
    rowValues.put(rowCount, dataRow);
    rowCount++;
  }

  protected void addColumn(DataValueType type) {
    columnTypes.put(type.getName(), type);
    columnCount++;
  }

  public void setColumnTypes(DataValueType[] types) {
    for(DataValueType type : types) {
      addColumn(type);
    }
  }

  @Override
  public void addColumns(final List<DataValueType> columns) {
    if(hasDuplicate(columns)){
      throw new IllegalArgumentException("Duplicate column name exists");
    }
    columns.forEach(this::addColumn);
  }

  private static <T> boolean hasDuplicate(Iterable<T> all) {
    Set<T> set = new HashSet<>();
    for (T each: all)
      if (!set.add(each))
        return true;
    return false;
  }

  public DataValueType getColumn(String columnName) {
    return columnTypes.get(columnName);
  }

  private DataValueType getColumn(int columnIndex) {
    String[] keyArray = columnTypes.keySet().toArray(new String[0]);
    return columnTypes.get(keyArray[columnIndex]);
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

    int col = 1;
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

      dataRow.addDataValue(getColumn(col).getName(), val);
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

  public DataValue<LocalDateTime> dateProvider(final DataRow dataRow) {
    return new DataValue<>(LocalDateTime.MIN);
  }

}