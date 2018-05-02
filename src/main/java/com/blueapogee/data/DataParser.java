package com.blueapogee.data;


import java.io.*;
import java.net.URL;

public class DataParser {

  public static void processFile(String fileStr, DataSet dataSet) {
    try {
      File file = new File(fileStr);
      BufferedReader br = new BufferedReader(new FileReader(file));
      process(br, dataSet);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void processURl(String urlStr, DataSet dataSet) {
    try {
      URL url = new URL(urlStr);
      BufferedReader in = new BufferedReader(
              new InputStreamReader(url.openStream()));

      process(in, dataSet);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void process(final BufferedReader bufferedReader, final DataSet dataSet) throws IOException {
    String inputLine;
    while ((inputLine = bufferedReader.readLine()) != null) {
      dataSet.processRow(inputLine);
    }
    bufferedReader.close();
  }

}