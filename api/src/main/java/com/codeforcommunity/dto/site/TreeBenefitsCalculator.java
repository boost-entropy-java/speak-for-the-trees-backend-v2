package com.codeforcommunity.dto.site;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TreeBenefitsCalculator {
  static final String region = "NoEastXXX";
  static final String dataFolder = "/OTMData";
  static final double[] intervals = new double[] {
          3.81, 11.43, 22.86,	38.10,	53.34,	68.58,	83.82,	99.06,	114.30};
  final String speciesCode;
  final double diameterCM;

  // diameter in inches
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
      throw new IllegalArgumentException("Unable to read " + path);
    }
    return df;
  }

  // helper to query species_master_list.csv and get the corresponding code
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
    throw new IllegalArgumentException("Unable to find entry with name " + commonName);
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
    throw new IllegalArgumentException(
            "Unable to locate entry at row " + rowName + " and column " + colName);
  }

  // helper to calculate the interpolated value of the given property
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

    //interpolate value
    double m = (y1-y0) / (x1-x0);
    double b = y0 - (x0*m);
    double value = m*diameterCM + b;

    return value;
  }

  /**
   * Calculates the total energy conserved (from natural gas and electricity) in kWh
   */
  public double calcEnergy() {
    return calcProperty("natural_gas") + calcProperty("electricity");
  }

  /**
   * Calculates the total water filtered in gal
   */
  public double calcStormwater() {
    return calcProperty("hydro_interception");
  }

  /**
   * Calculates the air quality improved based on the avoidance
   * and deposition of various compounds in kg:
   * nitrous oxide (NOx) avoided
   * nitrous oxide (NOx) deposition
   * ozone deposition
   * PM10 avoided
   * PM10 deposition
   * sulfur oxide (SOx) avoided
   * sulfur oxide (SOx) deposition
   * volatile organic compounds (VOCs) avoided
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

  /**
   * Calculates the total carbon dioxide removed (from avoiding and sequestering) in lbs
   */
  public double calcCo2Removed() {
    return calcProperty("co2_avoided") + calcProperty("co2_sequestered");
  }

  /**
   * Calculates the total carbon dioxide stored in lbs
   */
  public double calcCo2Stored() {
    return calcProperty("co2_storage");
  }
}
