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

  private final Map<Integer, Integer> columnFillCount;
  private final Map<Integer, List<DataValue>> columns;

  public AbstractDataSet(final String description, final boolean addDateColumn) {
    this.description = description;
    this.addDateColumn = addDateColumn;
    this.columnTypes = new LinkedHashMap<>();
    this.columns = new LinkedHashMap<>();
    if(addDateColumn) {
      this.columnTypes.put("Date", new DataValueType<>("Date", "The date", LocalDateTime.MIN));
      columns.put(0, new LinkedList<>());
      columnCount++;
    }
    // this.rowValues = new LinkedHashMap<>();
    this.columnFillCount = new LinkedHashMap<>();
  }

  public void writeJSON() {
    ObjectMapper mapperObj = new ObjectMapper();
    mapperObj.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
    mapperObj.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    try {
      mapperObj.writeValue(new File("D:/dataTwo.json"), getRow(0));
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
    DataRow dr = new DataRow();

    for(int i = 0;i< columns.entrySet().size();i++) {
      List<DataValue> dvl = columns.get(i);
      dr.addDataValue(getColumnType(i).getName(), dvl.get(index));
    }
    return dr;
  }

  @Override
  public void addRow(final DataRow dataRow) {

    if(addDateColumn) {
      getColumn(0).add(dateProvider(dataRow));
    }

    int initialDateColumn = addDateColumn ? 1 : 0;
    if(dataRow.getValues().size() != (columnTypes.size()) - initialDateColumn) {
      throw new IllegalArgumentException("Values and columns size must be the same size");
    }

    int columnIndex = addDateColumn ? 1 : 0;
    for(DataValue value : dataRow.getValues().values()) {
      if(!getColumnType(columnIndex).getFill().getClass().getSimpleName().equalsIgnoreCase(value.getData().getClass().getSimpleName())) {
        throw new IllegalArgumentException("Column " + columnIndex + " type not what it should be ");
      }
      getColumn(columnIndex).add(value);
      columnIndex++;
    }
    rowCount++;
  }

  protected void addColumn(DataValueType type) {

  }

  protected void addColumnType(DataValueType type) {
    columnTypes.put(type.getName(), type);
    columns.put(columnCount, new LinkedList<>());
    columnCount++;
  }

  public void setColumnTypes(DataValueType[] types) {
    for(DataValueType type : types) {
      addColumnType(type);
    }
  }

  @Override
  public void addColumns(final List<DataValueType> columns) {
    if(hasDuplicate(columns)){
      throw new IllegalArgumentException("Duplicate column name exists");
    }
    columns.forEach(this::addColumnType);
  }

  private static <T> boolean hasDuplicate(Iterable<T> all) {
    Set<T> set = new HashSet<>();
    for (T each: all)
      if (!set.add(each))
        return true;
    return false;
  }

  public DataValueType getColumnType(String columnName) {
    return columnTypes.get(columnName);
  }

  private DataValueType getColumnType(int columnIndex) {
    String[] keyArray = columnTypes.keySet().toArray(new String[0]);
    return columnTypes.get(keyArray[columnIndex]);
  }

  public List<DataValue> getColumn(int columnIndex) {
    return columns.get(columnIndex);
  }

  public void getColumnData() {

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

    if(getColumnCount() != (tokenizer.countTokens() + (addDateColumn ? 1 : 0))) {
      throw new IllegalArgumentException("String data tokens not equal to column count");
    }

    int col = 1;
    while(tokenizer.hasMoreTokens()) {
      String token = tokenizer.nextToken();
      Class type = getColumnType(col).getType();
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

      dataRow.addDataValue(getColumnType(col).getName(), val);
      col++;
    }
    addRow(dataRow);
  }

  @Override
  public Collection<DataRow> getDataRows() {
    List<DataRow> rows = new LinkedList<>();
    for(int i = 0; i<getRowCount();i++) {
      rows.add(getRow(i));
    }
    return rows;
  }

  private void setFillCounts(int columnIndex, Object parsedValue) {
    if(getColumnType(columnIndex).getFill().equals(parsedValue)) {
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


  public List<DataValue> sumColumns(int ... columnIndexes) {
    List<DataValue> values = new ArrayList<>();


    for(int row = 0; row < getRowCount(); row++) {
      Double sum = new Double("0");
      for (int colIndex = 0; colIndex < columnIndexes.length; colIndex++) {
        if (!columnIsNumber(columnIndexes[colIndex])) {
          throw new IllegalArgumentException("Trying to add non-number column, colIndex: " + colIndex);
        }
        Number tmp = ((Number) getColumn(columnIndexes[colIndex]).get(row).getData());
        sum += tmp.doubleValue();
      }
      values.add(new DataValue<>(sum));
    }
    return values;
  }



  private boolean columnIsNumber(int columnIndex) {
    Class classType = getColumnType(columnIndex).getType();
    return Number.class.isAssignableFrom(classType) || NumberValue.class.isAssignableFrom(classType);
  }




}