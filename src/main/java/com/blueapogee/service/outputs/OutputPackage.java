package com.blueapogee.service.outputs;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class OutputPackage {

  public List<Map<String, String>    > orbit = new ArrayList<>();
  public Map<String, String > errors = new HashMap<>();
  public List<GroundStation> groundStations = new ArrayList<>();

  public OutputPackage() {}

  public Map<String, Object> getPackage() {
    Map<String, Object> output = new HashMap<>();

    if(errors.size()>0) {
      output.put("errors", errors);
    } else {
      output.put("orbit", orbit);
      output.put("groundStations", groundStations);
    }
    return output;
  }


  public void addGroundStationInfo(String name, double longitude, double latitude, double altitude) {
    groundStations.add(new GroundStation(name, longitude, latitude, altitude));
  }

  public void addGroundStationEvent(String name, String date, String event) {
    Optional<GroundStation> station = groundStations.stream().filter(gs -> gs.name.equalsIgnoreCase(name))
            .findAny();
    if(station.isPresent()) {
      GroundStation groundStation = station.get();
      groundStation.events.add(new GroundStationEventOutput(date, event));
    }
  }

  public void addOrbitSample(Map<String, String> value) {
   orbit.add(value);
  }

  public static void main(String[] args) {
    OutputPackage outputPackage = new OutputPackage();

    ObjectMapper mapper = new ObjectMapper();
    try {
      System.out.println(mapper.writeValueAsString(outputPackage.getPackage()));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }

  }

}


