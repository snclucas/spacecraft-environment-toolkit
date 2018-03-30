package com.blueapogee.service.outputs;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;


@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class GroundStation {
  public String name;
  public double longitude;
  public double latitude;
  public double altitude;

  public GroundStation(final String name, final double longitude, final double latitude, final double altitude) {
    this.name = name;
    this.longitude = longitude;
    this.latitude = latitude;
    this.altitude = altitude;
  }

  public List<GroundStationEventOutput> events = new ArrayList<>();
}
