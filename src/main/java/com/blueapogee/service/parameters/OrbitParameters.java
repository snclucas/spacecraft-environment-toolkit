package com.blueapogee.service.parameters;

public class OrbitParameters {

  public String type = "";
  public String name = "";
  public boolean save = false;
  public String fromDate = "";
  public String toDate = "";

  public double epoch = 0.0;
  public double eccentricity = 0.0; //eccentricity
  public double semiMajorAxis = 0.0;
  public double inclination = 0.0;
  public double RAAN = 0.0;
  public double argumentOfPerigee = 0.0;
  public double trueAnomaly = 0.0;
  public double meanAnomaly = 0.0;
  public double eccentricAnomaly = 0.0;
  public String positionAngle = "true";

  public double Px = 0.0;
  public double Py = 0.0;
  public double Pz = 0.0;

  public double Vx = 0.0;
  public double Vy = 0.0;
  public double Vz = 0.0;

  public OrbitParameters() {}

  public OrbitParameters(final String type, final String name, final String fromDate, final String toDate,
                         final double epoch, final double eccentricity, final double semiMajorAxis,
                         final double inclination, final double RAAN, final double argumentOfPerigee,
                         final double trueAnomaly, final double meanAnomaly, final double eccentricAnomaly,
                         final String positionAngle) {
    this.type = type;
    this.name = name;
    this.fromDate = fromDate;
    this.toDate = toDate;
    this.epoch = epoch;
    this.eccentricity = eccentricity;
    this.semiMajorAxis = semiMajorAxis;
    this.inclination = inclination;
    this.RAAN = RAAN;
    this.argumentOfPerigee = argumentOfPerigee;
    this.trueAnomaly = trueAnomaly;
    this.meanAnomaly = meanAnomaly;
    this.eccentricAnomaly = eccentricAnomaly;
    this.positionAngle = positionAngle;
  }
}
