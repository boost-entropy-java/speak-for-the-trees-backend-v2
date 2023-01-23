package com.codeforcommunity.dto.site;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;

public class SiteEntry {
  private final Integer id;
  private final String username;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
  private final Timestamp updatedAt;

  private final Boolean treePresent;
  private final String status;
  private final String genus;
  private final String species;
  private final String commonName;
  private final String confidence;
  private final Double diameter;
  private final Double circumference;
  private final Boolean multistem;
  private final String coverage;
  private final String pruning;
  private final String condition;
  private final Boolean discoloring;
  private final Boolean leaning;
  private final Boolean constrictingGrate;
  private final Boolean wounds;
  private final Boolean pooling;
  private final Boolean stakesWithWires;
  private final Boolean stakesWithoutWires;
  private final Boolean light;
  private final Boolean bicycle;
  private final Boolean bagEmpty;
  private final Boolean bagFilled;
  private final Boolean tape;
  private final Boolean suckerGrowth;
  private final String siteType;
  private final String sidewalkWidth;
  private final Double siteWidth;
  private final Double siteLength;
  private final String material;
  private final Boolean raisedBed;
  private final Boolean fence;
  private final Boolean trash;
  private final Boolean wires;
  private final Boolean grate;
  private final Boolean stump;
  private final String treeNotes;
  private final String siteNotes;
  private final String treeName;
  private final String adopter;

  public SiteEntry(
      Integer id,
      String username,
      Timestamp updatedAt,
      Boolean treePresent,
      String status,
      String genus,
      String species,
      String commonName,
      String confidence,
      Double diameter,
      Double circumference,
      Boolean multistem,
      String coverage,
      String pruning,
      String condition,
      Boolean discoloring,
      Boolean leaning,
      Boolean constrictingGrate,
      Boolean wounds,
      Boolean pooling,
      Boolean stakesWithWires,
      Boolean stakesWithoutWires,
      Boolean light,
      Boolean bicycle,
      Boolean bagEmpty,
      Boolean bagFilled,
      Boolean tape,
      Boolean suckerGrowth,
      String siteType,
      String sidewalkWidth,
      Double siteWidth,
      Double siteLength,
      String material,
      Boolean raisedBed,
      Boolean fence,
      Boolean trash,
      Boolean wires,
      Boolean grate,
      Boolean stump,
      String treeNotes,
      String siteNotes,
      String treeName,
      String adopter) {
    this.id = id;
    this.username = username;
    this.updatedAt = updatedAt;
    this.treePresent = treePresent;
    this.status = status;
    this.genus = genus;
    this.species = species;
    this.commonName = commonName;
    this.confidence = confidence;
    this.diameter = diameter;
    this.circumference = circumference;
    this.multistem = multistem;
    this.coverage = coverage;
    this.pruning = pruning;
    this.condition = condition;
    this.discoloring = discoloring;
    this.leaning = leaning;
    this.constrictingGrate = constrictingGrate;
    this.wounds = wounds;
    this.pooling = pooling;
    this.stakesWithWires = stakesWithWires;
    this.stakesWithoutWires = stakesWithoutWires;
    this.light = light;
    this.bicycle = bicycle;
    this.bagEmpty = bagEmpty;
    this.bagFilled = bagFilled;
    this.tape = tape;
    this.suckerGrowth = suckerGrowth;
    this.siteType = siteType;
    this.sidewalkWidth = sidewalkWidth;
    this.siteWidth = siteWidth;
    this.siteLength = siteLength;
    this.material = material;
    this.raisedBed = raisedBed;
    this.fence = fence;
    this.trash = trash;
    this.wires = wires;
    this.grate = grate;
    this.stump = stump;
    this.treeNotes = treeNotes;
    this.siteNotes = siteNotes;
    this.treeName = treeName;
    this.adopter = adopter;
  }

  public Integer getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public Timestamp getUpdatedAt() {
    return updatedAt;
  }

  public Boolean getTreePresent() {
    return treePresent;
  }

  public String getStatus() {
    return status;
  }

  public String getGenus() {
    return genus;
  }

  public String getSpecies() {
    return species;
  }

  public String getCommonName() {
    return commonName;
  }

  public String getConfidence() {
    return confidence;
  }

  public Double getDiameter() {
    return diameter;
  }

  public Double getCircumference() {
    return circumference;
  }

  public Boolean getMultistem() {
    return multistem;
  }

  public String getCoverage() {
    return coverage;
  }

  public String getPruning() {
    return pruning;
  }

  public String getCondition() {
    return condition;
  }

  public Boolean getDiscoloring() {
    return discoloring;
  }

  public Boolean getLeaning() {
    return leaning;
  }

  public Boolean getConstrictingGrate() {
    return constrictingGrate;
  }

  public Boolean getWounds() {
    return wounds;
  }

  public Boolean getPooling() {
    return pooling;
  }

  public Boolean getStakesWithWires() {
    return stakesWithWires;
  }

  public Boolean getStakesWithoutWires() {
    return stakesWithoutWires;
  }

  public Boolean getLight() {
    return light;
  }

  public Boolean getBicycle() {
    return bicycle;
  }

  public Boolean getBagEmpty() {
    return bagEmpty;
  }

  public Boolean getBagFilled() {
    return bagFilled;
  }

  public Boolean getTape() {
    return tape;
  }

  public Boolean getSuckerGrowth() {
    return suckerGrowth;
  }

  public String getSiteType() {
    return siteType;
  }

  public String getSidewalkWidth() {
    return sidewalkWidth;
  }

  public Double getSiteWidth() {
    return siteWidth;
  }

  public Double getSiteLength() {
    return siteLength;
  }

  public String getMaterial() {
    return material;
  }

  public Boolean getRaisedBed() {
    return raisedBed;
  }

  public Boolean getFence() {
    return fence;
  }

  public Boolean getTrash() {
    return trash;
  }

  public Boolean getWires() {
    return wires;
  }

  public Boolean getGrate() {
    return grate;
  }

  public Boolean getStump() {
    return stump;
  }

  public String getTreeNotes() {
    return treeNotes;
  }

  public String getSiteNotes() {
    return siteNotes;
  }

  public String getTreeName() {
    return treeName;
  }

  public String getAdopter() {
    return adopter;
  }
}
