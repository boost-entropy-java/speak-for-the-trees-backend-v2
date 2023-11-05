package com.codeforcommunity.requester;

import com.codeforcommunity.dto.site.TreeBenefitsResponse;

import static org.jooq.generated.Tables.TREE_BENEFITS;
import static org.jooq.generated.Tables.TREE_SPECIES;
import static org.jooq.impl.DSL.lower;
import static org.jooq.impl.DSL.replace;

import java.util.HashMap;
import java.util.Map;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.generated.tables.records.TreeBenefitsRecord;
import org.jooq.generated.tables.records.TreeSpeciesRecord;

public class TreeBenefitsCalculator {
  static final double[] intervals =
      new double[] {3.81, 11.43, 22.86, 38.10, 53.34, 68.58, 83.82, 99.06, 114.30};

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
  static final Map<String, Double> unitConversion =
      new HashMap<String, Double>() {
        {
          put("kBTU_to_kWh", 0.2930);
          put("kg_to_lb", 2.20462);
          put("m3_to_gal", 264.172);
        }
      };

  // fields to be set in the constructor
  private final DSLContext db;
  private final String commonName;
  private final double diameterCM;

  // fields to be computed and set when calculateBenefits() is called
  private double x0;
  private double x1;
  private TreeBenefitsRecord y0Record;
  private TreeBenefitsRecord y1Record;
  // diameter in inches
  public TreeBenefitsCalculator(DSLContext db, String commonName, double diameter) {
    this.db = db;
    this.commonName = commonName;

    // clamp diameter to min and max of intervals
    this.diameterCM =
        Math.min(Math.max(diameter * 2.54, intervals[0]), intervals[intervals.length - 1]);
  }

  public TreeBenefitsResponse calculateBenefits() {
    String speciesCode = getSpeciesCode(this.commonName);

    if (speciesCode == null) {
      // if the given tree is unknown, return a response with all nulls
      return new TreeBenefitsResponse();
    }

    double x0 = 0;
    double x1 = 0;
    // i should never reach the last element in intervals
    for (int i = 0; i < intervals.length - 1; i++) {
      if (intervals[i] <= diameterCM) {
        x0 = intervals[i];
        x1 = intervals[i + 1];
      }
    }

    // store computed x values and records for easy access in helper methods
    this.x0 = x0;
    this.x1 = x1;
    this.y0Record =
        db.selectFrom(TREE_BENEFITS)
            .where(TREE_BENEFITS.SPECIES_CODE.eq(speciesCode))
            .and(TREE_BENEFITS.DIAMETER.eq(x0))
            .fetchOne();
    this.y1Record =
        db.selectFrom(TREE_BENEFITS)
            .where(TREE_BENEFITS.SPECIES_CODE.eq(speciesCode))
            .and(TREE_BENEFITS.DIAMETER.eq(x1))
            .fetchOne();

    return new TreeBenefitsResponse(
        calcEnergy(), calcEnergyMoney(),
        calcStormwater(), calcStormwaterMoney(),
        calcAirQuality(), calcAirQualityMoney(),
        calcCo2Removed(), calcCo2RemovedMoney(),
        calcCo2Stored(), calcCo2StoredMoney());
  }

  // helper to query the species code from the tree_species table
  private String getSpeciesCode(String commonName) {
    TreeSpeciesRecord record =
        db.selectFrom(TREE_SPECIES)
            .where(
                lower(replace(TREE_SPECIES.COMMON_NAME, " ", ""))
                    .eq(commonName.toLowerCase().replace(" ", "")))
            .fetchOne();

    if (record == null) {
      return null;
    }

    return record.getSpeciesCode();
  }

  // helper to calculate the interpolated value of the given property
  private double calcProperty(Field property) {
    double y0 = this.y0Record.get(property, Double.class);
    double y1 = this.y1Record.get(property, Double.class);

    if (y0 == y1) {
      return y0;
    }

    // interpolate value
    double m = (y1 - y0) / (x1 - x0);
    double b = y0 - (x0 * m);

    return m * diameterCM + b;
  }

  /** Calculates the total energy conserved (from natural gas and electricity) in kWh */
  private double calcEnergy() {
    // natural gas Kbtu to kWh
    return (calcProperty(TREE_BENEFITS.NATURAL_GAS) * unitConversion.get("kBTU_to_kWh"))
        + calcProperty(TREE_BENEFITS.ELECTRICITY);
  }

  /**
   * Calculates the total money saved from energy conserved (from natural gas and electricity) in
   * USD
   */
  private double calcEnergyMoney() {
    return (calcProperty(TREE_BENEFITS.NATURAL_GAS)
            * currencyConversion.get("natural_gas_kbtu_to_currency"))
        + (calcProperty(TREE_BENEFITS.ELECTRICITY)
            * currencyConversion.get("electricity_kwh_to_currency"));
  }

  /** Calculates the total water filtered in gal */
  private double calcStormwater() {
    // m3 to gal
    return calcProperty(TREE_BENEFITS.HYDRO_INTERCEPTION) * unitConversion.get("m3_to_gal");
  }

  /** Calculates the total money saved from water filtered in USD */
  private double calcStormwaterMoney() {
    return this.calcStormwater() * currencyConversion.get("h20_gal_to_currency");
  }

  /**
   * Calculates the air quality improved based on the avoidance and deposition of various compounds
   * in lbs: nitrous oxide (NOx) avoided nitrous oxide (NOx) deposition ozone deposition PM10
   * avoided PM10 deposition sulfur oxide (SOx) avoided sulfur oxide (SOx) deposition volatile
   * organic compounds (VOCs) avoided
   */
  private double calcAirQuality() {
    // kgs to lbs
    return (calcProperty(TREE_BENEFITS.AQ_NOX_AVOIDED)
            + calcProperty(TREE_BENEFITS.AQ_NOX_DEP)
            + calcProperty(TREE_BENEFITS.AQ_OZONE_DEP)
            + calcProperty(TREE_BENEFITS.AQ_PM10_AVOIDED)
            + calcProperty(TREE_BENEFITS.AQ_PM10_DEP)
            + calcProperty(TREE_BENEFITS.AQ_SOX_AVOIDED)
            + calcProperty(TREE_BENEFITS.AQ_SOX_DEP)
            + calcProperty(TREE_BENEFITS.AQ_VOC_AVOIDED))
        * unitConversion.get("kg_to_lb");
  }

  /** Calculates the total money saved from air quality improved in USD */
  private double calcAirQualityMoney() {
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
        * unitConversion.get("kg_to_lb");
  }

  /** Calculates the total carbon dioxide removed (from avoiding and sequestering) in lbs */
  private double calcCo2Removed() {
    // kgs to lbs
    return (calcProperty(TREE_BENEFITS.CO2_AVOIDED) + calcProperty(TREE_BENEFITS.CO2_SEQUESTERED))
        * unitConversion.get("kg_to_lb");
  }

  /** Calculates the total money saved from dioxide removed in USD */
  private double calcCo2RemovedMoney() {
    return this.calcCo2Removed() * currencyConversion.get("co2_lb_to_currency");
  }

  /** Calculates the total carbon dioxide stored in lbs */
  private double calcCo2Stored() {
    // kgs to lbs
    return calcProperty(TREE_BENEFITS.CO2_STORAGE) * unitConversion.get("kg_to_lb");
  }

  /** Calculates the total money saved from dioxide stored in USD */
  private double calcCo2StoredMoney() {
    return this.calcCo2Stored() * currencyConversion.get("co2_lb_to_currency");
  }
}
