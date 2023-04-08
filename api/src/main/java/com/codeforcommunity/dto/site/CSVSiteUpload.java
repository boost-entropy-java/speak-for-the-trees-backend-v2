package com.codeforcommunity.dto.site;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CSVSiteUpload {
  /* site columns */
  private Integer blockId;
  private BigDecimal lat;
  private BigDecimal lng;
  private String city;
  private String zip;
  private String address;
  private String neighborhood;

  /* site entries columns */
  private Boolean treePresent;
  private String status;
  private String genus;
  private String species;
  private String commonName;
  private String confidence;
  private Double diameter;
  private Double circumference;
  private Boolean multistem;
  private String coverage;
  private String pruning;
  private String condition;
  private Boolean discoloring;
  private Boolean leaning;
  private Boolean constrictingGrate;
  private Boolean wounds;
  private Boolean pooling;
  private Boolean stakesWithWires;
  private Boolean stakesWithoutWires;
  private Boolean light;
  private Boolean bicycle;
  private Boolean bagEmpty;
  private Boolean bagFilled;
  private Boolean tape;
  private Boolean suckerGrowth;
  private String siteType;
  private String sidewalkWidth;
  private Double siteWidth;
  private Double siteLength;
  private String material;
  private Boolean raisedBed;
  private Boolean fence;
  private Boolean trash;
  private Boolean wires;
  private Boolean grate;
  private Boolean stump;
  private String treeNotes;
  private String siteNotes;

  public CSVSiteUpload(
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
      Boolean constrictingGate,
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
      Integer blockId,
      BigDecimal lat,
      BigDecimal lng,
      String city,
      String zip,
      String address,
      String neighborhood) {
    this.blockId = blockId;
    this.lat = lat;
    this.lng = lng;
    this.city = city;
    this.zip = zip;
    this.address = address;
    this.neighborhood = neighborhood;

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
    this.constrictingGrate = constrictingGate;
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
  }

  public CSVSiteUpload() {
    super();
  }

  public AddSiteRequest toAddSiteRequest() {
    Integer neighborhoodId = CSVSiteUpload.mapNeighborhoodNameToId(neighborhood);

    return new AddSiteRequest(
        treePresent,
        status,
        genus,
        species,
        commonName,
        confidence,
        diameter,
        circumference,
        multistem,
        coverage,
        pruning,
        condition,
        discoloring,
        leaning,
        constrictingGrate,
        wounds,
        pooling,
        stakesWithWires,
        stakesWithoutWires,
        light,
        bicycle,
        bagEmpty,
        bagFilled,
        tape,
        suckerGrowth,
        siteType,
        sidewalkWidth,
        siteWidth,
        siteLength,
        material,
        raisedBed,
        fence,
        trash,
        wires,
        grate,
        stump,
        treeNotes,
        siteNotes,
        blockId,
        lat,
        lng,
        city,
        zip,
        address,
        neighborhoodId);
  }

  public Integer getBlockId() {
    return blockId;
  }

  public BigDecimal getLat() {
    return lat;
  }

  public BigDecimal getLng() {
    return lng;
  }

  public String getCity() {
    return city;
  }

  public String getZip() {
    return zip;
  }

  public String getAddress() {
    return address;
  }

  public String getNeighborhood() {
    return neighborhood;
  }

  public Boolean isTreePresent() {
    return falseIfNull(treePresent);
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

  public Boolean isMultistem() {
    return falseIfNull(multistem);
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

  public Boolean isDiscoloring() {
    return falseIfNull(discoloring);
  }

  public Boolean isLeaning() {
    return falseIfNull(leaning);
  }

  public Boolean isConstrictingGrate() {
    return falseIfNull(constrictingGrate);
  }

  public Boolean isWounds() {
    return falseIfNull(wounds);
  }

  public Boolean isPooling() {
    return falseIfNull(pooling);
  }

  public Boolean isStakesWithWires() {
    return falseIfNull(stakesWithWires);
  }

  public Boolean isStakesWithoutWires() {
    return falseIfNull(stakesWithoutWires);
  }

  public Boolean isLight() {
    return falseIfNull(light);
  }

  public Boolean isBicycle() {
    return falseIfNull(bicycle);
  }

  public Boolean isBagEmpty() {
    return falseIfNull(bagEmpty);
  }

  public Boolean isBagFilled() {
    return falseIfNull(bagFilled);
  }

  public Boolean isTape() {
    return falseIfNull(tape);
  }

  public Boolean isSuckerGrowth() {
    return falseIfNull(suckerGrowth);
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

  public Boolean isRaisedBed() {
    return falseIfNull(raisedBed);
  }

  public Boolean isFence() {
    return falseIfNull(fence);
  }

  public Boolean isTrash() {
    return falseIfNull(trash);
  }

  public Boolean isWires() {
    return falseIfNull(wires);
  }

  public Boolean isGrate() {
    return falseIfNull(grate);
  }

  public Boolean isStump() {
    return falseIfNull(stump);
  }

  public String getTreeNotes() {
    return treeNotes;
  }

  public String getSiteNotes() {
    return siteNotes;
  }

  private boolean falseIfNull(Boolean bool) {
    return Optional.ofNullable(bool).orElse(false);
  }

  private static Integer mapNeighborhoodNameToId(String neighborhood) {
    Map<String, Integer> neighborhoodIdsMap = new HashMap<>();
    neighborhoodIdsMap.put("Back Bay", 2);
    neighborhoodIdsMap.put("Charlestown", 4);
    neighborhoodIdsMap.put("Dorchester", 6);
    neighborhoodIdsMap.put("Downtown", 7);
    neighborhoodIdsMap.put("East Boston", 8);
    neighborhoodIdsMap.put("Hyde Park", 10);
    neighborhoodIdsMap.put("Jamaica Plain", 11);
    neighborhoodIdsMap.put("Mattapan", 12);
    neighborhoodIdsMap.put("Mission Hill", 13);
    neighborhoodIdsMap.put("North End", 14);
    neighborhoodIdsMap.put("Roslindale", 15);
    neighborhoodIdsMap.put("Roxbury", 16);
    neighborhoodIdsMap.put("South Boston", 17);
    neighborhoodIdsMap.put("West Roxbury", 19);
    neighborhoodIdsMap.put("Harbor Islands", 22);
    neighborhoodIdsMap.put("Allston", 24);
    neighborhoodIdsMap.put("Brighton", 25);
    neighborhoodIdsMap.put("Chinatown", 26);
    neighborhoodIdsMap.put("Leather District", 27);
    neighborhoodIdsMap.put("Longwood", 28);
    neighborhoodIdsMap.put("South Boston Waterfront", 29);
    neighborhoodIdsMap.put("Beacon Hill", 30);
    neighborhoodIdsMap.put("West End", 31);
    neighborhoodIdsMap.put("South End", 32);
    neighborhoodIdsMap.put("Bay Village", 33);
    neighborhoodIdsMap.put("Fenway", 34);

    return neighborhoodIdsMap.get(neighborhood);
  }
}
