package com.blueapogee.model.form;


import com.blueapogee.model.Orbit;

public class OrbitPropagationFormData {

  public PropagationParameters propagationParameters;
  public Orbit orbit;

  public OrbitPropagationFormData() {}

  public OrbitPropagationFormData(Orbit orbit, PropagationParameters propagationParameters) {
    this.orbit = orbit;
    this.propagationParameters = propagationParameters;
  }

  public PropagationParameters getPropagationParameters() {
    return propagationParameters;
  }

  public void setPropagationParameters(final PropagationParameters propagationParameters) {
    this.propagationParameters = propagationParameters;
  }

  public Orbit getOrbit() {
    return orbit;
  }

  public void setOrbit(final Orbit orbit) {
    this.orbit = orbit;
  }
}
