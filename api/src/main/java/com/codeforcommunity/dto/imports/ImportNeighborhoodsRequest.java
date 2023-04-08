package com.codeforcommunity.dto.imports;

import com.codeforcommunity.dto.ApiDto;
import com.codeforcommunity.exceptions.HandledException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ImportNeighborhoodsRequest extends ApiDto {
  private List<NeighborhoodImport> neighborhoods;

  public ImportNeighborhoodsRequest(List<NeighborhoodImport> neighborhoods) {
    this.neighborhoods = neighborhoods;
  }

  private ImportNeighborhoodsRequest() {}

  public List<NeighborhoodImport> getNeighborhoods() {
    return neighborhoods;
  }

  public void setNeighborhoods(List<NeighborhoodImport> neighborhoods) {
    this.neighborhoods = neighborhoods;
  }

  @Override
  public List<String> validateFields(String fieldPrefix) throws HandledException {
    String newFieldPrefix = fieldPrefix + "importNeighborhoodRequest.";

    if (neighborhoods == null) {
      return Collections.singletonList(newFieldPrefix + "neighborhoods");
    }

    return neighborhoods.stream()
        .flatMap(neighborhoodImport -> neighborhoodImport.validateFields(newFieldPrefix).stream())
        .collect(Collectors.toList());
  }
}
