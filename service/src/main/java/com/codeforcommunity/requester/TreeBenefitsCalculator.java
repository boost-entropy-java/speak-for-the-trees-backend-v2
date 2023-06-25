package com.codeforcommunity.requester;

import static org.jooq.generated.Tables.TREE_BENEFITS;
import static org.jooq.generated.Tables.TREE_SPECIES;

import com.codeforcommunity.exceptions.ResourceDoesNotExistException;
import java.util.HashMap;
import java.util.Map;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.generated.tables.records.TreeSpeciesRecord;

public class TreeBenefitsCalculator {
  private final DSLContext db;
  final String speciesCode;
  final double diameterCM;
  static final double[] intervals =
      new double[] {3.81, 11.43, 22.86, 38.10, 53.34, 68.58, 83.82, 99.06, 114.30};
  final double x0;
  final double x1;
  static final Map<String, Double> currencyConversion =
      new HashMap<String, Double>() {
        {
          put("electricity_kwh_to_currency", 0.1401);
          put("natural_gas_kbtu_to_currency", 0.01408);
          put("h20_gal_to_currency", 0.0008);
          put("co2_lb_to_currency", 0.00334);
          put("o3_lb_to_currency", 4.59);
          put("nox_lb_to_currency", 4.59);
          put("pm10_lb_to_currency", 8.31);
          put("sox_lb_to_currency", 3.48);
          put("voc_lb_to_currency", 2.31);
        }
      };

  // diameter in inches
  public TreeBenefitsCalculator(DSLContext db, String commonName, double diameter) {
    this.db = db;
    this.speciesCode = getSpeciesCode(commonName);
    this.diameterCM = diameter * 2.54;

    double x0 = 0;
    double x1 = 0;
    for (int i = 0; i < intervals.length; i++) {
      if (intervals[i] <= diameterCM) {
        x0 = intervals[i];
        x1 = intervals[i + 1];
      }
    }
    if (x0 == 0 || x1 == 0) {
      throw new IllegalArgumentException("diameter must be non-zero");
    }
    this.x0 = x0;
    this.x1 = x1;
  }

  // helper to query the species code from the tree_species table
  private String getSpeciesCode(String commonName) {
    TreeSpeciesRecord record =
        db.selectFrom(TREE_SPECIES).where(TREE_SPECIES.COMMON_NAME.eq(commonName)).fetchOne();
    if (record == null) {
      throw new IllegalArgumentException("Unable to find entry with name " + commonName);
    }
    String speciesCode = record.getSpeciesCode();
    return speciesCode;
  }

  // helper to calculate the interpolated value of the given property
  private double calcProperty(Field property) {
    double y0 =
        db.selectFrom(TREE_BENEFITS)
            .where(TREE_BENEFITS.SPECIES_CODE.eq(speciesCode))
            .and(TREE_BENEFITS.DIAMETER.eq(x0))
            .fetchOne(property, Double.class);
    double y1 =
        db.selectFrom(TREE_BENEFITS)
            .where(TREE_BENEFITS.SPECIES_CODE.eq(speciesCode))
            .and(TREE_BENEFITS.DIAMETER.eq(x1))
            .fetchOne(property, Double.class);

    if (y0 == 0 || y1 == 0) {
      throw new ResourceDoesNotExistException(0, speciesCode + "tree benefits entry");
    }

    // interpolate value
    double m = (y1 - y0) / (x1 - x0);
    double b = y0 - (x0 * m);
    double value = m * diameterCM + b;

    return value;
  }

  /** Calculates the total energy conserved (from natural gas and electricity) in kWh */
  public double calcEnergy() {
    // natural gas Kbtu to kWh
    return (calcProperty(TREE_BENEFITS.NATURAL_GAS) / 3.4121416)
        + calcProperty(TREE_BENEFITS.ELECTRICITY);
  }

  /**
   * Calculates the total money saved from energy conserved (from natural gas and electricity) in
   * USD
   */
  public double calcEnergyMoney() {
    return (calcProperty(TREE_BENEFITS.NATURAL_GAS)
            * currencyConversion.get("natural_gas_kbtu_to_currency"))
        + (calcProperty(TREE_BENEFITS.ELECTRICITY)
            * currencyConversion.get("electricity_kwh_to_currency"));
  }

  /** Calculates the total water filtered in gal */
  public double calcStormwater() {
    return calcProperty(TREE_BENEFITS.HYDRO_INTERCEPTION);
  }

  /** Calculates the total money saved from water filtered in USD */
  public double calcStormwaterMoney() {
    return this.calcStormwater() * currencyConversion.get("h20_gal_to_currency");
  }

  /**
   * Calculates the air quality improved based on the avoidance and deposition of various compounds
   * in lbs: nitrous oxide (NOx) avoided nitrous oxide (NOx) deposition ozone deposition PM10
   * avoided PM10 deposition sulfur oxide (SOx) avoided sulfur oxide (SOx) deposition volatile
   * organic compounds (VOCs) avoided
   */
  public double calcAirQuality() {
    // kgs to lbs
    return (calcProperty(TREE_BENEFITS.AQ_NOX_AVOIDED)
            + calcProperty(TREE_BENEFITS.AQ_NOX_DEP)
            + calcProperty(TREE_BENEFITS.AQ_OZONE_DEP)
            + calcProperty(TREE_BENEFITS.AQ_PM10_AVOIDED)
            + calcProperty(TREE_BENEFITS.AQ_PM10_DEP)
            + calcProperty(TREE_BENEFITS.AQ_SOX_AVOIDED)
            + calcProperty(TREE_BENEFITS.AQ_SOX_DEP)
            + calcProperty(TREE_BENEFITS.AQ_VOC_AVOIDED))
        * 2.20462;
  }

  /** Calculates the total money saved from air quality improved in USD */
  public double calcAirQualityMoney() {
    // kgs to lbs
    // distributive property: v1*lbs*m1 + v2*lbs*m2 = lbs*(v1*m1 + v2*m2)
    return (((calcProperty(TREE_BENEFITS.AQ_NOX_AVOIDED) + calcProperty(TREE_BENEFITS.AQ_NOX_DEP))
                * currencyConversion.get("nox_lb_to_currency"))
            + (calcProperty(TREE_BENEFITS.AQ_OZONE_DEP)
                * currencyConversion.get("o3_lb_to_currency"))
            + ((calcProperty(TREE_BENEFITS.AQ_PM10_AVOIDED)
                    + calcProperty(TREE_BENEFITS.AQ_PM10_DEP))
                * currencyConversion.get("pm10_lb_to_currency"))
            + ((calcProperty(TREE_BENEFITS.AQ_SOX_AVOIDED) + calcProperty(TREE_BENEFITS.AQ_SOX_DEP))
                * currencyConversion.get("sox_lb_to_currency"))
            + (calcProperty(TREE_BENEFITS.AQ_VOC_AVOIDED)
                * currencyConversion.get("voc_lb_to_currency")))
        * 2.20462;
  }

  /** Calculates the total carbon dioxide removed (from avoiding and sequestering) in lbs */
  public double calcCo2Removed() {
    // kgs to lbs
    return (calcProperty(TREE_BENEFITS.CO2_AVOIDED) + calcProperty(TREE_BENEFITS.CO2_SEQUESTERED))
        * 2.20462;
  }

  /** Calculates the total money saved from dioxide removed in USD */
  public double calcCo2RemovedMoney() {
    return this.calcCo2Removed() * currencyConversion.get("co2_lb_to_currency");
  }

  /** Calculates the total carbon dioxide stored in lbs */
  public double calcCo2Stored() {
    // kgs to lbs
    return calcProperty(TREE_BENEFITS.CO2_STORAGE) * 2.20462;
  }

  /** Calculates the total money saved from dioxide stored in USD */
  public double calcCo2StoredMoney() {
    return this.calcCo2Stored() * currencyConversion.get("co2_lb_to_currency");
  }
}
