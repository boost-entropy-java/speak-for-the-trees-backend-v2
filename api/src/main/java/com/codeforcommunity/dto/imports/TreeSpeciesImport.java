package com.codeforcommunity.dto.imports;

import com.codeforcommunity.dto.ApiDto;
import com.codeforcommunity.exceptions.HandledException;
import java.util.ArrayList;
import java.util.List;

public class TreeSpeciesImport extends ApiDto {
  private String genus;
  private String species;
  private String commonName;
  private String speciesCode;

  private String defaultImage;

  public TreeSpeciesImport(
      String genus, String species, String commonName, String speciesCode, String defaultImage) {
    this.genus = genus;
    this.species = species;
    this.commonName = commonName;
    this.speciesCode = speciesCode;
    this.defaultImage = defaultImage;
  }

  private TreeSpeciesImport() {}

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

  public String getSpeciesCode() {
    return speciesCode;
  }

  public void setSpeciesCode(String speciesCode) {
    this.speciesCode = speciesCode;
  }

  public String getDefaultImage() {
    return defaultImage;
  }

  public void setDefaultImage(String defaultImage) {
    this.defaultImage = defaultImage;
  }

  @Override
  public List<String> validateFields(String fieldPrefix) throws HandledException {
    String fieldName = fieldPrefix + "tree_species.";
    List<String> fields = new ArrayList<>();

    if (genus == null || isEmpty(genus)) {
      fields.add(fieldName + "genus");
    }
    if (species == null) { // species can be empty
      fields.add(fieldName + "species");
    }
    if (commonName == null || isEmpty(commonName)) {
      fields.add(fieldName + "commonName");
    }
    if (speciesCode == null || isEmpty(speciesCode)) {
      fields.add(fieldName + "speciesCode");
    }
    // default image can be null, but not empty
    if (defaultImage != null && defaultImage.isEmpty()) {
      fields.add(fieldName + "defaultImage");
    }

    return fields;
  }
}
