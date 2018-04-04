package com.blueapogee.physics.AeAp9;


public class Driver {

  public Driver() {
    ModelInputs modelInputs = new ModelInputs();
    modelInputs.addModelInput(ModelInputs.ModelInput.ModelType, "AE9", "This is the model type");
    modelInputs.addModelInput(ModelInputs.ModelInput.ModelDB, "../../modelData/AP9V15_runtime_tables.mat");
    modelInputs.addModelInput(ModelInputs.ModelInput.FluxType, "OnePtDiff");
    modelInputs.addModelInput(ModelInputs.ModelInput.KHminNNetDB, "../../modelData/fast_hmin_net.mat");
    modelInputs.addModelInput(ModelInputs.ModelInput.KPhiNNetDB, "../../modelData/fastPhi_net.mat");
    modelInputs.addModelInput(ModelInputs.ModelInput.MagfieldDB, "../../modelData/igrfDB.h5");
    System.out.println(modelInputs.getInputFile());
  }


  public static void main(String[] args) {
    new Driver();
  }


}
