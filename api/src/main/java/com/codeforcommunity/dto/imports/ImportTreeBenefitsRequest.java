package com.codeforcommunity.dto.imports;

import com.codeforcommunity.dto.ApiDto;
import com.codeforcommunity.exceptions.HandledException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ImportTreeBenefitsRequest extends ApiDto {
  private List<TreeBenefitImport> treeBenefits;

  public ImportTreeBenefitsRequest(List<TreeBenefitImport> treeBenefits) {
    this.treeBenefits = treeBenefits;
  }

  private ImportTreeBenefitsRequest() {}

  public List<TreeBenefitImport> getTreeBenefits() {
    return treeBenefits;
  }

  public void setTreeBenefits(List<TreeBenefitImport> treeBenefits) {
    this.treeBenefits = treeBenefits;
  }

  @Override
  public List<String> validateFields(String fieldPrefix) throws HandledException {
    String newFieldPrefix = fieldPrefix + "importTreeBenefitsRequest.";

    if (treeBenefits == null) {
      return Collections.singletonList(newFieldPrefix + "treeBenefits");
    }

    return treeBenefits.stream()
        .flatMap(treeBenefitImport -> treeBenefitImport.validateFields(newFieldPrefix).stream())
        .collect(Collectors.toList());
  }
}
