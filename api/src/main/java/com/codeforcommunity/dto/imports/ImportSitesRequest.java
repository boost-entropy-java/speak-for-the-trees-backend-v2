package com.codeforcommunity.dto.imports;

import com.codeforcommunity.dto.ApiDto;
import com.codeforcommunity.exceptions.HandledException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ImportSitesRequest extends ApiDto {
  private List<SiteImport> sites;

  public ImportSitesRequest(List<SiteImport> sites) {
    this.sites = sites;
  }

  private ImportSitesRequest() {}

  public List<SiteImport> getSites() {
    return sites;
  }

  public void setSites(List<SiteImport> sites) {
    this.sites = sites;
  }

  @Override
  public List<String> validateFields(String fieldPrefix) throws HandledException {
    String newFieldPrefix = fieldPrefix + "importSitesRequest.";

    if (sites == null) {
      return Collections.singletonList(newFieldPrefix + "sites");
    }

    return sites.stream()
        .flatMap(siteImport -> siteImport.validateFields(newFieldPrefix).stream())
        .collect(Collectors.toList());
  }
}
