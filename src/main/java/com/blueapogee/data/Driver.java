package com.blueapogee.data;


import com.blueapogee.data.datasets.SolarWindPlasma;
import com.blueapogee.data.datasets.sw_msheath_min;

public class Driver {

  private Driver() {
    test();
  }

  private void test() {
    sw_msheath_min dataSet = new sw_msheath_min(true);

    dataSet.processRow("1973 304 20 10 27 8  1973.8324400 3 1   -7.50   16.39   15.17   18.52   12.48  339.9  342.1  103.0   85.4    5.0    4.6   12.9  -14.8   17.6  -17.3");

    String url = "ftp://spdf.gsfc.nasa.gov/pub/data/imp/imp8/plasma_mit/sw_msheath_min/1973_imp8_mit_min_final.asc";

    DataParser.processURL(url, dataSet);

    System.out.println( dataSet.dateProvider(dataSet.getRow(0))     );
    System.out.println( dataSet.getRow(0)     );
    dataSet.writeJSON();

    String h = "";

   // SolarWindPlasma swPlasma = new SolarWindPlasma("", false);
   // swPlasma.processRow("");

  }

  public static void main(String[] args) {
    new Driver();
  }

}
