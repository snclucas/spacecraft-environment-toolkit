package com.blueapogee.service;

import com.blueapogee.model.HtplUser;
import com.blueapogee.model.Orbit;
import com.blueapogee.model.form.OrbitFormData;
import com.blueapogee.model.form.OrbitPropagationFormData;
import com.blueapogee.model.form.PropagationParameters;
import com.blueapogee.model.repo.OrbitRepository;
import org.bson.types.ObjectId;
import org.orekit.data.DataProvidersManager;
import org.orekit.data.DirectoryCrawler;
import org.orekit.errors.OrekitException;
import org.orekit.frames.Frame;
import org.orekit.frames.FramesFactory;
import org.orekit.orbits.KeplerianOrbit;
import org.orekit.orbits.OrbitType;
import org.orekit.orbits.PositionAngle;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.analytical.KeplerianPropagator;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScale;
import org.orekit.time.TimeScalesFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

@Component
public class OrbitService {

  @Autowired
  private OrbitRepository orbitRepository;

  public Orbit getOrbitById(String id) {
    return orbitRepository.findOne(id);
  }

  public Page<Orbit> getAllOrbits(HtplUser user, Sort sort, int pageNumber, int limit) {
    Pageable request =
            new PageRequest(pageNumber - 1, limit, sort);

    Page<Orbit> orbits = orbitRepository.findAll(request);

    return orbits;
  }

  public Orbit saveOrbit(Orbit orbit) {
    return orbitRepository.save(orbit);
  }

  public void deleteOrbit(Orbit orbit) {
    orbitRepository.delete(orbit);
  }

  public long deleteOrbit(final HtplUser user, final String orbit_id) {
    Orbit orbitToDelete = getOrbitById(orbit_id);
    long result = 0;
    if(orbitToDelete != null) {
      result = orbitRepository.deleteOrbitById(orbitToDelete.id);
    }
    return result;
  }

  public Orbit saveOrbit(final OrbitFormData orbitFormData, final HtplUser user) {
    Orbit orbit = new Orbit(orbitFormData);
    return saveOrbit(orbit);
  }

  // TODO FINSIHS
  private boolean checkOrbitValidity(final OrbitFormData orbitFormData) {
    String orbitType = orbitFormData.type;

    if(!orbitType.equalsIgnoreCase("CARTESIAN")) {
      return orbitFormData.getSemiMajorAxis() >= 0.0;
    }

    if(orbitType.equalsIgnoreCase("KEPLERIAN")) {
      return orbitFormData.getEccentricity() >= 0.0 &&
              orbitFormData.getInclination() >= 0.0 &&
              orbitFormData.argumentOfPerigee >= 0.0;
    }

    if(orbitType.equalsIgnoreCase("KEPLERIAN") ||
            orbitType.equalsIgnoreCase("CIRCULAR") ||
            orbitType.equalsIgnoreCase("EQUINOCTIAL")) {
      return orbitFormData.getSemiMajorAxis() >= 0.0;
    }
    //case "KEPLERIAN":

    //case "EQUINOCTIAL":
    //case "CIRCULAR":
    //case "CARTESIAN":
    return true;
  }


  public OrbitTrace propagateOrbit(final OrbitPropagationFormData orbitPropagationFormData, final HtplUser user) {


    OrbitTrace trace = new OrbitTrace();

    TimeScale utc = null;
    try {

      File orekitData = new File(Thread.currentThread().getContextClassLoader().getResource("orekit-data").getFile());

      //File orekitData = new File("/path/to/the/folder/orekit-data");
      DataProvidersManager manager = DataProvidersManager.getInstance();
      manager.addProvider(new DirectoryCrawler(orekitData));


      PropagationParameters params = orbitPropagationFormData.getPropagationParameters();

      Frame inertialFrame = FramesFactory.getEME2000();

      utc = TimeScalesFactory.getUTC();

      AbsoluteDate initialDate = new AbsoluteDate(2004, 01, 01, 23, 30, 00.000, utc);
      Orbit suppliedOrbit = orbitPropagationFormData.orbit;

      org.orekit.orbits.Orbit initialOrbit = new KeplerianOrbit(
              suppliedOrbit.getSemiMajorAxis(),
              suppliedOrbit.getEccentricity(),
              suppliedOrbit.getInclination(),
              suppliedOrbit.getArgumentOfPerigee(),
              suppliedOrbit.getRAAN(),
              suppliedOrbit.getTrueAnomaly(),
              PositionAngle.TRUE,
              inertialFrame,
              initialDate,
              params.mu);


      KeplerianPropagator kepler = new KeplerianPropagator(initialOrbit);
      kepler.setSlaveMode();



      double duration = params.duration;
      AbsoluteDate finalDate = initialDate.shiftedBy(duration);
      double stepT = params.stepTime;
      int cpt = 1;
      for (AbsoluteDate extrapDate = initialDate;
           extrapDate.compareTo(finalDate) <= 0;
           extrapDate = extrapDate.shiftedBy(stepT))  {
        SpacecraftState currentState = kepler.propagate(extrapDate);
        System.out.println("step " + cpt++);
        System.out.println(" time : " + currentState.getDate());
        System.out.println(" " + currentState.getOrbit());

        trace.orbitTrace.put(currentState.getDate().toString(), currentState.getOrbit());

      }


    } catch (OrekitException e) {
      e.printStackTrace();
    }

    return trace;
  }
}
