package com.blueapogee.physics.AeAp9;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ModelInputs {

  public enum ModelTypeChoice {
    AE9,
    AP9,
    Plasma,
    AE8,
    AP8
  }

  public enum FluxTypeChoice {
    OnePtDiff,
    TwoPtDiff,
    Integral
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
  public ModelTypeChoice modelType;
  public BasicModelInput<String> modelDB;
  public String magFieldDB;
  public String KPhiNNetDB;
  public String KHminNNetDB;
  public String outFile;
  public String orbitFile;

  public FluxTypeChoice fluxType;

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

    if(modelType == null) {
      throw new IllegalArgumentException("ModelType is required");
    }

    if(modelDB == null) {
      throw new IllegalArgumentException("ModelDB is required");
    }

    if (fluxType == FluxTypeChoice.TwoPtDiff && energies.size() == 0 && energies2.size() == 0) {
      throw new IllegalArgumentException("Two-point differential requires ‘Energies’ and ‘Energies2’ parameter values.");
    }

    for(FluxOut fluxOut: fluxOutEntries) {
      if (modelType == ModelTypeChoice.Plasma &&
              fluxOut.getFluxOutChoice() == FluxOut.FluxOutChoice.MonteCarlo) {
        throw new IllegalArgumentException("FluxType: MonteCarlo is not applicable for ModelType: Plasma");
      }

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

  List<ModelInput> modelInputs = new ArrayList<>();

  public void addModelInput(final String name, T value, String comment) {
    modelInputs.add(new BasicModelInput(name, value, comment))
  }

  public ModelTypeChoice getModelType() {
    return modelType;
  }

  public void setModelType(final ModelTypeChoice modelType) {
    this.modelType = modelType;
  }

  public BasicModelInput<String> getModelDB() {
    return modelDB;
  }

  public void setModelDB(final String namemodelDB) {
    this.modelDB = modelDB;
  }

  public String getMagFieldDB() {
    return magFieldDB;
  }

  public void setMagFieldDB(final String magFieldDB) {
    this.magFieldDB = magFieldDB;
  }

  public String getKPhiNNetDB() {
    return KPhiNNetDB;
  }

  public void setKPhiNNetDB(final String KPhiNNetDB) {
    this.KPhiNNetDB = KPhiNNetDB;
  }

  public String getKHminNNetDB() {
    return KHminNNetDB;
  }

  public void setKHminNNetDB(final String KHminNNetDB) {
    this.KHminNNetDB = KHminNNetDB;
  }

  public String getOutFile() {
    return outFile;
  }

  public void setOutFile(final String outFile) {
    this.outFile = outFile;
  }

  public String getOrbitFile() {
    return orbitFile;
  }

  public void setOrbitFile(final String orbitFile) {
    this.orbitFile = orbitFile;
  }


  public FluxTypeChoice getFluxType() {
    return fluxType;
  }

  public void setFluxType(final FluxTypeChoice fluxType) {
    this.fluxType = fluxType;
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
    inputFileBuilder.append("ModelType:").append(this.getModelType()).append(System.lineSeparator());
    inputFileBuilder.append("ModelDB:").append(this.getModelDB()).append(System.lineSeparator());

    return inputFileBuilder.toString();
  }


  public static void main(String[] args) {
    ModelInputs modelInputs = new ModelInputs();

    modelInputs.addModelInput("ds", "sdsd", "sdsds");

    modelInputs.setModelType(ModelTypeChoice.AE9);
    modelInputs.setModelDB("../../modelData/AP9V15_runtime_tables.mat");
    modelInputs.checkInputs();
    System.out.println(modelInputs.getInputFile());
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