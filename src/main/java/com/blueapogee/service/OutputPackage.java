package com.blueapogee.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OutputPackage {
  public List<Map<String, String>> orbit = new ArrayList<>();

  public List<Map<String, String>> passes = new ArrayList<>();

  Map<      String, List<Map<String, String>>       > egg = new HashMap<>();

  public OutputPackage() {

  }

  public void add(String category, String subCategory, Object value) {

    if(egg.containsKey(category)) {
      egg.get(category).add(value);
    }


  }



}
