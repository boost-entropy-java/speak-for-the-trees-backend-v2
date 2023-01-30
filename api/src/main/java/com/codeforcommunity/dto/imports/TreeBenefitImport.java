package com.codeforcommunity.dto.imports;

import com.codeforcommunity.dto.ApiDto;
import com.codeforcommunity.exceptions.HandledException;
import java.util.ArrayList;
import java.util.List;

public class TreeBenefitImport extends ApiDto {
  private String speciesCode;
  private Double diameter;
  private Double aqNoxAvoided;
  private Double aqNoxDep;
  private Double aqOzoneDep;
  private Double aqPm10Avoided;
  private Double aqPm10Dep;
  private Double aqSoxAvoided;
  private Double aqSoxDep;
  private Double aqVocAvoided;
  private Double co2Avoided;
  private Double co2Sequestered;
  private Double co2Storage;
  private Double electricity;
  private Double hydroInterception;
  private Double naturalGas;

  public TreeBenefitImport(
      String speciesCode,
      Double diameter,
      Double aqNoxAvoided,
      Double aqNoxDep,
      Double aqOzoneDep,
      Double aqPm10Avoided,
      Double aqPm10Dep,
      Double aqSoxAvoided,
      Double aqSoxDep,
      Double aqVocAvoided,
      Double co2Avoided,
      Double co2Sequestered,
      Double co2Storage,
      Double electricity,
      Double hydroInterception,
      Double naturalGas) {
    this.speciesCode = speciesCode;
    this.diameter = diameter;
    this.aqNoxAvoided = aqNoxAvoided;
    this.aqNoxDep = aqNoxDep;
    this.aqOzoneDep = aqOzoneDep;
    this.aqPm10Avoided = aqPm10Avoided;
    this.aqPm10Dep = aqPm10Dep;
    this.aqSoxAvoided = aqSoxAvoided;
    this.aqSoxDep = aqSoxDep;
    this.aqVocAvoided = aqVocAvoided;
    this.co2Avoided = co2Avoided;
    this.co2Sequestered = co2Sequestered;
    this.co2Storage = co2Storage;
    this.electricity = electricity;
    this.hydroInterception = hydroInterception;
    this.naturalGas = naturalGas;
  }

  private TreeBenefitImport() {}

  public String getSpeciesCode() {
    return speciesCode;
  }

  public void setSpeciesCode(String speciesCode) {
    this.speciesCode = speciesCode;
  }

  public Double getDiameter() {
    return diameter;
  }

  public void setDiameter(Double diameter) {
    this.diameter = diameter;
  }

  public Double getAqNoxAvoided() {
    return aqNoxAvoided;
  }

  public void setAqNoxAvoided(Double aqNoxAvoided) {
    this.aqNoxAvoided = aqNoxAvoided;
  }

  public Double getAqNoxDep() {
    return aqNoxDep;
  }

  public void setAqNoxDep(Double aqNoxDep) {
    this.aqNoxDep = aqNoxDep;
  }

  public Double getAqOzoneDep() {
    return aqOzoneDep;
  }

  public void setAqOzoneDep(Double aqOzoneDep) {
    this.aqOzoneDep = aqOzoneDep;
  }

  public Double getAqPm10Avoided() {
    return aqPm10Avoided;
  }

  public void setAqPm10Avoided(Double aqPm10Avoided) {
    this.aqPm10Avoided = aqPm10Avoided;
  }

  public Double getAqPm10Dep() {
    return aqPm10Dep;
  }

  public void setAqPm10Dep(Double aqPm10Dep) {
    this.aqPm10Dep = aqPm10Dep;
  }

  public Double getAqSoxAvoided() {
    return aqSoxAvoided;
  }

  public void setAqSoxAvoided(Double aqSoxAvoided) {
    this.aqSoxAvoided = aqSoxAvoided;
  }

  public Double getAqSoxDep() {
    return aqSoxDep;
  }

  public void setAqSoxDep(Double aqSoxDep) {
    this.aqSoxDep = aqSoxDep;
  }

  public Double getAqVocAvoided() {
    return aqVocAvoided;
  }

  public void setAqVocAvoided(Double aqVocAvoided) {
    this.aqVocAvoided = aqVocAvoided;
  }

  public Double getCo2Avoided() {
    return co2Avoided;
  }

  public void setCo2Avoided(Double co2Avoided) {
    this.co2Avoided = co2Avoided;
  }

  public Double getCo2Sequestered() {
    return co2Sequestered;
  }

  public void setCo2Sequestered(Double co2Sequestered) {
    this.co2Sequestered = co2Sequestered;
  }

  public Double getCo2Storage() {
    return co2Storage;
  }

  public void setCo2Storage(Double co2Storage) {
    this.co2Storage = co2Storage;
  }

  public Double getElectricity() {
    return electricity;
  }

  public void setElectricity(Double electricity) {
    this.electricity = electricity;
  }

  public Double getHydroInterception() {
    return hydroInterception;
  }

  public void setHydroInterception(Double hydroInterception) {
    this.hydroInterception = hydroInterception;
  }

  public Double getNaturalGas() {
    return naturalGas;
  }

  public void setNaturalGas(Double naturalGas) {
    this.naturalGas = naturalGas;
  }

  @Override
  public List<String> validateFields(String fieldPrefix) throws HandledException {
    String fieldName = fieldPrefix + "tree_benefit.";
    List<String> fields = new ArrayList<>();

    if (speciesCode == null || isEmpty(speciesCode)) {
      fields.add(fieldName + "speciesCode");
    }
    if (diameter == null || diameter < 0) {
      fields.add(fieldName + "diameter");
    }
    if (aqNoxAvoided == null || aqNoxAvoided < 0) {
      fields.add(fieldName + "aqNoxAvoided");
    }
    if (aqNoxDep == null || aqNoxDep < 0) {
      fields.add(fieldName + "aqNoxDep");
    }
    if (aqOzoneDep == null || aqOzoneDep < 0) {
      fields.add(fieldName + "aqOzoneDep");
    }
    if (aqPm10Avoided == null || aqPm10Avoided < 0) {
      fields.add(fieldName + "aqPm10Avoided");
    }
    if (aqPm10Dep == null || aqPm10Dep < 0) {
      fields.add(fieldName + "aqPm10Dep");
    }
    if (aqSoxAvoided == null || aqSoxAvoided < 0) {
      fields.add(fieldName + "aqSoxAvoided");
    }
    if (aqSoxDep == null || aqSoxDep < 0) {
      fields.add(fieldName + "aqSoxDep");
    }
    if (aqVocAvoided == null || aqVocAvoided < 0) {
      fields.add(fieldName + "aqVocAvoided");
    }
    if (co2Avoided == null || co2Avoided < 0) {
      fields.add(fieldName + "co2Avoided");
    }
    if (co2Sequestered == null || co2Sequestered < 0) {
      fields.add(fieldName + "co2Sequestered");
    }
    if (co2Storage == null || co2Storage < 0) {
      fields.add(fieldName + "co2Storage");
    }
    if (electricity == null || electricity < 0) {
      fields.add(fieldName + "electricity");
    }
    if (hydroInterception == null || hydroInterception < 0) {
      fields.add(fieldName + "hydroInterception");
    }
    if (naturalGas == null || naturalGas < 0) {
      fields.add(fieldName + "naturalGas");
    }

    return fields;
  }
}
