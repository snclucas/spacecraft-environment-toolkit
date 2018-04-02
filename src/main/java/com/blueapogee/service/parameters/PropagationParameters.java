package com.blueapogee.service.parameters;


public class PropagationParameters {
  public double duration = 5*3600;
  public double stepTime = 3600;
  public double mu = 3.986004415e+14;
  public String propagator = "KeplerianPropagator";
  public double positionTolerance = 10.0;
  public double minStep = 0.001;
  public double maxStep = 1000.0;
  public String initialDate = "";
  public String integrator = "RungeKutta";
  public String forceModel = "Newtonian";

  public PropagationParameters() {}

}
