package com.blueapogee.controller;

import com.blueapogee.model.HtplUser;
import com.blueapogee.model.Orbit;
import com.blueapogee.model.form.OrbitFormData;
import com.blueapogee.service.OrbitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class OrbitController {

  @Autowired
  private OrbitService orbitService;

  @RequestMapping(method = GET, value = {"/orbits"})
  public ModelAndView getOrbits(
          @AuthenticationPrincipal UserDetails user,
          @RequestParam(value = "sortby", defaultValue = "created_at") String sortby,
          @RequestParam(value = "order", defaultValue = "asc") String order,
          @RequestParam(value = "page", defaultValue = "1") int page,
          @RequestParam(value = "limit", defaultValue = "100") int limit,
          @RequestParam(value = "privacy", defaultValue = "public") String privacy,
          @RequestParam(value = "tags", defaultValue = "") String tags) {

    Sort sort = new Sort(
            order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortby);
    HtplUser htplUser = (HtplUser) user;

    Page<Orbit> orbits = orbitService.getAllOrbits(htplUser, sort, page, limit);

    ModelAndView mav = new ModelAndView("orbits");
    mav.addObject("orbitFormData", new OrbitFormData());
    mav.addObject("orbits", orbits);
    return mav;

  }


  }
