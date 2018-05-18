package com.blueapogee.data;

import java.util.*;

public class DataRow {

  private final Map<String, DataValue> values = new LinkedHashMap<>();


  void addDataValue(String colName, DataValue dataValue) {
    values.put(colName, dataValue);
  }

  Map<String, DataValue> getValues() {
    return values;
  }

  public DataValue getValue(String columnName) {
    return values.get(columnName);
  }

  void addColumn(int index, String key, DataValue val) {
    add(values, index, key, val);
  }

  @Override
  public String toString() {
    return "DataRow{" + "values=" + values + '}';
  }

  private static void add(Map<String, DataValue> map, int index, String key, DataValue value) {
    assert (map != null);
    assert !map.containsKey(key);
    assert (index >= 0) && (index < map.size());

    int i = 0;
    List<Map.Entry<String, DataValue>> rest = new ArrayList<>();
    for (Map.Entry<String, DataValue> entry : map.entrySet()) {
      if (i++ >= index) {
        rest.add(entry);
      }
    }
    map.put(key, value);
    for (Map.Entry<String, DataValue> entry : rest) {
      map.remove(entry.getKey());
      map.put(entry.getKey(), entry.getValue());
    }
  }
}