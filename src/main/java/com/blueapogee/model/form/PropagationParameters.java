package com.blueapogee.model.form;


public class PropagationParameters {

  public double duration = 365*24*3600;
  public double stepTime = 3600;
  public double mu = 3.986004415e+14;
  public String propagator = "KeplerianPropagator";

  public PropagationParameters() {}

}
