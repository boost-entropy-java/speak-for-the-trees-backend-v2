package com.codeforcommunity.dto.site;

public class TreeBenefitsResponse {
  private final Double energy;
  private final Double energyMoney;
  private final Double stormwater;
  private final Double stormwaterMoney;
  private final Double airQuality;
  private final Double airQualityMoney;
  private final Double co2Removed;
  private final Double co2RemovedMoney;
  private final Double co2Stored;
  private final Double co2StoredMoney;

  public TreeBenefitsResponse() {
    this(null, null, null, null, null, null, null, null, null, null);
  }

  public TreeBenefitsResponse(
      Double energy,
      Double energyMoney,
      Double stormwater,
      Double stormwaterMoney,
      Double airQuality,
      Double airQualityMoney,
      Double co2Removed,
      Double co2RemovedMoney,
      Double co2Stored,
      Double co2StoredMoney) {
    this.energy = energy;
    this.energyMoney = energyMoney;
    this.stormwater = stormwater;
    this.stormwaterMoney = stormwaterMoney;
    this.airQuality = airQuality;
    this.airQualityMoney = airQualityMoney;
    this.co2Removed = co2Removed;
    this.co2RemovedMoney = co2RemovedMoney;
    this.co2Stored = co2Stored;
    this.co2StoredMoney = co2StoredMoney;
  }

  public Double getEnergy() {
    return energy;
  }

  public Double getEnergyMoney() {
    return energyMoney;
  }

  public Double getStormwater() {
    return stormwater;
  }

  public Double getStormwaterMoney() {
    return stormwaterMoney;
  }

  public Double getAirQuality() {
    return airQuality;
  }

  public Double getAirQualityMoney() {
    return airQualityMoney;
  }

  public Double getCo2Removed() {
    return co2Removed;
  }

  public Double getCo2RemovedMoney() {
    return co2RemovedMoney;
  }

  public Double getCo2Stored() {
    return co2Stored;
  }

  public Double getCo2StoredMoney() {
    return co2StoredMoney;
  }
}
