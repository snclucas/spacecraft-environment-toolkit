package com.blueapogee.service;


import org.orekit.utils.PVCoordinates;
import org.orekit.utils.TimeStampedPVCoordinates;

public class PVCoordinate {

  public String date;

  public double Px;
  public double Py;
  public double Pz;

  public double Vx;
  public double Vy;
  public double Vz;

  public double Ax;
  public double Ay;
  public double Az;

  public PVCoordinate(TimeStampedPVCoordinates pvCoordinates) {
    this.date = pvCoordinates.getDate().toString();
    this.Px = pvCoordinates.getPosition().getX();
    this.Py = pvCoordinates.getPosition().getY();
    this.Pz = pvCoordinates.getPosition().getZ();

    this.Vx = pvCoordinates.getVelocity().getX();
    this.Vy = pvCoordinates.getVelocity().getY();
    this.Vz = pvCoordinates.getVelocity().getZ();

    this.Ax = pvCoordinates.getAcceleration().getX();
    this.Ay = pvCoordinates.getAcceleration().getY();
    this.Az = pvCoordinates.getAcceleration().getZ();
  }

}
