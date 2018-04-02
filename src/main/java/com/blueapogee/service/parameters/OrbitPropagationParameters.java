package com.blueapogee.service.parameters;


import java.util.List;

public class OrbitPropagationParameters {

  public boolean loadOrbit = false;
  public boolean saveOrbit = false;
  public PropagationParameters propagation;
  public OrbitParameters orbit;
  public List<GroundStationParameters> groundstation;
  public OutputParameters output;

  public OrbitPropagationParameters() {}

  public OrbitPropagationParameters(boolean saveOrbit, boolean loadOrbit, OrbitParameters orbit,
                                    PropagationParameters propagation,
                                    List<GroundStationParameters> groundstation,
                                    OutputParameters output) {
    this.loadOrbit = loadOrbit;
    this.saveOrbit = saveOrbit;
    this.orbit = orbit;
    this.propagation = propagation;
    this.output = output;
    this.groundstation = groundstation;
  }

  public PropagationParameters getPropagationParameters() {
    return propagation;
  }

  public void setPropagationParameters(final PropagationParameters propagationParameters) {
    this.propagation = propagationParameters;
  }

  public OrbitParameters getOrbit() {
    return orbit;
  }

  public void setOrbit(final OrbitParameters orbit) {
    this.orbit = orbit;
  }
}