package com.codeforcommunity.dto.imports;

import com.codeforcommunity.dto.ApiDto;
import com.codeforcommunity.exceptions.HandledException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ImportTreeSpeciesRequest extends ApiDto {
  private List<TreeSpeciesImport> treeSpecies;

  public ImportTreeSpeciesRequest(List<TreeSpeciesImport> treeSpecies) {
    this.treeSpecies = treeSpecies;
  }

  private ImportTreeSpeciesRequest() {}

  public List<TreeSpeciesImport> getTreeSpecies() {
    return treeSpecies;
  }

  public void setTreeSpecies(List<TreeSpeciesImport> treeSpecies) {
    this.treeSpecies = treeSpecies;
  }

  @Override
  public List<String> validateFields(String fieldPrefix) throws HandledException {
    String newFieldPrefix = fieldPrefix + "importTreeSpeciesRequest.";

    if (treeSpecies == null) {
      return Collections.singletonList(newFieldPrefix + "treeSpecies");
    }

    return treeSpecies.stream()
        .flatMap(treeSpeciesImport -> treeSpeciesImport.validateFields(newFieldPrefix).stream())
        .collect(Collectors.toList());
  }
}
