package com.blueapogee.service.validators;

import com.blueapogee.service.parameters.PropagationParameters;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class OrbitPropagationValidator {

  public static List<String> validIntegrators = Arrays.asList("HolmesFeatherstone");

  public static List<String> validForceModels = Arrays.asList("Newtonian");

  public static ValidationResult validate(PropagationParameters propagationParameters) {

    if(propagationParameters.maxStep <= 0) {
      return new ValidationResult(false, "Parameter maxStep needs to be > 0");
    }

    if(propagationParameters.minStep <= 0) {
      return new ValidationResult(false, "Parameter minStep needs to be > 0");
    }

    if(propagationParameters.minStep > propagationParameters.maxStep) {
      return new ValidationResult(false, "Parameter minStep less than parameter maxStep.");
    }

    boolean validForceModel = validForceModels.stream().filter(
            fm -> fm.equalsIgnoreCase(propagationParameters.forceModel)).count() > 0;

    if(!validForceModel) {
      return new ValidationResult(false, "Parameter forceModel should be either [" +
              validForceModels.stream().collect(Collectors.joining(",")) + "]");
    }

    boolean validIntegrator = validIntegrators.stream().filter(
            fm -> fm.equalsIgnoreCase(propagationParameters.integrator)).count() > 0;

    if(!validIntegrator) {
      return new ValidationResult(false, "Parameter integrator should be either [" +
              validIntegrators.stream().collect(Collectors.joining(",")) + "]");
    }

    return new ValidationResult(true, "Valid");
  }

}
