package com.blueapogee.data.datasets;


import com.blueapogee.data.AbstractDataSet;
import com.blueapogee.data.DataRow;
import com.blueapogee.data.DataValue;
import com.blueapogee.data.DataValueType;
import com.blueapogee.utils.DateUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.StringTokenizer;

public class SolarWindPlasma extends AbstractDataSet {


  public SolarWindPlasma(final String description, final boolean addDateColumn) {
    super(description, addDateColumn);
  }

  private void buildDataSet() {
    addColumn(new DataValueType<>("time_tag", "The year", -9999));
    addColumn(new DataValueType<>("density", "Density", -9999));
    addColumn(new DataValueType<>("speed", "Speed", -9999));
    addColumn(new DataValueType<>("temperature", "Temperature", -9999));
  }

  @Override
  public DataValue<LocalDateTime> dateProvider(final DataRow dataRow) {
    LocalDateTime localDateTime = DateUtil.convertDecimalDateToDate((double)dataRow.getValue("DecimalYear").getData());
    return new DataValue<>(localDateTime);
  }

  @Override
  public void processRow(final String dataRowStr) {

    String testData = "\"2018-05-17 14:06:00.000\",\"2.86\",\"-1.14\",\"-6.43\",\"338.19\",\"-64.41\",\"7.16\"";
    ObjectMapper mapperObj = new ObjectMapper();
    try {
      mapperObj.readValue(testData, SolarWindPlasmaData.class);
    } catch (IOException e) {
      e.printStackTrace();
    }


  }


  private static class SolarWindPlasmaData {
    public String time_tag;
    public String density;
    public String speed;
    public String temperature;

    public SolarWindPlasmaData(String data) {
      StringTokenizer tokenizer = new StringTokenizer(data, ",\"");

      tokenizer.nextToken();

    }

  }


}
