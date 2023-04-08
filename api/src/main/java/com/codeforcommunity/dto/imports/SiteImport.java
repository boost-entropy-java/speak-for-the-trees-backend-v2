package com.codeforcommunity.dto.imports;

import com.codeforcommunity.dto.ApiDto;
import com.codeforcommunity.exceptions.HandledException;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(
    ignoreUnknown = true) // Allows us to store data in JSON for future use but not import it
public class SiteImport extends ApiDto {

  private Integer blockId;
  private BigDecimal lat;
  private BigDecimal lng;
  private String city;
  private String zip;
  private String address;
  private Integer neighborhoodId;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
  private Timestamp deletedAt;

  private Integer siteId;
  private Integer userId;
  private String username;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
  private Timestamp updatedAt;

  private Boolean qa;
  private Boolean treePresent;
  private String status;
  private String genus;
  private String species;
  private String commonName;
  private String confidence;
  private Boolean multistem;
  private Double diameter;
  private Double circumference;
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
  private String melneaCassTrees;
  private Integer mcbNumber;
  private String treeDedicatedTo;
  private Date plantingDate;
  private String treeName;

  /* Cambridge fields */
  private Integer trunks;
  private String speciesShort;
  private String location;
  private String siteRetiredReason;
  private String inspectr;
  private Boolean abutsOpenArea;
  private String treeWellCover;
  private Integer treeGrateActionReq;
  private String globalId;
  private Boolean pb;
  private Boolean siteReplanted;
  private Integer overheadWires;
  private String ownership;
  private Boolean scheduledRemoval;
  private Boolean structuralSoil;
  private String wateringResponsibility;
  private String cultivar;
  private Integer solarRating;
  private Boolean bareRoot;
  private Boolean adaCompliant;
  private Date cartegraphPlantDate;
  private Boolean locationRetired;
  private Date createdDate;
  private String order;
  private String plantingSeason;
  private Boolean exposedRootFlare;
  private String stTreePruningZone;
  private Boolean memTree;
  private Date cartegraphRetireDate;
  private String removalReason;
  private String offStTreePruningZone;
  private String plantingContract;
  private Double treeWellDepth;
  private Date removalDate;
  private String scientificName;
  private Boolean biocharAdded;
  private String lastEditedUser;

  public SiteImport(
      Integer blockId,
      BigDecimal lat,
      BigDecimal lng,
      String city,
      String zip,
      String address,
      Integer neighborhoodId,
      Timestamp deletedAt,
      Integer siteId,
      Integer userId,
      String username,
      Timestamp updatedAt,
      Boolean qa,
      Boolean treePresent,
      String status,
      String genus,
      String species,
      String commonName,
      String confidence,
      Boolean multistem,
      Double diameter,
      Double circumference,
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
      String melneaCassTrees,
      Integer mcbNumber,
      String treeDedicatedTo,
      Date plantingDate,
      String treeName,

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
    this.blockId = blockId;
    this.lat = lat;
    this.lng = lng;
    this.city = city;
    this.zip = zip;
    this.address = address;
    this.neighborhoodId = neighborhoodId;
    this.deletedAt = deletedAt;
    this.siteId = siteId;
    this.userId = userId;
    this.username = username;
    this.updatedAt = updatedAt;
    this.qa = qa;
    this.treePresent = treePresent;
    this.status = status;
    this.genus = genus;
    this.species = species;
    this.commonName = commonName;
    this.confidence = confidence;
    this.multistem = multistem;
    this.diameter = diameter;
    this.circumference = circumference;
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
    this.melneaCassTrees = melneaCassTrees;
    this.mcbNumber = mcbNumber;
    this.treeDedicatedTo = treeDedicatedTo;
    this.plantingDate = plantingDate;
    this.treeName = treeName;

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

  private SiteImport() {}

  public Integer getBlockId() {
    return blockId;
  }

  public void setBlockId(Integer blockId) {
    this.blockId = blockId;
  }

  public BigDecimal getLat() {
    return lat;
  }

  public void setLat(BigDecimal lat) {
    this.lat = lat;
  }

  public BigDecimal getLng() {
    return lng;
  }

  public void setLng(BigDecimal lng) {
    this.lng = lng;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getZip() {
    return zip;
  }

  public void setZip(String zip) {
    this.zip = zip;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public Integer getNeighborhoodId() {
    return neighborhoodId;
  }

  public void setNeighborhoodId(Integer neighborhoodId) {
    this.neighborhoodId = neighborhoodId;
  }

  public Timestamp getDeletedAt() {
    return deletedAt;
  }

  public void setDeletedAt(Timestamp deletedAt) {
    this.deletedAt = deletedAt;
  }

  public Integer getSiteId() {
    return siteId;
  }

  public void setSiteId(Integer siteId) {
    this.siteId = siteId;
  }

  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public Timestamp getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Timestamp updatedAt) {
    this.updatedAt = updatedAt;
  }

  public Boolean getQa() {
    return qa;
  }

  public void setQa(Boolean qa) {
    this.qa = qa;
  }

  public Boolean getTreePresent() {
    return treePresent;
  }

  public void setTreePresent(Boolean treePresent) {
    this.treePresent = treePresent;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getGenus() {
    return genus;
  }

  public void setGenus(String genus) {
    this.genus = genus;
  }

  public String getSpecies() {
    return species;
  }

  public void setSpecies(String species) {
    this.species = species;
  }

  public String getCommonName() {
    return commonName;
  }

  public void setCommonName(String commonName) {
    this.commonName = commonName;
  }

  public String getConfidence() {
    return confidence;
  }

  public void setConfidence(String confidence) {
    this.confidence = confidence;
  }

  public Boolean getMultistem() {
    return multistem;
  }

  public void setMultistem(Boolean multistem) {
    this.multistem = multistem;
  }

  public Double getDiameter() {
    return diameter;
  }

  public void setDiameter(Double diameter) {
    this.diameter = diameter;
  }

  public Double getCircumference() {
    return circumference;
  }

  public void setCircumference(Double circumference) {
    this.circumference = circumference;
  }

  public String getCoverage() {
    return coverage;
  }

  public void setCoverage(String coverage) {
    this.coverage = coverage;
  }

  public String getPruning() {
    return pruning;
  }

  public void setPruning(String pruning) {
    this.pruning = pruning;
  }

  public String getCondition() {
    return condition;
  }

  public void setCondition(String condition) {
    this.condition = condition;
  }

  public Boolean getDiscoloring() {
    return discoloring;
  }

  public void setDiscoloring(Boolean discoloring) {
    this.discoloring = discoloring;
  }

  public Boolean getLeaning() {
    return leaning;
  }

  public void setLeaning(Boolean leaning) {
    this.leaning = leaning;
  }

  public Boolean getConstrictingGrate() {
    return constrictingGrate;
  }

  public void setConstrictingGrate(Boolean constrictingGrate) {
    this.constrictingGrate = constrictingGrate;
  }

  public Boolean getWounds() {
    return wounds;
  }

  public void setWounds(Boolean wounds) {
    this.wounds = wounds;
  }

  public Boolean getPooling() {
    return pooling;
  }

  public void setPooling(Boolean pooling) {
    this.pooling = pooling;
  }

  public Boolean getStakesWithWires() {
    return stakesWithWires;
  }

  public void setStakesWithWires(Boolean stakesWithWires) {
    this.stakesWithWires = stakesWithWires;
  }

  public Boolean getStakesWithoutWires() {
    return stakesWithoutWires;
  }

  public void setStakesWithoutWires(Boolean stakesWithoutWires) {
    this.stakesWithoutWires = stakesWithoutWires;
  }

  public Boolean getLight() {
    return light;
  }

  public void setLight(Boolean light) {
    this.light = light;
  }

  public Boolean getBicycle() {
    return bicycle;
  }

  public void setBicycle(Boolean bicycle) {
    this.bicycle = bicycle;
  }

  public Boolean getBagEmpty() {
    return bagEmpty;
  }

  public void setBagEmpty(Boolean bagEmpty) {
    this.bagEmpty = bagEmpty;
  }

  public Boolean getBagFilled() {
    return bagFilled;
  }

  public void setBagFilled(Boolean bagFilled) {
    this.bagFilled = bagFilled;
  }

  public Boolean getTape() {
    return tape;
  }

  public void setTape(Boolean tape) {
    this.tape = tape;
  }

  public Boolean getSuckerGrowth() {
    return suckerGrowth;
  }

  public void setSuckerGrowth(Boolean suckerGrowth) {
    this.suckerGrowth = suckerGrowth;
  }

  public String getSiteType() {
    return siteType;
  }

  public void setSiteType(String siteType) {
    this.siteType = siteType;
  }

  public String getSidewalkWidth() {
    return sidewalkWidth;
  }

  public void setSidewalkWidth(String sidewalkWidth) {
    this.sidewalkWidth = sidewalkWidth;
  }

  public Double getSiteWidth() {
    return siteWidth;
  }

  public void setSiteWidth(Double siteWidth) {
    this.siteWidth = siteWidth;
  }

  public Double getSiteLength() {
    return siteLength;
  }

  public void setSiteLength(Double siteLength) {
    this.siteLength = siteLength;
  }

  public String getMaterial() {
    return material;
  }

  public void setMaterial(String material) {
    this.material = material;
  }

  public Boolean getRaisedBed() {
    return raisedBed;
  }

  public void setRaisedBed(Boolean raisedBed) {
    this.raisedBed = raisedBed;
  }

  public Boolean getFence() {
    return fence;
  }

  public void setFence(Boolean fence) {
    this.fence = fence;
  }

  public Boolean getTrash() {
    return trash;
  }

  public void setTrash(Boolean trash) {
    this.trash = trash;
  }

  public Boolean getWires() {
    return wires;
  }

  public void setWires(Boolean wires) {
    this.wires = wires;
  }

  public Boolean getGrate() {
    return grate;
  }

  public void setGrate(Boolean grate) {
    this.grate = grate;
  }

  public Boolean getStump() {
    return stump;
  }

  public void setStump(Boolean stump) {
    this.stump = stump;
  }

  public String getTreeNotes() {
    return treeNotes;
  }

  public void setTreeNotes(String treeNotes) {
    this.treeNotes = treeNotes;
  }

  public String getSiteNotes() {
    return siteNotes;
  }

  public void setSiteNotes(String siteNotes) {
    this.siteNotes = siteNotes;
  }

  public String getMelneaCassTrees() {
    return melneaCassTrees;
  }

  public void setMelneaCassTrees(String melneaCassTrees) {
    this.melneaCassTrees = melneaCassTrees;
  }

  public Integer getMcbNumber() {
    return mcbNumber;
  }

  public void setMcbNumber(Integer mcbNumber) {
    this.mcbNumber = mcbNumber;
  }

  public String getTreeDedicatedTo() {
    return treeDedicatedTo;
  }

  public void setTreeDedicatedTo(String treeDedicatedTo) {
    this.treeDedicatedTo = treeDedicatedTo;
  }

  public Date getPlantingDate() {
    return plantingDate;
  }

  public void setPlantingDate(Date plantingDate) {
    this.plantingDate = plantingDate;
  }

  public String getTreeName() {
    return treeName;
  }

  public void setTreeName(String treeName) {
    this.treeName = treeName;
  }

  /* Cambridge fields */
  public Integer getTrunks() {
    return trunks;
  }

  public void setTrunks(Integer trunks) {
    this.trunks = trunks;
  }

  public String getSpeciesShort() {
    return speciesShort;
  }

  public void setSpeciesShort(String speciesShort) {
    this.speciesShort = speciesShort;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getSiteRetiredReason() {
    return siteRetiredReason;
  }

  public void setSiteRetiredReason(String siteRetiredReason) {
    this.siteRetiredReason = siteRetiredReason;
  }

  public String getInspectr() {
    return inspectr;
  }

  public void setInspectr(String inspectr) {
    this.inspectr = inspectr;
  }

  public Boolean getAbutsOpenArea() {
    return abutsOpenArea;
  }

  public void setAbutsOpenArea(Boolean abutsOpenArea) {
    this.abutsOpenArea = abutsOpenArea;
  }

  public String getTreeWellCover() {
    return treeWellCover;
  }

  public void setTreeWellCover(String treeWellCover) {
    this.treeWellCover = treeWellCover;
  }

  public Integer getTreeGrateActionReq() {
    return treeGrateActionReq;
  }

  public void setTreeGrateActionReq(Integer treeGrateActionReq) {
    this.treeGrateActionReq = treeGrateActionReq;
  }

  public String getGlobalId() {
    return globalId;
  }

  public void setGlobalId(String globalId) {
    this.globalId = globalId;
  }

  public Boolean getPb() {
    return pb;
  }

  public void setPb(Boolean pb) {
    this.pb = pb;
  }

  public Boolean getSiteReplanted() {
    return siteReplanted;
  }

  public void setSiteReplanted(Boolean siteReplanted) {
    this.siteReplanted = siteReplanted;
  }

  public Integer getOverheadWires() {
    return overheadWires;
  }

  public void setOverheadWires(Integer overheadWires) {
    this.overheadWires = overheadWires;
  }

  public String getOwnership() {
    return ownership;
  }

  public void setOwnership(String ownership) {
    this.ownership = ownership;
  }

  public Boolean getScheduledRemoval() {
    return scheduledRemoval;
  }

  public void setScheduledRemoval(Boolean scheduledRemoval) {
    this.scheduledRemoval = scheduledRemoval;
  }

  public Boolean getStructuralSoil() {
    return structuralSoil;
  }

  public void setStructuralSoil(Boolean structuralSoil) {
    this.structuralSoil = structuralSoil;
  }

  public String getWateringResponsibility() {
    return wateringResponsibility;
  }

  public void setWateringResponsibility(String wateringResponsibility) {
    this.wateringResponsibility = wateringResponsibility;
  }

  public String getCultivar() {
    return cultivar;
  }

  public void setCultivar(String cultivar) {
    this.cultivar = cultivar;
  }

  public Integer getSolarRating() {
    return solarRating;
  }

  public void setSolarRating(Integer solarRating) {
    this.solarRating = solarRating;
  }

  public Boolean getBareRoot() {
    return bareRoot;
  }

  public void setBareRoot(Boolean bareRoot) {
    this.bareRoot = bareRoot;
  }

  public Boolean getAdaCompliant() {
    return adaCompliant;
  }

  public void setAdaCompliant(Boolean adaCompliant) {
    this.adaCompliant = adaCompliant;
  }

  public Date getCartegraphPlantDate() {
    return cartegraphPlantDate;
  }

  public void setCartegraphPlantDate(Date cartegraphPlantDate) {
    this.cartegraphPlantDate = cartegraphPlantDate;
  }

  public Boolean getLocationRetired() {
    return locationRetired;
  }

  public void setLocationRetired(Boolean locationRetired) {
    this.locationRetired = locationRetired;
  }

  public Date getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

  public String getOrder() {
    return order;
  }

  public void setOrder(String order) {
    this.order = order;
  }

  public String getPlantingSeason() {
    return plantingSeason;
  }

  public void setPlantingSeason(String plantingSeason) {
    this.plantingSeason = plantingSeason;
  }

  public Boolean getExposedRootFlare() {
    return exposedRootFlare;
  }

  public void setExposedRootFlare(Boolean exposedRootFlare) {
    this.exposedRootFlare = exposedRootFlare;
  }

  public String getStTreePruningZone() {
    return stTreePruningZone;
  }

  public void setStTreePruningZone(String stTreePruningZone) {
    this.stTreePruningZone = stTreePruningZone;
  }

  public Boolean getMemTree() {
    return memTree;
  }

  public void setMemTree(Boolean memTree) {
    this.memTree = memTree;
  }

  public Date getCartegraphRetireDate() {
    return cartegraphRetireDate;
  }

  public void setCartegraphRetireDate(Date cartegraphRetireDate) {
    this.cartegraphRetireDate = cartegraphRetireDate;
  }

  public String getRemovalReason() {
    return removalReason;
  }

  public void setRemovalReason(String removalReason) {
    this.removalReason = removalReason;
  }

  public String getOffStTreePruningZone() {
    return offStTreePruningZone;
  }

  public void setOffStTreePruningZone(String offStTreePruningZone) {
    this.offStTreePruningZone = offStTreePruningZone;
  }

  public String getPlantingContract() {
    return plantingContract;
  }

  public void setPlantingContract(String plantingContract) {
    this.plantingContract = plantingContract;
  }

  public Double getTreeWellDepth() {
    return treeWellDepth;
  }

  public void setTreeWellDepth(Double treeWellDepth) {
    this.treeWellDepth = treeWellDepth;
  }

  public Date getRemovalDate() {
    return removalDate;
  }

  public void setRemovalDate(Date removalDate) {
    this.removalDate = removalDate;
  }

  public String getScientificName() {
    return scientificName;
  }

  public void setScientificName(String scientificName) {
    this.scientificName = scientificName;
  }

  public Boolean getBiocharAdded() {
    return biocharAdded;
  }

  public void setBiocharAdded(Boolean biocharAdded) {
    this.biocharAdded = biocharAdded;
  }

  public String getLastEditedUser() {
    return lastEditedUser;
  }

  public void setLastEditedUser(String lastEditedUser) {
    this.lastEditedUser = lastEditedUser;
  }

  @Override
  public List<String> validateFields(String fieldPrefix) throws HandledException {
    String fieldName = fieldPrefix + "sites.";
    List<String> fields = new ArrayList<>();

    // blockId and zip are null sometimes, so not checked
    if (siteId == null) {
      fields.add(fieldName + "siteId");
    }
    if (lat == null) {
      fields.add(fieldName + "lat");
    }
    if (lng == null) {
      fields.add(fieldName + "lng");
    }
    if (city == null) {
      fields.add(fieldName + "city");
    }
    // TEMP: remove address check to upload Cambridge data
    // if (address == null) {
    //   fields.add(fieldName + "address");
    // }
    if (updatedAt == null) {
      fields.add(fieldName + "updatedAt");
    }

    return fields;
  }
}
