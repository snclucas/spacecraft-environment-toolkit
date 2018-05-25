package com.blueapogee.data.datasets;


import com.blueapogee.data.AbstractDataSet;
import com.blueapogee.data.DataRow;
import com.blueapogee.data.DataValue;
import com.blueapogee.data.DataValueType;
import com.blueapogee.utils.DateUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SolarWindPlasma <T> extends AbstractDataSet {


  public SolarWindPlasma(final String description, final boolean addDateColumn) {
    super(description, addDateColumn);
    buildDataSet();
  }

  private void buildDataSet() {
    addColumnType(new DataValueType<>("time_tag", "The year", -9999));
    addColumnType(new DataValueType<>("density", "Density", -9999));
    addColumnType(new DataValueType<>("speed", "Speed", -9999));
    addColumnType(new DataValueType<>("temperature", "Temperature", -9999));
  }

  @Override
  public DataValue<LocalDateTime> dateProvider(final DataRow dataRow) {
    LocalDateTime localDateTime = DateUtil.convertDecimalDateToDate((double)dataRow.getValue("DecimalYear").getData());
    return new DataValue<>(localDateTime);
  }

  @Override
  public void processRow(final String dataRowStr) {
    List<SolarWindPlasmaData> solarWindPlasma = new ArrayList<>();
    List[] data = null;
    ObjectMapper mapperObj = new ObjectMapper();
    try {
      data = mapperObj.readValue(dataRowStr, List[].class);
    } catch (IOException e) {
      e.printStackTrace();
    }

    for(List list : data) {
      solarWindPlasma.add(new SolarWindPlasmaData(list));
    }

    String h = "";

  }


  private static class SolarWindPlasmaData {
    private String time_tag;
    private String bx_gsm;
    private String by_gsm;
    private String bz_gsm;
    private String lon_gsm;
    private String lat_gsm;
    private String bt;

    public SolarWindPlasmaData(List data) {
      if(data.size() > 7) {

      }
      this.time_tag = (String)data.get(0);
      this.bx_gsm = (String)data.get(1);
      this.by_gsm = (String)data.get(2);
      this.bz_gsm = (String)data.get(3);
      this.lon_gsm = (String)data.get(4);
      this.lat_gsm = (String)data.get(5);
      this.bt = (String)data.get(6);
    }

  }


}
