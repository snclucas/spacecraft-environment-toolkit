package com.blueapogee.controller.rest;

import com.blueapogee.model.HtplUser;
import com.blueapogee.model.Orbit;
import com.blueapogee.service.outputs.OrbitPackage;
import com.blueapogee.service.parameters.GeoFieldParameters;
import com.blueapogee.model.form.OrbitFormData;
import com.blueapogee.service.parameters.OrbitPropagationParameters;
import com.blueapogee.service.OrbitService;

import com.blueapogee.service.parameters.TLEParameters;
import org.bson.types.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;



@RestController
public class OrbitRestController {

  @Autowired
  private OrbitService orbitService;


  @Autowired
  private HttpServletRequest context;


  @RequestMapping(method=GET, value={"/api/orbits/{id}"})
  public Orbit message(
          @PathVariable("id") String id) {
    HtplUser user = (HtplUser) context.getAttribute("user_from_token");
    return orbitService.getOrbitById(id, user);
  }

  @RequestMapping(method=GET, value={"/api/orbits"})
  public List<Orbit> getMessages(
          @RequestParam(value = "sortby", defaultValue = "created_at") String sortby,
          @RequestParam(value = "order", defaultValue = "asc") String order,
          @RequestParam(value = "page", defaultValue = "1") int page,
          @RequestParam(value = "limit", defaultValue = "100") int limit,
          @RequestParam(value = "tags", defaultValue = "") String tags) {
    HtplUser user = (HtplUser) context.getAttribute("user_from_token");
    Sort sort = new Sort(
            order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortby);
    Page<Orbit> orbits = orbitService.getAllOrbits(user, sort, page, limit);
    return orbits.getContent();
  }

  @RequestMapping(method=POST, value={"/api/orbits/{orbit_id}/delete"},
          produces={"application/json"},
          consumes={"application/json"})
  public String deleteOrbitJSON(@PathVariable("orbit_id") String orbit_id) {
    return deleteOrbit(orbit_id);
  }

  @RequestMapping(method=GET, value={"/api/orbits/{orbit_id_or_name}"},
          produces={"application/json"},
          consumes={"application/json"})
  public OrbitPackage getOrbit(@PathVariable("orbit_id_or_name") String orbit_id_or_name) {
    HtplUser user = (HtplUser) context.getAttribute("user_from_token");

    Orbit orbit = ObjectId.isValid(orbit_id_or_name)
            ? orbitService.getOrbitById(orbit_id_or_name, user)
            : orbitService.getOrbitByName(orbit_id_or_name, user);

    return (orbit != null)
            ? new OrbitPackage("", "success" ,orbit)
            : new OrbitPackage("No orbit found with id " + orbit_id_or_name, "error", null);
  }

  @RequestMapping(method=POST, value={"/api/orbits/add"},
          produces={"application/json"},
          consumes={"application/json"})
  public Orbit createOrbitFromJSON(@RequestBody OrbitFormData orbitFormData ) {
    return createOrbit(orbitFormData);
  }

  @RequestMapping(method=POST, value={"/api/orbits/propagate"},
          produces={"application/json"},
          consumes={"application/json"})
  public Map<String, Object> propagateOrbitFromJSON(@RequestBody OrbitPropagationParameters orbitPropagationParameters) {
    return propagateOrbit(orbitPropagationParameters);
  }


  @RequestMapping(method=POST, value={"/api/orbits/tle-propagate"},
          produces={"application/json"},
          consumes={"application/json"})
  public void propagateTLEFromJSON(@RequestBody TLEParameters tleParameters) {
    propagateTLEOrbit(tleParameters);
  }


  @RequestMapping(method=POST, value={"/api/model/geofield"},
          produces={"application/json"},
          consumes={"application/json"})
  public Map<String, Object>  calcGeoFieldFromJSON(@RequestBody GeoFieldParameters geoFieldParameters) {
    HtplUser user = (HtplUser) context.getAttribute("user_from_token");
    return calcGeoField(geoFieldParameters, user);
  }

  private Map<String, Object> calcGeoField(final GeoFieldParameters geoFieldParameters, HtplUser user) {
    return orbitService.calcGeoField(geoFieldParameters, user).getPackage();
  }


  @RequestMapping(method = RequestMethod.POST, value={"/api/orbits/add"},
          headers = "content-type=application/x-www-form-urlencoded")
  public Orbit createOrbitFromForm(@ModelAttribute OrbitFormData orbitFormData ) {
    return createOrbit(orbitFormData);
  }

  private Orbit createOrbit(OrbitFormData orbitFormData) {
    HtplUser user = (HtplUser) context.getAttribute("user_from_token");
    return orbitService.saveOrbit(orbitFormData, user);
  }

  private Map<String, Object> propagateOrbit(OrbitPropagationParameters orbitPropagationParameters) {
    HtplUser user = (HtplUser) context.getAttribute("user_from_token");
    return orbitService.propagateOrbit(orbitPropagationParameters, user).getPackage();
  }


  private void propagateTLEOrbit(TLEParameters tleParameters) {
    HtplUser user = (HtplUser) context.getAttribute("user_from_token");
    orbitService.propagateTLEOrbit(tleParameters, user);//.getPackage();
  }


  private String deleteOrbit(String orbit_id) {
    HtplUser user = (HtplUser) context.getAttribute("user_from_token");
    long result = orbitService.deleteOrbit(orbit_id, user);

    JSONObject jsonString = new JSONObject();

    try {
      if(result > 0) {
        jsonString.put("result", "success");
        jsonString.put("orbit", "Entity with id " + orbit_id + " deleted.");
      } else {
        jsonString.put("result", "error");
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return jsonString.toString();
  }

  @RequestMapping(method=POST, value={"/api/orbits/delete/{orbit_id}"})
  public Orbit deleteMessages(@PathVariable("orbit_id") String orbit_id) {
    HtplUser user = (HtplUser) context.getAttribute("user_from_token");
    Orbit orbit = orbitService.getOrbitById(orbit_id, user);
    orbitService.deleteOrbit(orbit);
    return orbit;
  }

}
