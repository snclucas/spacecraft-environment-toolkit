package com.blueapogee.service;

import com.blueapogee.model.HtplUser;
import com.blueapogee.model.Orbit;
import com.blueapogee.model.form.*;
import com.blueapogee.model.repo.OrbitRepository;
import com.blueapogee.service.util.OrbitUtils;
import org.hipparchus.ode.ODEIntegrator;
import org.hipparchus.ode.nonstiff.*;
import org.hipparchus.util.FastMath;
import org.orekit.bodies.BodyShape;
import org.orekit.bodies.GeodeticPoint;
import org.orekit.bodies.OneAxisEllipsoid;
import org.orekit.data.DataProvidersManager;
import org.orekit.data.DirectoryCrawler;
import org.orekit.errors.OrekitException;
import org.orekit.forces.*;
import org.orekit.forces.gravity.*;
import org.orekit.forces.gravity.potential.*;
import org.orekit.frames.Frame;
import org.orekit.frames.FramesFactory;
import org.orekit.frames.TopocentricFrame;
import org.orekit.orbits.KeplerianOrbit;
import org.orekit.orbits.OrbitType;
import org.orekit.orbits.PositionAngle;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.events.ElevationDetector;
import org.orekit.propagation.events.EventDetector;
import org.orekit.propagation.events.handlers.EventHandler;
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

@Component
public class OrbitService {

  public OrbitService() {
    try {
      File orekitData = new File(Thread.currentThread().getContextClassLoader().getResource("orekit-data").getFile());
      DataProvidersManager manager = DataProvidersManager.getInstance();
      manager.addProvider(new DirectoryCrawler(orekitData));
    } catch (OrekitException e) {
      e.printStackTrace();
    }
  }


  /*


{
	"loadOrbit": false,
	"saveOrbit": false,
    "orbit": {
    	"name": "myorbit",
    	"type": "keplerian",
    	"semiMajorAxis": "24396159",
    	"eccentricity": "0.72831215",
    	"inclination": "0.122173",
    	"RAAN": "4.55531",
    	"argumentOfPerigee": "3.14159",
    	"trueAnomaly": "0.0",
    	"Px": -6142438.668,
    	"Py": 3492467.560,
    	"Pz": -25767.25680,
    	"Vx": 505.8479685,
    	"Vy": 942.7809215,
    	"Vz": 7435.922231
    },
    "propagation": {
    	"initialDate": "2018-01-01T07:30:45.874",
    	"propagator": "keplerian",
    	"integrator": "HolmesFeatherstone",
    	"duration": 86400,
    	"stepTime": 100,
    	"minStep": 0.001,
    	"maxStep": 1000.0,
    	"positionTolerance": 10
    },
    "groundstation": {
    	"longitude": 0.785398,
    	"latitude": 0.436332,
    	"altitude": 0.0
    },
    "output": {
    	"outputs": "X,Y,Z,I,lat-lon"
    }
}

  */

  @Autowired
  private OrbitRepository orbitRepository;

  public Orbit getOrbitById(String id, final HtplUser user) {
    return orbitRepository.findOne(id);
  }

  public Orbit getOrbitByName(String name, final HtplUser user) {
    return orbitRepository.getOrbitByNameAndUser(name, user.getUsername());
  }

  public Page<Orbit> getAllOrbits(HtplUser user, Sort sort, int pageNumber, int limit) {
    Pageable request = new PageRequest(pageNumber - 1, limit, sort);
    Page<Orbit> orbits = orbitRepository.findAllByName(user.getUsername(), request);
    return orbits;
  }

  public Orbit saveOrbit(Orbit orbit, final HtplUser user) {
    orbit.setUser(user.getUsername());
    return orbitRepository.save(orbit);
  }

  public void deleteOrbit(Orbit orbit) {
    orbitRepository.delete(orbit);
  }

  public long deleteOrbit(final String orbit_id, final HtplUser user) {
    Orbit orbitToDelete = getOrbitById(orbit_id, user);
    long result = 0;
    if(orbitToDelete != null) {
      result = orbitRepository.deleteOrbitById(orbitToDelete.id, user.getUsername());
    }
    return result;
  }

  public Orbit saveOrbit(final OrbitFormData orbitFormData, final HtplUser user) {
    Orbit orbit = new Orbit(orbitFormData);
    return saveOrbit(orbit, user);
  }

  public Orbit saveOrbit(final OrbitPropagationParameters orbitPropagationParameters, final HtplUser user) {
    Orbit orbit = new Orbit(orbitPropagationParameters.getOrbit());
    return saveOrbit(orbit, user);
  }

  public Orbit saveOrbit(final OrbitParameters orbitParameters, final HtplUser user) {
    Orbit orbit = new Orbit(orbitParameters);
    return saveOrbit(orbit, user);
  }

  public OrbitTrace propagateOrbit(final OrbitPropagationParameters orbitPropagationParameters, final HtplUser user) {
    OrbitTrace trace = new OrbitTrace();

    OrbitParameters orbitParameters = orbitPropagationParameters.orbit;
    if(!OrbitUtils.checkOrbitValidity(orbitParameters)) {
      trace.orbitTrace.add(OrbitUtils.makeErrorOutput("Bad orbit"));
      return trace;
    }

    PropagationParameters propagationParams = orbitPropagationParameters.getPropagationParameters();
    OutputParameters outputParameters = orbitPropagationParameters.output;

    try {
      Frame inertialFrame = FramesFactory.getEME2000();

      TimeScale utc = TimeScalesFactory.getUTC();
      AbsoluteDate initialDate = new AbsoluteDate(propagationParams.initialDate, utc);

      org.orekit.orbits.Orbit initialOrbit;
      if(orbitPropagationParameters.loadOrbit && !orbitParameters.name.equals("")) {
        Orbit orbit = getOrbitByName(orbitParameters.name, user);
        if(orbit == null) {
          trace.orbitTrace.add(OrbitUtils.makeErrorOutput("No orbit found with name {" + orbitParameters.name + "}"));
          return trace;
        }
       /// orbit.
        initialOrbit = new KeplerianOrbit(
                orbit.semiMajorAxis,
                orbit.eccentricity,
                orbit.inclination,
                orbit.argumentOfPerigee,
                orbit.RAAN,
                orbit.trueAnomaly,
                PositionAngle.TRUE,
                inertialFrame,
                initialDate,
                propagationParams.mu);
      } else {
        initialOrbit = OrbitUtils.getOrbit(orbitParameters, inertialFrame, initialDate, propagationParams.mu);
      }

      if(orbitPropagationParameters.saveOrbit && !orbitParameters.name.equals("")) {
        saveOrbit(orbitParameters, user);
      }

      // Initial state definition
      SpacecraftState initialState = new SpacecraftState(initialOrbit);

      OrbitType propagationType;
      if(propagationParams.propagator.equalsIgnoreCase("equinoctal")) {
        propagationType = OrbitType.EQUINOCTIAL;
      } else if(propagationParams.propagator.equalsIgnoreCase("cartesian")) {
        propagationType = OrbitType.CARTESIAN;
      } else if(propagationParams.propagator.equalsIgnoreCase("circular")) {
        propagationType = OrbitType.CIRCULAR;
      } else {
        propagationType = OrbitType.KEPLERIAN;
      }

      double[][] tolerances =
              NumericalPropagator.tolerances(propagationParams.positionTolerance, initialOrbit, propagationType);

      ODEIntegrator integrator;
      if(propagationParams.integrator.equalsIgnoreCase("DormandPrince")) {
        integrator =
                new DormandPrince853Integrator(propagationParams.minStep, propagationParams.maxStep, tolerances[0], tolerances[1]);
      } else {
        integrator = new ClassicalRungeKuttaIntegrator(propagationParams.stepTime);
      }

      NumericalPropagator propagator = new NumericalPropagator(integrator);

      propagator.setOrbitType(propagationType);

      ForceModel forceModel;
      if(propagationParams.forceModel.equalsIgnoreCase("HolmesFeatherstone")) {
        NormalizedSphericalHarmonicsProvider provider =
                GravityFieldFactory.getNormalizedProvider(10, 10);
        forceModel = new HolmesFeatherstoneAttractionModel(FramesFactory.getITRF(IERSConventions.IERS_2010, true),

                provider);
        propagator.addForceModel(forceModel);
      } else {
        forceModel = new NewtonianAttraction(propagationParams.mu);
        propagator.addForceModel(forceModel);
      }

      propagator.setMasterMode(propagationParams.stepTime, new TutorialStepHandler(trace, outputParameters));

      propagator.setInitialState(initialState);


      if(orbitPropagationParameters.groundstation != null) {
        double longitude = FastMath.toRadians(45.);
        double latitude  = FastMath.toRadians(25.);
        double altitude  = 0.;
        GeodeticPoint station1 = new GeodeticPoint(latitude, longitude, altitude);

        Frame earthFrame = FramesFactory.getITRF(IERSConventions.IERS_2010, true);
        BodyShape earth = new OneAxisEllipsoid(Constants.WGS84_EARTH_EQUATORIAL_RADIUS,
                Constants.WGS84_EARTH_FLATTENING,
                earthFrame);
        TopocentricFrame sta1Frame = new TopocentricFrame(earth, station1, "station1");

        double maxcheck  = 60.0;
        double threshold =  0.001;
        double elevation = FastMath.toRadians(5.);
        EventDetector sta1Visi =
                new ElevationDetector(maxcheck, threshold, sta1Frame).
                        withConstantElevation(elevation).
                        withHandler(new VisibilityHandler());

        propagator.addEventDetector(sta1Visi);
      }




      SpacecraftState finalState =
              propagator.propagate(new AbsoluteDate(initialDate, propagationParams.duration));

    } catch (OrekitException e) {
      e.printStackTrace();
    }

    return trace;
  }

  private static class VisibilityHandler implements EventHandler<ElevationDetector> {

    public Action eventOccurred(final SpacecraftState s, final ElevationDetector detector,
                                final boolean increasing) {
      if (increasing) {
        System.out.println(" Visibility on " + detector.getTopocentricFrame().getName()
                + " begins at " + s.getDate());
        return Action.CONTINUE;
      } else {
        System.out.println(" Visibility on " + detector.getTopocentricFrame().getName()
                + " ends at " + s.getDate());
        return Action.STOP;
      }
    }

    public SpacecraftState resetState(final ElevationDetector detector, final SpacecraftState oldState) {
      return oldState;
    }

  }

  private static class TutorialStepHandler implements OrekitFixedStepHandler {

    private final OrbitTrace trace;
    private final OutputParameters outputParameters;

    TutorialStepHandler(OrbitTrace trace, OutputParameters outputParameters) {
      this.trace = trace;
      this.outputParameters = outputParameters;
    }

    public void handleStep(SpacecraftState currentState, boolean isLast) {
      KeplerianOrbit o = (KeplerianOrbit) OrbitType.KEPLERIAN.convertType(currentState.getOrbit());
      trace.orbitTrace.add(OrbitUtils.makeOutput(o, outputParameters.outputs));
    }

  }








}


