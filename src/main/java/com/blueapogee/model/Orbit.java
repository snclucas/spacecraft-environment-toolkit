package com.blueapogee.model;

import com.blueapogee.model.form.OrbitFormData;
import com.blueapogee.model.form.OrbitParameters;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Document(collection = "orbits")
public class Orbit {

  @Id
  public String id;

  public String type = "";
  public String name = "";
  public String fromDate = "";
  public String toDate = "";
  public String created_at;
  public boolean isPrivate = true;

  public double epoch = 0.0;
  public double eccentricity = 0.0; //eccentricity
  public double semiMajorAxis = 0.0;
  public double inclination = 0.0;
  public double RAAN = 0.0;
  public double argumentOfPerigee = 0.0;
  public double trueAnomaly = 0.0;
  public double meanAnomaly = 0.0;

  private String user;

  public Orbit() {
    this.id = new ObjectId().toString();
  }

  public Orbit(final String type, final String name, final String fromDate, final String toDate, final double epoch,
               final double eccentricity, final double semiMajorAxis, final double inclination,
               final double RAAN, final double argumentOfPerigee, final double trueAnomaly) {
    TimeZone tz = TimeZone.getTimeZone("UTC");
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
    df.setTimeZone(tz);

    this.type = type;
    this.name = name;

    this.fromDate =  fromDate.equals("") ? df.format(new Date()) : fromDate;
    this.toDate =  toDate.equals("") ? df.format(addYear(new Date())) : toDate;
    this.epoch = epoch;
    this.eccentricity = eccentricity;
    this.semiMajorAxis = semiMajorAxis;
    this.inclination = inclination;
    this.RAAN = RAAN;
    this.argumentOfPerigee = argumentOfPerigee;
    this.trueAnomaly = trueAnomaly;


    this.created_at = df.format(new Date());
  }

  public Orbit(final OrbitParameters orbitParameters) {
    this(orbitParameters.type, orbitParameters.name, orbitParameters.fromDate, orbitParameters.toDate, orbitParameters.epoch,
            orbitParameters.eccentricity, orbitParameters.semiMajorAxis, orbitParameters.inclination,
            orbitParameters.RAAN, orbitParameters.argumentOfPerigee, orbitParameters.trueAnomaly);
  }

  public String getUser() {
    return user;
  }

  public void setUser(final String user) {
    this.user = user;
  }

  public String getType() {
    return type;
  }

  public void setType(final String type) {
    this.name = type;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String getFromDate() {
    return fromDate;
  }

  public void setFromDate(final String fromDate) {
    this.fromDate = fromDate;
  }

  public String getToDate() {
    return toDate;
  }

  public void setToDate(final String toDate) {
    this.toDate = toDate;
  }

  public double getEpoch() {
    return epoch;
  }

  public void setEpoch(final double epoch) {
    this.epoch = epoch;
  }

  public double getEccentricity() {
    return eccentricity;
  }

  public void setEccentricity(final double eccentricity) {
    this.eccentricity = eccentricity;
  }

  public double getSemiMajorAxis() {
    return semiMajorAxis;
  }

  public void setSemiMajorAxis(final double semiMajorAxis) {
    this.semiMajorAxis = semiMajorAxis;
  }

  public double getInclination() {
    return inclination;
  }

  public void setInclination(final double inclination) {
    this.inclination = inclination;
  }

  public double getRAAN() {
    return RAAN;
  }

  public void setRAAN(final double RAAN) {
    this.RAAN = RAAN;
  }

  public double getArgumentOfPerigee() {
    return argumentOfPerigee;
  }

  public void setArgumentOfPerigee(final double argumentOfPerigee) {
    this.argumentOfPerigee = argumentOfPerigee;
  }

  public double getTrueAnomaly() {
    return trueAnomaly;
  }

  public void setTrueAnomaly(final double trueAnomaly) {
    this.trueAnomaly = trueAnomaly;
  }

  public void setCreatedOnNow() {
    TimeZone tz = TimeZone.getTimeZone("UTC");
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
    df.setTimeZone(tz);
    this.created_at = df.format(new Date());
  }

  public Date addYear(Date toDate) {
    Calendar c = Calendar.getInstance();
    c.setTime(toDate);
    c.add(Calendar.YEAR, 1);
    return c.getTime();
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    final Orbit orbit = (Orbit) o;

    return id != null ? id.equals(orbit.id) : orbit.id == null;
  }

  @Override
  public int hashCode() {
    return id != null ? id.hashCode() : 0;
  }
}
