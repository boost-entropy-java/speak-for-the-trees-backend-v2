package com.codeforcommunity.dto.site;

public class TreeBenefitsResponse {
  private final double energy;
  private final double energyMoney;
  private final double stormwater;
  private final double stormwaterMoney;
  private final double airQuality;
  private final double airQualityMoney;
  private final double co2Removed;
  private final double co2RemovedMoney;
  private final double co2Stored;
  private final double co2StoredMoney;

  public TreeBenefitsResponse(
          double energy,
          double energyMoney,
          double stormwater,
          double stormwaterMoney,
          double airQuality,
          double airQualityMoney,
          double co2Removed,
          double co2RemovedMoney,
          double co2Stored,
          double co2StoredMoney) {
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

  public double getEnergy() {
    return energy;
  }

  public double getEnergyMoney() {
    return energyMoney;
  }

  public double getStormwater() {
    return stormwater;
  }

  public double getStormwaterMoney() {
    return stormwaterMoney;
  }

  public double getAirQuality() {
    return airQuality;
  }

  public double getAirQualityMoney() {
    return airQualityMoney;
  }

  public double getCo2Removed() {
    return co2Removed;
  }

  public double getCo2RemovedMoney() {
    return co2RemovedMoney;
  }

  public double getCo2Stored() {
    return co2Stored;
  }

  public double getCo2StoredMoney() {
    return co2StoredMoney;
  }
}
