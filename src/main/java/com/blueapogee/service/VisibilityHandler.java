package com.blueapogee.service;

import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.events.ElevationDetector;
import org.orekit.propagation.events.handlers.EventHandler;


public class VisibilityHandler implements EventHandler<ElevationDetector> {

  private final String stationName;
  private final OutputPackage trace;

  VisibilityHandler(final String stationName, final OutputPackage trace) {
    this.stationName = stationName;
    this.trace = trace;
  }

  public Action eventOccurred(final SpacecraftState s, final ElevationDetector detector, final boolean increasing) {

    if (increasing) {
      trace.addGroundStationEvent(stationName, s.getDate().toString(), "Visible");
      return Action.CONTINUE;
    } else {
      trace.addGroundStationEvent(stationName, s.getDate().toString(), "Visiblity ends");
      return Action.STOP;
    }
  }

  public SpacecraftState resetState(final ElevationDetector detector, final SpacecraftState oldState) {
    return oldState;
  }

}