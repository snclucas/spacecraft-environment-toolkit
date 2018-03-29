package com.blueapogee.service.util;


import com.blueapogee.model.form.OrbitParameters;
import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.orekit.frames.Frame;
import org.orekit.orbits.CartesianOrbit;
import org.orekit.orbits.KeplerianOrbit;
import org.orekit.orbits.Orbit;
import org.orekit.orbits.PositionAngle;
import org.orekit.time.AbsoluteDate;
import org.orekit.utils.PVCoordinates;
import org.orekit.utils.TimeStampedPVCoordinates;

import java.util.HashMap;
import java.util.Map;

public class OrbitUtils {


  public static Orbit getOrbit(OrbitParameters orbitParameters, Frame inertialFrame, AbsoluteDate initialDate, double mu) {
    Orbit orbit;
    if(orbitParameters.type.equalsIgnoreCase("cartesian")) {
      Vector3D position  = new Vector3D(orbitParameters.Px, orbitParameters.Py, orbitParameters.Pz);
      Vector3D velocity  = new Vector3D(orbitParameters.Vx, orbitParameters.Vy, orbitParameters.Vz);
      PVCoordinates pvCoordinates = new PVCoordinates(position, velocity);
      orbit = new KeplerianOrbit(pvCoordinates, inertialFrame, initialDate, mu);
    } else {
      orbit = new KeplerianOrbit(
              orbitParameters.semiMajorAxis,
              orbitParameters.eccentricity,
              orbitParameters.inclination,
              orbitParameters.argumentOfPerigee,
              orbitParameters.RAAN,
              orbitParameters.trueAnomaly,
              PositionAngle.TRUE,
              inertialFrame,
              initialDate,
              mu);
    }

    return orbit;
  }

  public static double[] cartesianToLatLon(double r, double x, double y, double z) {
  double[] latLon = new double[2];

    latLon[0] = Math.asin(z/r)*(180/Math.PI);

    if (x > 0) {
      latLon[1] = Math.atan(y/x)*(180/Math.PI);
    } else if (y > 0) {
      latLon[1] = Math.atan(y/x)*(180/Math.PI) + 180;
    } else {
      latLon[1] = Math.atan(y/x)*(180/Math.PI) - 180;
    }
    return latLon;
  }

  public static boolean checkOrbitValidity(final OrbitParameters orbitParameters) {
    String orbitType = orbitParameters.type;
    if(!orbitType.equalsIgnoreCase("CARTESIAN") &&
            !orbitType.equalsIgnoreCase("KEPLERIAN") &&
            !orbitType.equalsIgnoreCase("CIRCULAR") &&
            !orbitType.equalsIgnoreCase("EQUINOCTIAL")) {
      return false;
    }

    if (!orbitType.equalsIgnoreCase("CARTESIAN")) {
      return orbitParameters.semiMajorAxis > 6700;
    }

    return true;
  }

  public static Map<String, String> makeErrorOutput(String error) {
    Map<String, String> outputMap = new HashMap<>();
    outputMap.put("Status", "Problem");
    outputMap.put("Message", error);
    return outputMap;
  }

  public static Map<String, String> makeOutput(org.orekit.orbits.Orbit orbit, String output) {
    Map<String, String> outputMap = new HashMap<>();
    output = output.trim().replace(" ", "");
    String[] outputs = output.split(",");

    TimeStampedPVCoordinates orbitPVCoordinates = orbit.getPVCoordinates();

    outputMap.put("date", orbitPVCoordinates.getDate().toString());

    for(String outputToken : outputs) {
      if(outputToken.equalsIgnoreCase("X")) {
        outputMap.put("Px", Double.toString(orbitPVCoordinates.getPosition().getX()));
      }
      if(outputToken.equalsIgnoreCase("Y")) {
        outputMap.put("Py", Double.toString(orbitPVCoordinates.getPosition().getY()));
      }
      if(outputToken.equalsIgnoreCase("Z")) {
        outputMap.put("Pz", Double.toString(orbitPVCoordinates.getPosition().getZ()));
      }

      if(outputToken.equalsIgnoreCase("VX")) {
        outputMap.put("Vx", Double.toString(orbitPVCoordinates.getVelocity().getX()));
      }
      if(outputToken.equalsIgnoreCase("VY")) {
        outputMap.put("Vy", Double.toString(orbitPVCoordinates.getVelocity().getY()));
      }
      if(outputToken.equalsIgnoreCase("VZ")) {
        outputMap.put("Vz", Double.toString(orbitPVCoordinates.getVelocity().getZ()));
      }

      if(outputToken.equalsIgnoreCase("AX")) {
        outputMap.put("Ax", Double.toString(orbitPVCoordinates.getAcceleration().getX()));
      }
      if(outputToken.equalsIgnoreCase("AY")) {
        outputMap.put("Ay", Double.toString(orbitPVCoordinates.getAcceleration().getY()));
      }
      if(outputToken.equalsIgnoreCase("AZ")) {
        outputMap.put("Az", Double.toString(orbitPVCoordinates.getAcceleration().getZ()));
      }

      if(outputToken.equalsIgnoreCase("A")) {
        outputMap.put("A", Double.toString(orbit.getA()));
      }

      if(outputToken.equalsIgnoreCase("E")) {
        outputMap.put("E", Double.toString(orbit.getE()));
      }

      if(outputToken.equalsIgnoreCase("I")) {
        outputMap.put("I", Double.toString(orbit.getI()));
      }

      if(outputToken.equalsIgnoreCase("lat-lon")) {
        outputMap.put("Lat", Double.toString(OrbitUtils.cartesianToLatLon(
                orbit.getA(),
                orbitPVCoordinates.getPosition().getX(),
                orbitPVCoordinates.getPosition().getY(),
                orbitPVCoordinates.getPosition().getZ()
        )[0]));
        outputMap.put("Lon", Double.toString(OrbitUtils.cartesianToLatLon(
                orbit.getA(),
                orbitPVCoordinates.getPosition().getX(),
                orbitPVCoordinates.getPosition().getY(),
                orbitPVCoordinates.getPosition().getZ()
        )[1]));
      }

    }
    return outputMap;
  }

}