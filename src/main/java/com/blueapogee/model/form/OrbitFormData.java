package com.blueapogee.model.form;

import com.blueapogee.service.parameters.OrbitParameters;

public class OrbitFormData extends OrbitParameters {

  public boolean isPrivate = true;


  public OrbitFormData() {}


  public OrbitFormData(final String type, final String name, final String fromDate, final String toDate, final double epoch,
               final double eccentricity, final double semiMajorAxis, final double inclination,
               final double RAAN, final double argumentOfPerigee, final double trueAnomaly, final double meanAnomaly,
                       final double eccentricAnomaly, String positionAngle) {
    super(type, name, fromDate, toDate, epoch, eccentricity, semiMajorAxis, inclination,
            RAAN, argumentOfPerigee, trueAnomaly, meanAnomaly, eccentricAnomaly, positionAngle);
  }

  public String getType() {
    return type;
  }

  public void setType(final String type) {
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String getFromDate() {
    return fromDate;
  }

  public void setFromDate(final String fromDate) {
    this.fromDate = fromDate;
  }

  public String getToDate() {
    return toDate;
  }

  public void setToDate(final String toDate) {
    this.toDate = toDate;
  }

  public double getEpoch() {
    return epoch;
  }

  public void setEpoch(final double epoch) {
    this.epoch = epoch;
  }

  public double getEccentricity() {
    return eccentricity;
  }

  public void setEccentricity(final double eccentricity) {
    this.eccentricity = eccentricity;
  }

  public double getSemiMajorAxis() {
    return semiMajorAxis;
  }

  public void setSemiMajorAxis(final double semiMajorAxis) {
    this.semiMajorAxis = semiMajorAxis;
  }

  public double getInclination() {
    return inclination;
  }

  public void setInclination(final double inclination) {
    this.inclination = inclination;
  }

  public double getRAAN() {
    return RAAN;
  }

  public void setRAAN(final double RAAN) {
    this.RAAN = RAAN;
  }

  public double getArgumentOfPerigee() {
    return argumentOfPerigee;
  }

  public void setArgumentOfPerigee(final double argumentOfPerigee) {
    this.argumentOfPerigee = argumentOfPerigee;
  }

  public double getTrueAnomaly() {
    return trueAnomaly;
  }

  public void setTrueAnomaly(final double trueAnomaly) {
    this.trueAnomaly = trueAnomaly;
  }

}
