package com.blueapogee.data.datasets;


import com.blueapogee.data.AbstractDataSet;
import com.blueapogee.data.DataRow;
import com.blueapogee.data.DataValue;
import com.blueapogee.data.DataValueType;
import com.blueapogee.utils.DateUtil;

import java.time.LocalDateTime;

public class sw_msheath_min extends AbstractDataSet {

  public sw_msheath_min(boolean addDateColumn) {
    super("sw_msheath_min", addDateColumn);
    buildDataSet();
  }

  private void buildDataSet() {
    addColumnType(new DataValueType<>("Year", "The year", -9999));
    addColumnType(new DataValueType<>("DOY", "Day of year", -9999));
    addColumnType(new DataValueType<>("Hour", "The year", -9999));
    addColumnType(new DataValueType<>("Minute", "The year", -9999));
    addColumnType(new DataValueType<>("Second", "The year", -9999));
    addColumnType(new DataValueType<>("Spacecraft flag", "The year", -9999));
    addColumnType(new DataValueType<>("DecimalYear", "Decimal year", 9999.99));
    addColumnType(new DataValueType<>("RegionFlag", "The year", -9999));
    addColumnType(new DataValueType<>("Operating mode flag", "The year", -9999));
    addColumnType(new DataValueType<>("X", "The year", 9999.0));
    addColumnType(new DataValueType<>("Y", "The year", 9999.0));
    addColumnType(new DataValueType<>("Z", "The year", 9999.0));
    addColumnType(new DataValueType<>("Operating mode flag 1", "The year", 9999.0));
    addColumnType(new DataValueType<>("Operating mode flag 2", "The year", 9999.0));
    addColumnType(new DataValueType<>("Xx", "The year", 9999.0));
    addColumnType(new DataValueType<>("Yv", "The year", 9999.0));
    addColumnType(new DataValueType<>("Zv", "The year", 9999.0));
    addColumnType(new DataValueType<>("Operating mode flag 3", "The year", 9999.0));
    addColumnType(new DataValueType<>("Operating mode flag 4", "The year", 9999.0));
    addColumnType(new DataValueType<>("Xa", "The year", 9999.0));
    addColumnType(new DataValueType<>("Ya", "The year", 9999.0));
    addColumnType(new DataValueType<>("Za", "The year", 9999.0));
    addColumnType(new DataValueType<>("Operating mode flag 5", "The year", 9999.0));
    addColumnType(new DataValueType<>("Operating mode flag 6", "The year", 9999.0));
  }

  @Override
  public DataValue<LocalDateTime> dateProvider(final DataRow dataRow) {
    //System.out.println(dataRow.toString());
    LocalDateTime localDateTime = DateUtil.convertDecimalDateToDate((double)dataRow.getValue("DecimalYear").getData());
    return new DataValue<>(localDateTime);
  }
}