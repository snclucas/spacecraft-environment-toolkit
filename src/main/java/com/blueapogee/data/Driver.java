package com.blueapogee.data;


import com.blueapogee.data.datasets.SolarWindPlasma;
import com.blueapogee.data.datasets.sw_msheath_min;

public class Driver {

  private Driver() {
    test();
  }

  private void test() {

    SolarWindPlasma dataSet = new SolarWindPlasma("SolarWindPlasma", true);

    String url = "http://services.swpc.noaa.gov/products/solar-wind/mag-1-day.json";

    DataParser.processURL(url, dataSet);

    //System.out.println( dataSet.dateProvider(dataSet.getRow(0))     );
    //System.out.println( dataSet.getRow(0)     );
    //dataSet.writeJSON();

    //dataSet.sumColumns(1, 2);

    String h = "";

   // SolarWindPlasma swPlasma = new SolarWindPlasma("", false);
   // swPlasma.processRow("");

  }

  public static void main(String[] args) {
    new Driver();
  }

}
