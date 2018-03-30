package com.blueapogee.service.outputs;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class GroundStationEventOutput {
  public String date;
  public String event;

  public GroundStationEventOutput(final String date, final String event) {
    this.date = date;
    this.event = event;
  }
}
