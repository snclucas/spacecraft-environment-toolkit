package com.blueapogee.service.util;


import com.blueapogee.model.HtplUser;
import com.blueapogee.model.Orbit;
import com.blueapogee.service.OrbitService;
import com.blueapogee.service.outputs.OrbitPackage;
import com.blueapogee.service.outputs.OutputPackage;
import com.blueapogee.service.parameters.GeoFieldParameters;
import com.blueapogee.service.parameters.OutputParameters;
import org.orekit.errors.OrekitException;
import org.orekit.frames.Frame;
import org.orekit.frames.FramesFactory;
import org.orekit.models.earth.GeoMagneticElements;
import org.orekit.models.earth.GeoMagneticField;
import org.orekit.models.earth.GeoMagneticFieldFactory;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScale;
import org.orekit.time.TimeScalesFactory;

import java.util.Map;

public class GeomagneticFieldUtils {


  public static GeoMagneticElements calcField(AbsoluteDate initialDate, String modelToRun,
                                   double longitude, double latitude, double altitude) throws OrekitException {
    Frame inertialFrame = FramesFactory.getEME2000();
    TimeScale utc = TimeScalesFactory.getUTC();

    double year = GeoMagneticField.getDecimalYear(
            initialDate.getComponents(utc).getDate().getDay(),
            initialDate.getComponents(utc).getDate().getMonth(),
            initialDate.getComponents(utc).getDate().getYear());

    if(year > 2015.0) {
      year = 2015.0;
    }

    GeoMagneticField model = modelToRun.equalsIgnoreCase("WMM")
            ? GeoMagneticFieldFactory.getWMM(year)
            : GeoMagneticFieldFactory.getIGRF(year);

    return model.calculateField(longitude, latitude, altitude);
  }


  public static OutputPackage calcGeoField(final OrbitService orbitService, final GeoFieldParameters geoFieldParameters, HtplUser user) {
    OutputPackage trace = new OutputPackage();

    Frame inertialFrame = FramesFactory.getEME2000();
    TimeScale utc;
    try {
      utc = TimeScalesFactory.getUTC();
    } catch (OrekitException e) {
      trace.errors = (OrbitUtils.makeErrorOutput("Error getting UTC date"));
      return trace;
    }
    AbsoluteDate initialDate = new AbsoluteDate(geoFieldParameters.propagation.initialDate, utc);

    org.orekit.orbits.Orbit initialOrbit;
    if (geoFieldParameters.orbit == null && !geoFieldParameters.orbitName.equalsIgnoreCase("")) {
      Orbit orbitFromDB = orbitService.getOrbitByName(geoFieldParameters.orbitName, user);
      if (orbitFromDB == null) {
        trace.errors = (OrbitUtils.makeErrorOutput("No orbit found with name {" +
                geoFieldParameters.orbitName + "}"));
        return trace;
      }

      initialOrbit = OrbitUtils.getOrbitFromOrbitModel(orbitFromDB,
              inertialFrame, initialDate, geoFieldParameters.propagation.mu);

    } else {
      initialOrbit = OrbitUtils.getOrbitFromParameters(geoFieldParameters.orbit,
              inertialFrame, initialDate, geoFieldParameters.propagation.mu);
    }

    OutputParameters outputParameters = new OutputParameters();
    outputParameters.outputs = "lat-lon, alt";

    OutputPackage propOutput = orbitService.propagateOrbit(initialOrbit,
            geoFieldParameters.propagation,
            outputParameters,
            null,  // List<GroundStationParameters> groundStationParameters,
            initialDate);

    for(Map<String, String> orbitData: propOutput.orbit) {

      double lon = Double.parseDouble(orbitData.get("lon"));
      double lat = Double.parseDouble(orbitData.get("lat"));
      double alt = Double.parseDouble(orbitData.get("alt"));


      GeoMagneticElements result = null;
      try {
        result = calcField(initialDate, geoFieldParameters.geoFieldModel, lon, lat, alt);
      } catch (OrekitException e) {
        e.printStackTrace();
      }

      String output = geoFieldParameters.outputs.trim().replace(" ", "");
      String[] outputs = output.split(",");

      for(String outputToken : outputs) {
        if (outputToken.equalsIgnoreCase("F")) {
          orbitData.put("F", Double.toString(result.getTotalIntensity()));
        }
        if (outputToken.equalsIgnoreCase("H")) {
          orbitData.put("H", Double.toString(result.getHorizontalIntensity()));
        }
        if (outputToken.equalsIgnoreCase("X")) {
          orbitData.put("X", Double.toString(result.getFieldVector().getX()));
        }
        if (outputToken.equalsIgnoreCase("Y")) {
          orbitData.put("Y", Double.toString(result.getFieldVector().getY()));
        }
        if (outputToken.equalsIgnoreCase("Z")) {
          orbitData.put("Z", Double.toString(result.getFieldVector().getZ()));
        }
        if (outputToken.equalsIgnoreCase("I")) {
          orbitData.put("I", Double.toString(result.getInclination()));
        }
        if (outputToken.equalsIgnoreCase("D")) {
          orbitData.put("D", Double.toString(result.getDeclination()));
        }
      }
    }

    return propOutput;

  }


}
