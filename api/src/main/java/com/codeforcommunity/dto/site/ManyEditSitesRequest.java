package com.codeforcommunity.dto.site;

import com.codeforcommunity.dto.ApiDto;
import com.codeforcommunity.exceptions.HandledException;
import java.util.ArrayList;
import java.util.List;

public class ManyEditSitesRequest extends ApiDto {

  private List<EditSiteRequest> editSitesRequests;

  private List<Integer> sites;

  public ManyEditSitesRequest(List<EditSiteRequest> editSitesRequests, List<Integer> sites) {
    this.editSitesRequests = editSitesRequests;
    this.sites = sites;
  }

  public ManyEditSitesRequest() {}

  public List<EditSiteRequest> getEditSitesRequests() {
    return editSitesRequests;
  }

  public void setEditSitesRequests(List<EditSiteRequest> editSitesRequests) {
    this.editSitesRequests = editSitesRequests;
  }

  public List<Integer> getSites() {
    return sites;
  }

  public void setSites(List<Integer> sites) {
    this.sites = sites;
  }

  @Override
  public List<String> validateFields(String fieldPrefix) throws HandledException {
    String fieldName = fieldPrefix + "edit_many_sites.";
    List<String> fields = new ArrayList<>();

    if (sites.size() != editSitesRequests.size()) {
      fields.add(fieldName + "lengths");
    }

    return fields;
  }
}
