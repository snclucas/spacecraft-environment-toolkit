package com.blueapogee.service;

import com.blueapogee.model.HtplUser;
import com.blueapogee.model.Orbit;
import com.blueapogee.model.form.OrbitFormData;
import com.blueapogee.model.form.OrbitPropagationFormData;
import com.blueapogee.model.form.PropagationParameters;
import com.blueapogee.model.repo.OrbitRepository;
import org.bson.types.ObjectId;
import org.hipparchus.ode.nonstiff.*;
import org.hipparchus.util.*;
import org.orekit.data.DataProvidersManager;
import org.orekit.data.DirectoryCrawler;
import org.orekit.errors.OrekitException;
import org.orekit.forces.*;
import org.orekit.forces.gravity.*;
import org.orekit.forces.gravity.potential.*;
import org.orekit.frames.Frame;
import org.orekit.frames.FramesFactory;
import org.orekit.orbits.KeplerianOrbit;
import org.orekit.orbits.OrbitType;
import org.orekit.orbits.PositionAngle;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.analytical.KeplerianPropagator;
import org.orekit.propagation.numerical.*;
import org.orekit.propagation.sampling.*;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScale;
import org.orekit.time.TimeScalesFactory;
import org.orekit.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class OrbitService {


  /*


{
	"orbit": {
		"type": "keplerian",
		"semiMajorAxis": 24396159,
		"eccentricity":0.72831215,
		"inclination":0.72831215
	},
	"propagationParameters": {
		"propagator": "KeplerianPropagator",
		"initalDate":"2007-01-01T23:38:00.000",
		"duration": 6000,
		"stepTime": 60,
		"minStep": 0.001,
		"maxStep": 1000.0,
		"positionTolerance": 10
	}
}


  */

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

      AbsoluteDate initialDate = new AbsoluteDate(params.initalDate, utc);

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

      // Initial state definition
      SpacecraftState initialState = new SpacecraftState(initialOrbit);

      OrbitType propagationType;
      if(params.propagator.equalsIgnoreCase("equinoctal")) {
        propagationType = OrbitType.EQUINOCTIAL;
      } else if(params.propagator.equalsIgnoreCase("cartesian")) {
        propagationType = OrbitType.CARTESIAN;
      } else if(params.propagator.equalsIgnoreCase("circular")) {
        propagationType = OrbitType.CIRCULAR;
      } else {
        propagationType = OrbitType.KEPLERIAN;
      }

      double[][] tolerances =
              NumericalPropagator.tolerances(params.positionTolerance, initialOrbit, propagationType);
      AdaptiveStepsizeIntegrator integrator =
              new DormandPrince853Integrator(params.minStep, params.maxStep, tolerances[0], tolerances[1]);

      NumericalPropagator propagator = new NumericalPropagator(integrator);
      propagator.setOrbitType(propagationType);

      NormalizedSphericalHarmonicsProvider provider =
              GravityFieldFactory.getNormalizedProvider(10, 10);

      ForceModel holmesFeatherstone =
              new HolmesFeatherstoneAttractionModel(FramesFactory.getITRF(IERSConventions.IERS_2010,
                      true),
                      provider);

      propagator.addForceModel(holmesFeatherstone);

      propagator.setMasterMode(60., new TutorialStepHandler());

      propagator.setInitialState(initialState);

      SpacecraftState finalState =
              propagator.propagate(new AbsoluteDate(initialDate, 630.));

    } catch (OrekitException e) {
      e.printStackTrace();
    }

    return trace;
  }



  private static class TutorialStepHandler implements OrekitFixedStepHandler {

    public void init(final SpacecraftState s0, final AbsoluteDate t) {
      System.out.println("          date                a           e" +
              "           i         \u03c9          \u03a9" +
              "          \u03bd");
    }

    public void handleStep(SpacecraftState currentState, boolean isLast) {
      KeplerianOrbit o = (KeplerianOrbit) OrbitType.KEPLERIAN.convertType(currentState.getOrbit());
      System.out.format(Locale.US, "%s %12.3f %10.8f %10.6f %10.6f %10.6f %10.6f%n",
              currentState.getDate(),
              o.getA(), o.getE(),
              FastMath.toDegrees(o.getI()),
              FastMath.toDegrees(o.getPerigeeArgument()),
              FastMath.toDegrees(o.getRightAscensionOfAscendingNode()),
              FastMath.toDegrees(o.getTrueAnomaly()));
      if (isLast) {
        System.out.println("this was the last step ");
        System.out.println();
      }
    }

  }



}


