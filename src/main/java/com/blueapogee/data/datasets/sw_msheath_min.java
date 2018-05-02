package com.blueapogee.data.datasets;


import com.blueapogee.data.AbstractDataSet;
import com.blueapogee.data.DataValueType;

import java.util.ArrayList;
import java.util.List;

public class sw_msheath_min extends AbstractDataSet {

  public sw_msheath_min() {
    buildDataSet();
  }

  private void buildDataSet() {
    List<DataValueType> dd = new ArrayList<>();

    dd.add(new DataValueType<>("Year", "The year", -9999));
    dd.add(new DataValueType<>("DOY", "Day of year", -9999));
    dd.add(new DataValueType<>("Hour", "The year", -9999));
    dd.add(new DataValueType<>("Minute", "The year", -9999));
    dd.add(new DataValueType<>("Second", "The year", -9999));
    dd.add(new DataValueType<>("Spacecraft flag", "The year", -9999));
    dd.add(new DataValueType<>("DecimalYear", "Decimal year", 9999.99));
    dd.add(new DataValueType<>("RegionFlag", "The year", -9999));
    dd.add(new DataValueType<>("Operating mode flag", "The year", -9999));
    dd.add(new DataValueType<>("X", "The year", 9999.0));
    dd.add(new DataValueType<>("Y", "The year", 9999.0));
    dd.add(new DataValueType<>("Z", "The year", 9999.0));
    dd.add(new DataValueType<>("Operating mode flag", "The year", 9999.0));
    dd.add(new DataValueType<>("Operating mode flag", "The year", 9999.0));
    dd.add(new DataValueType<>("X", "The year", 9999.0));
    dd.add(new DataValueType<>("Y", "The year", 9999.0));
    dd.add(new DataValueType<>("Z", "The year", 9999.0));
    dd.add(new DataValueType<>("Operating mode flag", "The year", 9999.0));
    dd.add(new DataValueType<>("Operating mode flag", "The year", 9999.0));
    dd.add(new DataValueType<>("X", "The year", 9999.0));
    dd.add(new DataValueType<>("Y", "The year", 9999.0));
    dd.add(new DataValueType<>("Z", "The year", 9999.0));
    dd.add(new DataValueType<>("Operating mode flag", "The year", 9999.0));
    dd.add(new DataValueType<>("Operating mode flag", "The year", 9999.0));

    addColumns(dd);

  }

}