package com.blueapogee.service;

import com.blueapogee.service.outputs.OutputPackage;
import com.blueapogee.service.parameters.OutputParameters;
import com.blueapogee.service.util.OrbitUtils;
import org.orekit.orbits.KeplerianOrbit;
import org.orekit.orbits.OrbitType;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.sampling.OrekitFixedStepHandler;


public class StepHandler implements OrekitFixedStepHandler {

  private final OutputPackage trace;
  private final OutputParameters outputParameters;

  StepHandler(final OutputPackage trace, final OutputParameters outputParameters) {
    this.trace = trace;
    this.outputParameters = outputParameters;
  }

  public void handleStep(SpacecraftState currentState, boolean isLast) {
    KeplerianOrbit o = (KeplerianOrbit) OrbitType.KEPLERIAN.convertType(currentState.getOrbit());
    trace.addOrbitSample(OrbitUtils.makeOutput(o, outputParameters.outputs));
  }

}
