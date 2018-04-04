package com.blueapogee.physics.AeAp9;


import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators;
import org.springframework.ui.Model;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ModelInputs {

  static BiPredicate<String, Double[]> predicateFluxOut = (l,s) -> {
    boolean returnValue = true;
    String[] tokens = l.trim().split(",");

    if(tokens.length<2) {
      return false;
    } else {
      for(int i = 1;i<tokens.length;i++) {
        double val = Double.parseDouble(tokens[i]);
        if(val<s[0] && val>s[1]) {
          returnValue = false;
        }
      }
    }
    return returnValue;
  };

  static BiPredicate<Double[], Double> predicateNumberRange = (l,s) -> {
    return s>l[0] && s<l[1];
  };

  static BiPredicate<List<String>, String> predicateStringList = (l,s) -> l.stream().anyMatch(str -> str.trim().equals(s));

  public enum ModelInput {
    ModelType("ModelType", predicateStringList, Arrays.asList("AP9", "AE9", "AE8", "AP8", "Plasma"), true, ""),
    ModelDB("ModelDB", predicateStringList, Collections.EMPTY_LIST, true, ""),
    MagfieldDB("MagfieldDB", predicateStringList, Collections.EMPTY_LIST, true, ""),
    KPhiNNetDB("KPhiNNetDB", predicateStringList, Collections.EMPTY_LIST, true, ""),
    KHminNNetDB("KHminNNetDB", predicateStringList, Collections.EMPTY_LIST, true, ""),
    FluxType("FluxType", predicateStringList, Arrays.asList("OnePtDiff", "TwoPtDiff", "Integral"), true, ""),

    FluxOut("FluxOut", predicateStringList, Arrays.asList("Mean", "Percentile", "Perturbed", "MonteCarlo"), true, ""),

    OutFile("OutFile", Collections.EMPTY_LIST, true, ""),
    OrbitFile("OrbitFile", Collections.EMPTY_LIST, true, "");

    private final String name;
    private final List<String> allowedValues;
    private final BiPredicate predicate;
    private final boolean required;
    private final String defaultValue;

    ModelInput(String name, BiPredicate<List<String>,String> predicate, final List<String> allowedValues,
               boolean required, String defaultValue) {
      this.name = name;
      this.allowedValues = allowedValues;
      this.predicate = predicate;
      this.required = required;
      this.defaultValue = defaultValue;
    }

    public String getName() {
      return name;
    }

    public String getDefault() {
      return defaultValue;
    }

    public boolean isValid(String text) {
      return predicateStringList.test(allowedValues, text);
    }

  }

  private Map<ModelInput, String[]> modelInputs = new LinkedHashMap<>();

  {
//    addModelInput(ModelInput.ModelType, "");
    modelInputs = Arrays.stream(ModelInput.values()).collect(
            Collectors.toMap(x -> x, x -> new String[]{"",""}));
  }


  public enum TimeSpecChoice {
    MJD,
    YrDDDFrac,
    YrDDDGmt,
    YrMoDaGmt,
    YrMoDaHrMnSc
  }

  public enum CoordSysChoice {
    GEI,
    GEO,
    GDZ,
    GSM,
    GSE,
    SM,
    MAG,
    SPH,
    RLL
  }

  public enum CoordUnitsChoice {
    Re,
    km
  }

  public enum DataDelimChoice {
    Comma,
    Space,
    Tab
  }

  public enum CoordOrderChoice {
    Standard,
    Inverted
  }


  // Basic inputs





  public List<Double> energies = new ArrayList<>();
  public List<Double> energies2 = new ArrayList<>();

  public List<FluxOut> fluxOutEntries = new ArrayList<>();

  public boolean fluenceOut;
  public boolean doseRateOut;
  public boolean doseAccumOut;

  //Advanced
  public TimeSpecChoice timeSpec = TimeSpecChoice.MJD;
  public TimeSpecChoice inTimeSpec = TimeSpecChoice.MJD;

  public CoordSysChoice coordSys = CoordSysChoice.GEI;
  public CoordSysChoice inCoordSys = CoordSysChoice.GEI;

  public CoordUnitsChoice coordUnits = CoordUnitsChoice.Re;
  public CoordUnitsChoice inCoordUnits = CoordUnitsChoice.Re;

  public CoordOrderChoice coordOrder = CoordOrderChoice.Standard;
  public CoordOrderChoice inCoordOrder = CoordOrderChoice.Standard;

  public DataDelimChoice dataDelim = DataDelimChoice.Comma;
  public DataDelimChoice inDataDelim = DataDelimChoice.Comma;

  private void checkInputs() {

   // if(modelType == null) {
    //  throw new IllegalArgumentException("ModelType is required");
    //}

   // if(modelDB == null) {
   ///   throw new IllegalArgumentException("ModelDB is required");
    //}

    //if (fluxType == FluxTypeChoice.TwoPtDiff && energies.size() == 0 && energies2.size() == 0) {
    //  throw new IllegalArgumentException("Two-point differential requires ‘Energies’ and ‘Energies2’ parameter values.");
    //}

    for(FluxOut fluxOut: fluxOutEntries) {
    //  if (modelType == ModelTypeChoice.Plasma &&
     //         fluxOut.getFluxOutChoice() == FluxOut.FluxOutChoice.MonteCarlo) {
     //   throw new IllegalArgumentException("FluxType: MonteCarlo is not applicable for ModelType: Plasma");
     // }

      for (String val : fluxOut.getValuesList()) {
        // Check for range
        String[] range = new String[]{val};
        if (val.contains("-")) {
          range = val.split("-");
        }
        for (String r : range){
          double rd = Double.parseDouble(r);
          if (fluxOut.getFluxOutChoice() == FluxOut.FluxOutChoice.MonteCarlo) {
            if (rd < 0 || rd > 999)
              throw new IllegalArgumentException("Flux out values need to be between 0-999 for MonteCarlo");
          } else {
            if (rd < 0 || rd > 99)
              throw new IllegalArgumentException("Flux out values need to be between 0-99 for Mean, Percentile and Perturbed");
          }
        }
      }
    }

  }


  public void addModelInput(final ModelInput modelInput, String value, String comment) {
    if(modelInput.isValid(value)) {
      modelInputs.put(modelInput, new String[]{value, comment});
    } else {
      if(modelInput.allowedValues.size()>0) {
        throw new IllegalArgumentException(modelInput.name() + " input can only take the following values: " +
                listToString(modelInput.allowedValues));
      } else {
        throw new IllegalArgumentException(modelInput.name() + " input cannot be empty.");
      }
    }

  }

  public void addModelInput(final ModelInput modelInput, String value) {
    addModelInput(modelInput, value, "");
  }

  private String listToString(List<String> list) {
    return list.stream().map(Object::toString).collect(Collectors.joining(","));
  }





  public List<Double> getEnergiesAsList() {
    return energies;
  }

  public String getEnergiesAsString() {
    return energies.stream().map(Object::toString).collect(Collectors.joining(","));
  }

  public void setEnergies(final List<Double> energies) {
    this.energies = energies;
  }

  public List<Double> getEnergies2AsList() {
    return energies2;
  }

  public String getEnergies2AsString() {
    return energies2.stream().map(Object::toString).collect(Collectors.joining(","));
  }

  public void setEnergies2(final List<Double> energies2) {
    this.energies2 = energies2;
  }

  public List<FluxOut> getFluxOutList() {
    return fluxOutEntries;
  }

  public FluxOut getFluxOut(int index) {
    return fluxOutEntries.get(index);
  }

  public String getFluxOutAsString(int index) {
    FluxOut fluxOut = getFluxOut(index);
    return fluxOut.getFluxOutChoice().toString() + "," + fluxOut.getValuesString();
  }

  public void addFluxOut(final FluxOut fluxOut) {
    this.fluxOutEntries.add(fluxOut);
  }

  public boolean isFluenceOut() {
    return fluenceOut;
  }

  public void setFluenceOut(final boolean fluenceOut) {
    this.fluenceOut = fluenceOut;
  }

  public boolean isDoseRateOut() {
    return doseRateOut;
  }

  public void setDoseRateOut(final boolean doseRateOut) {
    this.doseRateOut = doseRateOut;
  }

  public boolean isDoseAccumOut() {
    return doseAccumOut;
  }

  public void setDoseAccumOut(final boolean doseAccumOut) {
    this.doseAccumOut = doseAccumOut;
  }

  public void addFluxOut(FluxOut.FluxOutChoice fluxOutType, String... valuesIn) {
    List<String> tempVals = new ArrayList<>();
    this.fluxOutEntries.add(new FluxOut(fluxOutType, tempVals.toArray(new String[0])));
  }



  public String getInputFile() {
    StringBuilder inputFileBuilder = new StringBuilder();

    for (Map.Entry<ModelInput, String[]> entry : modelInputs.entrySet()) {

      if(!entry.getKey().isValid(entry.getValue()[0])) {
        if(entry.getKey().allowedValues.size()>0) {
          throw new IllegalArgumentException(entry.getKey().name() + " input can only take the following values: " +
                  listToString(entry.getKey().allowedValues));
        } else {
          throw new IllegalArgumentException(entry.getKey().name() + " input cannot be empty.");
        }
      }

      if(!entry.getValue()[1].equalsIgnoreCase("")) {
        inputFileBuilder.append("# " + entry.getValue()[1]).append(System.lineSeparator());
      }
      inputFileBuilder.append(entry.getKey().name() + ": ").append(entry.getValue()[0]).append(System.lineSeparator());

      // if("something".equals(entry.getValue())){
     //   result = entry.getValue();
     // }
    }

   // modelInputs.entrySet().stream()
    //        .map()
    //        .filter(map -> map.getKey() == 2)
     //       .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));

   // inputFileBuilder.append("ModelType:").append(this.getModelType()).append(System.lineSeparator());
   // inputFileBuilder.append("ModelDB:").append(this.getModelDB()).append(System.lineSeparator());

    return inputFileBuilder.toString();
  }


  public void getModelInputValue(ModelInput input) {
    if(modelInputs.containsKey(input)) {

    }
  }


}

class FluxOut {
  public enum FluxOutChoice {
    Mean,
    Percentile,
    Perturbed,
    MonteCarlo
  }

  private FluxOutChoice fluxOutChoice;

  private List<String> values = new ArrayList<>();

  public FluxOut(FluxOutChoice fluxOutType, String... valuesIn) {
    this.fluxOutChoice = fluxOutType;
    values.addAll(Arrays.asList(valuesIn));
  }

  public FluxOutChoice getFluxOutChoice() {
    return fluxOutChoice;
  }

  public String getValuesString() {
    return values.stream().map(Object::toString).collect(Collectors.joining(","));
  }

  public List<String> getValuesList() {
    return values;
  }

  public String[] getValuesArray() {
    return values.toArray(new String[0]);
  }



}

class BasicModelInput implements ModelInput {

  private final String name;
  private final String value;
  private final String comment;

  public BasicModelInput(final String name, final String value, final String comment) {
    this.name = name;
    this.value = value;
    this.comment = comment;
  }

  @Override
  public String getValue() {
    return value;
  }

  @Override
  public String getComment() {
    return comment;
  }

  @Override
  public String getStringOutput() {
    StringBuilder output = new StringBuilder();
    output.append("# " + comment).append(System.lineSeparator());
    output.append(name + ": " + getValue()).append(System.lineSeparator());
    return output.toString();
  }
}

class ClosedSetModelInput implements ModelInput {

  private final String name;
  private final String value;
  private final String[] allowedValues;
  private final String comment;

  public ClosedSetModelInput(final String name, final String[] allowedValues, final String value, final String comment) {
    this.name = name;
    this.allowedValues = allowedValues;
    this.value = value;
    this.comment = comment;
  }

  @Override
  public String getValue() {
    return value;
  }

  @Override
  public String getComment() {
    return comment;
  }

  @Override
  public String getStringOutput() {
    StringBuilder output = new StringBuilder();
    output.append("# " + comment).append(System.lineSeparator());
    output.append(name + ": " + getValue()).append(System.lineSeparator());
    return output.toString();
  }
}