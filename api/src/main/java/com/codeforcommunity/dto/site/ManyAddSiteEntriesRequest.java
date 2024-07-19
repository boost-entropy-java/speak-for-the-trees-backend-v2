package com.codeforcommunity.dto.site;

import com.codeforcommunity.dto.ApiDto;
import com.codeforcommunity.exceptions.HandledException;
import java.util.ArrayList;
import java.util.List;

public class ManyAddSiteEntriesRequest extends ApiDto {

  private List<UpdateSiteRequest> updateSiteRequests;

  private List<Integer> sites;

  public ManyAddSiteEntriesRequest(
      List<UpdateSiteRequest> updateSiteRequests, List<Integer> sites) {
    this.updateSiteRequests = updateSiteRequests;
    this.sites = sites;
  }

  public ManyAddSiteEntriesRequest() {}

  public List<UpdateSiteRequest> getUpdateSiteRequests() {
    return updateSiteRequests;
  }

  public void setUpdateSiteRequests(List<UpdateSiteRequest> updateSiteRequests) {
    this.updateSiteRequests = updateSiteRequests;
  }

  public List<Integer> getSites() {
    return sites;
  }

  public void setSites(List<Integer> sites) {
    this.sites = sites;
  }

  @Override
  public List<String> validateFields(String fieldPrefix) throws HandledException {
    String fieldName = fieldPrefix + "add_many_site_entries.";
    List<String> fields = new ArrayList<>();

    if (sites.size() != updateSiteRequests.size()) {
      fields.add(fieldName + "lengths");
    }

    return fields;
  }
}
