package com.blueapogee.controller.rest;


import com.blueapogee.model.*;

public class OrbitPackage {
  public String message;
  public String status;
  public Orbit orbit;

  public OrbitPackage(String message, String status, Orbit orbit) {
    this.message = message;
    this.status = status;
    this.orbit = orbit;
  }


}
