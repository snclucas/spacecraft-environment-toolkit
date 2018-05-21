package com.blueapogee.physics;


public class PlasmaPhysics {


  static double calcPlanetStagnationPoint(final double K, final double Be,
                                   final double n_sw, final double mi,
                                   final double vsw) {
    return Math.pow((K*Be) / ( 2 * Constants.mu0 * n_sw * mi * Math.pow(vsw, 2.0) ), 1/6);
  }

  static double calcPlanetStagnationPointFlank(final double K, final double Be,
                                          final double n_sw, final double gamma,
                                          final double Tsw) {
    return Math.pow((K*Be) / ( 2 * Constants.mu0 * gamma * n_sw * Constants.k_b * Tsw ), 1/6);
  }
}
