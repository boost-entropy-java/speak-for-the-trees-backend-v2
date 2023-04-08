package com.codeforcommunity.dto.site;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.sql.Date;
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

  /* Cambridge fields */
  private final Integer trunks;
  private final String speciesShort;
  private final String location;
  private final String siteRetiredReason;
  private final String inspectr;
  private final Boolean abutsOpenArea;
  private final String treeWellCover;
  private final Integer treeGrateActionReq;
  private final String globalId;
  private final Boolean pb;
  private final Boolean siteReplanted;
  private final Integer overheadWires;
  private final String ownership;
  private final Boolean scheduledRemoval;
  private final Boolean structuralSoil;
  private final String wateringResponsibility;
  private final String cultivar;
  private final Integer solarRating;
  private final Boolean bareRoot;
  private final Boolean adaCompliant;
  private final Date cartegraphPlantDate;
  private final Boolean locationRetired;
  private final Date createdDate;
  private final String order;
  private final String plantingSeason;
  private final Boolean exposedRootFlare;
  private final String stTreePruningZone;
  private final Boolean memTree;
  private final Date cartegraphRetireDate;
  private final String removalReason;
  private final String offStTreePruningZone;
  private final String plantingContract;
  private final Double treeWellDepth;
  private final Date removalDate;
  private final String scientificName;
  private final Boolean biocharAdded;
  private final String lastEditedUser;

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
      String adopter,

      /* Cambridge fields */
      Integer trunks,
      String speciesShort,
      String location,
      String siteRetiredReason,
      String inspectr,
      Boolean abutsOpenArea,
      String treeWellCover,
      Integer treeGrateActionReq,
      String globalId,
      Boolean pb,
      Boolean siteReplanted,
      Integer overheadWires,
      String ownership,
      Boolean scheduledRemoval,
      Boolean structuralSoil,
      String wateringResponsibility,
      String cultivar,
      Integer solarRating,
      Boolean bareRoot,
      Boolean adaCompliant,
      Date cartegraphPlantDate,
      Boolean locationRetired,
      Date createdDate,
      String order,
      String plantingSeason,
      Boolean exposedRootFlare,
      String stTreePruningZone,
      Boolean memTree,
      Date cartegraphRetireDate,
      String removalReason,
      String offStTreePruningZone,
      String plantingContract,
      Double treeWellDepth,
      Date removalDate,
      String scientificName,
      Boolean biocharAdded,
      String lastEditedUser) {
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

    /* Cambridge fields */
    this.trunks = trunks;
    this.speciesShort = speciesShort;
    this.location = location;
    this.siteRetiredReason = siteRetiredReason;
    this.inspectr = inspectr;
    this.abutsOpenArea = abutsOpenArea;
    this.treeWellCover = treeWellCover;
    this.treeGrateActionReq = treeGrateActionReq;
    this.globalId = globalId;
    this.pb = pb;
    this.siteReplanted = siteReplanted;
    this.overheadWires = overheadWires;
    this.ownership = ownership;
    this.scheduledRemoval = scheduledRemoval;
    this.structuralSoil = structuralSoil;
    this.wateringResponsibility = wateringResponsibility;
    this.cultivar = cultivar;
    this.solarRating = solarRating;
    this.bareRoot = bareRoot;
    this.adaCompliant = adaCompliant;
    this.cartegraphPlantDate = cartegraphPlantDate;
    this.locationRetired = locationRetired;
    this.createdDate = createdDate;
    this.order = order;
    this.plantingSeason = plantingSeason;
    this.exposedRootFlare = exposedRootFlare;
    this.stTreePruningZone = stTreePruningZone;
    this.memTree = memTree;
    this.cartegraphRetireDate = cartegraphRetireDate;
    this.removalReason = removalReason;
    this.offStTreePruningZone = offStTreePruningZone;
    this.plantingContract = plantingContract;
    this.treeWellDepth = treeWellDepth;
    this.removalDate = removalDate;
    this.scientificName = scientificName;
    this.biocharAdded = biocharAdded;
    this.lastEditedUser = lastEditedUser;
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

  /* Cambridge fields */
  public Integer getTrunks() {
    return trunks;
  }

  public String getSpeciesShort() {
    return speciesShort;
  }

  public String getLocation() {
    return location;
  }

  public String getSiteRetiredReason() {
    return siteRetiredReason;
  }

  public String getInspectr() {
    return inspectr;
  }

  public Boolean getAbutsOpenArea() {
    return abutsOpenArea;
  }

  public String getTreeWellCover() {
    return treeWellCover;
  }

  public Integer getTreeGrateActionReq() {
    return treeGrateActionReq;
  }

  public String getGlobalId() {
    return globalId;
  }

  public Boolean getPb() {
    return pb;
  }

  public Boolean getSiteReplanted() {
    return siteReplanted;
  }

  public Integer getOverheadWires() {
    return overheadWires;
  }

  public String getOwnership() {
    return ownership;
  }

  public Boolean getScheduledRemoval() {
    return scheduledRemoval;
  }

  public Boolean getStructuralSoil() {
    return structuralSoil;
  }

  public String getWateringResponsibility() {
    return wateringResponsibility;
  }

  public String getCultivar() {
    return cultivar;
  }

  public Integer getSolarRating() {
    return solarRating;
  }

  public Boolean getBareRoot() {
    return bareRoot;
  }

  public Boolean getAdaCompliant() {
    return adaCompliant;
  }

  public Date getCartegraphPlantDate() {
    return cartegraphPlantDate;
  }

  public Boolean getLocationRetired() {
    return locationRetired;
  }

  public Date getCreatedDate() {
    return createdDate;
  }

  public String getOrder() {
    return order;
  }

  public String getPlantingSeason() {
    return plantingSeason;
  }

  public Boolean getExposedRootFlare() {
    return exposedRootFlare;
  }

  public String getStTreePruningZone() {
    return stTreePruningZone;
  }

  public Boolean getMemTree() {
    return memTree;
  }

  public Date getCartegraphRetireDate() {
    return cartegraphRetireDate;
  }

  public String getRemovalReason() {
    return removalReason;
  }

  public String getOffStTreePruningZone() {
    return offStTreePruningZone;
  }

  public String getPlantingContract() {
    return plantingContract;
  }

  public Double getTreeWellDepth() {
    return treeWellDepth;
  }

  public Date getRemovalDate() {
    return removalDate;
  }

  public String getScientificName() {
    return scientificName;
  }

  public Boolean getBiocharAdded() {
    return biocharAdded;
  }

  public String getLastEditedUser() {
    return lastEditedUser;
  }
}
