package com.codeforcommunity.dto.site;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TreeBenefitsCalculator {
  static final String region = "NoEastXXX";
  static final String dataFolder = "OTMData";
  static final double[] intervals = new double[] {
          3.81, 11.43, 22.86,	38.10,	53.34,	68.58,	83.82,	99.06,	114.30};
  final String speciesCode;
  final double diameterCM;
  /**
   * For each returned value, the corresponding columns to add/aggregate are:
   * energy: natural_gas , electricity
   * stormwater: hydro_interception
   * airQuality: all columns prefixed with aq
   * co2Removed: co2_sequestered, co2_avoided
   * co2Stored: co2_storage
   */
  public TreeBenefitsCalculator(String commonName, double diameter) {
    this.speciesCode = getSpeciesCode(commonName);
    this.diameterCM = diameter*2.54;
  }

  // helper to get the path of the csv with the given property data
  private String getCSVPath(String property) {
    return dataFolder + "/" + "output__" + region + "__" + property + ".csv";
  }

  // helper to read in a csv as a matrix of strings
  private List<List<String>> readCSV(String path) {
    List<List<String>> df = new ArrayList();
    try (BufferedReader br = new BufferedReader(new FileReader(path))) {
      String line;
      while ((line = br.readLine()) != null) {
        String[] values = line.split(",");
        df.add(Arrays.asList(values));
      }
    } catch (IOException e) {
      throw new IllegalArgumentException();
    }
    return df;
  }

  // queries species_master_list.csv to get the corresponding code
  private String getSpeciesCode(String commonName) {
    List<List<String>> codeDF = readCSV(dataFolder + "/species_master_list.csv");
    int comNameIdx = 2;
    int codeIdx = 4;

    for (int i=1; i<=codeDF.size(); i++) {
      List<String> row = codeDF.get(i);
      if (row.get(comNameIdx).equals(commonName)) {
        return row.get(codeIdx);
      }
    }
    // query does not exist
    throw new IllegalArgumentException();
  }

  // helper to query the value at the given row and column names of a matrix
  private String queryRowCol(List<List<String>> df, String rowName, String colName) {
    int colIdx = df.get(0).indexOf(colName);
    for (int i=1; i<=df.size(); i++) {
      List<String> row = df.get(i);
      if (row.get(0).equals(rowName)) {
        return row.get(colIdx);
      }
    }
    // query does not exist
    throw new IllegalArgumentException();
  }

  // interpolates the corresponding y value of an input x given two points (x0,y0) and (x1, y1)
  private double interpolate(double x, double x0, double x1, double y0, double y1) {
    double m = (y1-y0) / (x1-x0);
    double b = y0 - (x0*m);
    double y = m*x + b;
    return y;
  }

  private double calcProperty(String property) {
    String path = getCSVPath(property);
    List<List<String>> codeDF = readCSV(path);
    double x0 = 0;
    double x1 = 0;
    for (int i=0; i<intervals.length; i++) {
      if (intervals[i]<=diameterCM) {
        x0 = intervals[i];
        x1 = intervals[i+1];
      }
    }
    if (x0==0 || x1==0) {
      throw new IllegalArgumentException();
    }
    double y0 = Double.parseDouble(queryRowCol(codeDF, speciesCode, String.valueOf(x0)));
    double y1 = Double.parseDouble(queryRowCol(codeDF, speciesCode, String.valueOf(x1)));

    double value = interpolate(diameterCM, x0, x1, y0, y1);
    return value;
  }

  // calculates the total energy conserved (from natural gas and electricity) in kWh
  public double calcEnergy() {
    return calcProperty("natural_gas") + calcProperty("electricity");
  }

  // calculates the total water filtered in gal
  public double calcStormwater() {
    return calcProperty("hydro_interception");
  }

  /**
   * calculates the air quality improved based on:
   * nitrous oxide (NOx) avoided (kg)
   * nitrous oxide (NOx) deposition (kg)
   * ozone deposition (kg)
   * PM10 avoided (kg)
   * PM10 deposition (kg)
   * sulfur oxide (SOx) avoided (kg)
   * sulfur oxide (SOx) deposition (kg)
   * volatile organic compounds (VOCs) avoided (kg)
   */
  public double calcAirQuality() {
    return calcProperty("aq_nox_avoided") +
            calcProperty("aq_nox_dep") +
                calcProperty("aq_ozone_dep") +
                  calcProperty("aq_p10_avoided") +
                    calcProperty("aq_p10_dep") +
                      calcProperty("aq_sox_avoided") +
                        calcProperty("aq_sox_dep") +
                          calcProperty("aq_voc_avoided");
  }
  // calculates the total carbon dioxide removed (from avoiding and sequestering) in lbs
  public double calcCo2Removed() {
    return calcProperty("co2_avoided") + calcProperty("co2_sequestered");
  }

  // calculates the total carbon dioxide stored in lbs
  public double calcCo2Stored() {
    return calcProperty("co2_storage");
  }
}
