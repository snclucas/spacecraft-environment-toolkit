package com.blueapogee.model;

import org.springframework.data.annotation.Id;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;


public class HtplUser extends User {

  @Id
  private String id;


  public HtplUser(String username, String password, String id, Collection<? extends GrantedAuthority> authorities) {
    super(username, password, authorities);
    this.id = id;
  }

  public String getId() {
    return id;
  }

}
