package com.blueapogee.controller.rest;

import com.blueapogee.model.HtplUser;
import com.blueapogee.model.Orbit;
import com.blueapogee.model.form.OrbitFormData;
import com.blueapogee.model.form.OrbitPropagationFormData;
import com.blueapogee.service.OrbitService;

import com.blueapogee.service.OrbitTrace;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
    return orbitService.getOrbitById(id);
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

  @RequestMapping(method=POST, value={"/api/orbits/add"},
          produces={"application/json"},
          consumes={"application/json"})
  public Orbit createOrbitFromJSON(@RequestBody OrbitFormData orbitFormData ) {
    return createOrbit(orbitFormData);
  }


  @RequestMapping(method=POST, value={"/api/orbits/propagate"},
          produces={"application/json"},
          consumes={"application/json"})
  public OrbitTrace propagateOrbitFromJSON(@RequestBody OrbitPropagationFormData orbitPropagationFormData) {
    return propagateOrbit(orbitPropagationFormData);
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

  private OrbitTrace propagateOrbit(OrbitPropagationFormData orbitPropagationFormData) {
    HtplUser user = (HtplUser) context.getAttribute("user_from_token");
    return orbitService.propagateOrbit(orbitPropagationFormData, user);
  }

  private String deleteOrbit(String orbit_id) {
    HtplUser user = (HtplUser) context.getAttribute("user_from_token");
    long result = orbitService.deleteOrbit(user, orbit_id);

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
    Orbit orbit = orbitService.getOrbitById(orbit_id);
    orbitService.deleteOrbit(orbit);
    return orbit;
  }

}
