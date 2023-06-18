package com.codeforcommunity.benefits;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jooq.DSLContext;
import org.jooq.generated.tables.records.TreeBenefitsRecord;
import org.jooq.generated.tables.records.TreeSpeciesRecord;

public class TreeBenefitsCalculator {
  static final String region = "NoEastXXX";
  static final double[] intervals = new double[] {
          3.81, 11.43, 22.86,	38.10,	53.34,	68.58,	83.82,	99.06,	114.30};
  static final Map<String, Double> currencyConversion = new HashMap<String, Double>() {{
            put("electricity_kwh_to_currency", 0.1401);
            put("natural_gas_kbtu_to_currency", 0.01408);
            put("h20_gal_to_currency", 0.0008);
            put("co2_lb_to_currency", 0.00334);
            put("o3_lb_to_currency", 4.59);
            put("nox_lb_to_currency", 4.59);
            put("pm10_lb_to_currency", 8.31);
            put("sox_lb_to_currency", 3.48);
            put("voc_lb_to_currency", 2.31);
  }};
  final String speciesCode;
  final double diameterCM;
  private final DSLContext db;

  // diameter in inches
  public TreeBenefitsCalculator(DSLContext db, String commonName, double diameter) {
    this.db = db;
    this.speciesCode = getSpeciesCode(commonName);
    this.diameterCM = diameter*2.54;
  }

  // helper to query species_master_list.csv and get the corresponding code
  private String getSpeciesCode(String commonName) {
    TreeSpeciesRecord record = db.selectFrom(TREE_SPECIES)
            .where(TREE_SPECIES.COMMON_NAME.eq(commonName)).fetchOne();
    if (record == null) {
      throw new IllegalArgumentException("Unable to find entry with name " + commonName);
    }
    String speciesCode = record.getSpeciesCode();
    return speciesCode;
  }

  // helper to calculate the interpolated value of the given property
  private double calcProperty(String property) {
    double x0 = 0;
    double x1 = 0;
    for (int i=0; i<intervals.length; i++) {
      if (intervals[i]<=diameterCM) {
        x0 = intervals[i];
        x1 = intervals[i+1];
      }
    }
    if (x0==0 || x1==0) {
      throw new IllegalArgumentException("diameter must be non-zero");
    }
    TreeBenefitsRecord y0Record = db.select().from(TREE_BENEFITS)
            .where(TREE_BENEFITS.SPECIES_CODE.eq(speciesCode))
            .and(TREE_BENEFITS.DIAMETER.eq(x0)).fetchOne();
    TreeBenefitsRecord y1Record = db.selectFrom(TREE_BENEFITS)
            .where(TREE_BENEFITS.SPECIES_CODE.eq(speciesCode))
            .and(TREE_BENEFITS.DIAMETER.eq(x1)).fetchOne();

    if (y0Record==null || y1Record==null) {
      throw new ResourceDoesNotExistException(speciesCode, "tree benefits entry");
    }

    double y0 = r0Record.get
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
   * Calculates the total energy conserved (from natural gas and electricity) in kWh
   */
  public double calcEnergyMoney() {
    return calcProperty("natural_gas")* currencyConversion.get("natural_") + calcProperty("electricity");
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
