package com.blueapogee.physics.externalfield;


public class T01ModelInputs {

  private final double PS; //earth's dipole tilt angle in radians 23.5
  private final double PDYN; //solar wind ram pressure in nanoPascals
  private final double DST;
  private final double BYIMF; // nT
  private final double BZIMF; // nT
  private final double G1;
  private final double G2;

  public T01ModelInputs(final double PS, final double PDYN, final double DST, final
  double BYIMF, final double BZIMF, final double g1, final double g2) {
    this.PS = PS;
    this.PDYN = PDYN;
    this.DST = DST;
    this.BYIMF = BYIMF;
    this.BZIMF = BZIMF;
    G1 = g1;
    G2 = g2;
  }


  public double getPS() {
    return PS;
  }

  public double getPDYN() {
    return PDYN;
  }

  public double getDST() {
    return DST;
  }

  public double getBYIMF() {
    return BYIMF;
  }

  public double getBZIMF() {
    return BZIMF;
  }

  public double getG1() {
    return G1;
  }

  public double getG2() {
    return G2;
  }
}
