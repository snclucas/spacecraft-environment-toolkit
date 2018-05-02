package com.blueapogee.physics.AeAp9;


import java.io.*;

public class Driver {

  public Driver() {

    File ap9ae9folder = new File(Thread.currentThread().getContextClassLoader().getResource("Ae9Ap9").getFile());

    ModelInputs modelInputs = new ModelInputs();
    modelInputs.addModelInput(ModelInputs.ModelInput.ModelType, "AE9", "This is the model type");
    modelInputs.addModelInput(ModelInputs.ModelInput.ModelDB, ap9ae9folder + "/modelData/AP9V15_runtime_tables.mat");
    modelInputs.addModelInput(ModelInputs.ModelInput.FluxType, "1PtDiff");
    modelInputs.addModelInput(ModelInputs.ModelInput.KHminNNetDB, ap9ae9folder + "/modelData/fast_hmin_net.mat");
    modelInputs.addModelInput(ModelInputs.ModelInput.KPhiNNetDB, ap9ae9folder + "/modelData/fastPhi_net.mat");
    modelInputs.addModelInput(ModelInputs.ModelInput.MagfieldDB, ap9ae9folder + "/modelData/igrfDB.h5");

    modelInputs.addModelInput(ModelInputs.ModelInput.Energies, "1,2,5,10");

    modelInputs.addModelInput(ModelInputs.ModelInput.OutFile, ap9ae9folder + "/myOutput.txt");
    modelInputs.addModelInput(ModelInputs.ModelInput.OrbitFile, ap9ae9folder + "/ephem_sat.dat");

    modelInputs.addModelInput(ModelInputs.ModelInput.FluxOut, "Percentile,10,20,50,99");

    System.out.println(modelInputs.getInputFile());


    try {

      File outputFile = new File("D:\\input.txt");

      BufferedWriter writer = null;
      writer = new BufferedWriter(new FileWriter(new File("D:\\input.txt")));
      writer.write(modelInputs.getInputFile());
      writer.flush();
      writer.close();

      ProcessBuilder pb = new ProcessBuilder(ap9ae9folder.toString() + "\\bin\\win64\\CmdLineAe9Ap9.exe", "-i", outputFile.getAbsolutePath());
      //pb.redirectOutput(ProcessBuilder.Redirect.PIPE);
      pb= pb.directory(new File("D:\\ap9\\"));
      pb = pb.redirectOutput(new File("d:/out.txt"));
      pb= pb.redirectError(new File("d:/err.txt"));

      final Process p=pb.start();

      BufferedReader br=new BufferedReader(new InputStreamReader(p.getInputStream()));

      String line;
      while((line=br.readLine())!=null){
        System.out.println(line);
      }
    } catch (Exception ex) {
      System.out.println(ex);
    }



  }


  public static void main(String[] args) {
    new Driver();
  }


}
