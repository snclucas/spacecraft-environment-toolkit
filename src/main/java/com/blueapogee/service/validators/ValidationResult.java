package com.blueapogee.service.validators;


public class ValidationResult {
  public boolean success;
  public String message;


  public ValidationResult(final boolean success, final String message) {
    this.success = success;
    this.message = message;
  }
}
