package com.codeforcommunity.dto.site;

import com.codeforcommunity.dto.ApiDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class FilterSiteImageRequest extends ApiDto {

  @JsonFormat(timezone = "America/New_York", pattern = "yyyy-MM-dd")
  private Timestamp submittedStart;

  @JsonFormat(timezone = "America/New_York", pattern = "yyyy-MM-dd")
  private Timestamp submittedEnd;

  private List<Integer> siteIds;

  private List<Integer> neighborhoodIds;

  public FilterSiteImageRequest(
      Timestamp submittedStart,
      Timestamp submittedEnd,
      List<Integer> siteIds,
      List<Integer> neighborhoodIds) {
    this.submittedStart = submittedStart;
    this.submittedEnd = submittedEnd;
    this.siteIds = siteIds;
    this.neighborhoodIds = neighborhoodIds;
  }

  public FilterSiteImageRequest() {}

  public Timestamp getSubmittedStart() {
    return submittedStart;
  }

  public void setSubmittedStart(Timestamp submittedStart) {
    this.submittedStart = submittedStart;
  }

  public Timestamp getSubmittedEnd() {
    return submittedEnd;
  }

  public void setSubmittedEnd(Timestamp submittedEnd) {
    this.submittedEnd = submittedEnd;
  }

  public List<Integer> getNeighborhoodIds() {
    return neighborhoodIds;
  }

  public void setNeighborhoodIds(List<Integer> neighborhoodIds) {
    this.neighborhoodIds = neighborhoodIds;
  }

  public List<Integer> getSiteIds() {
    return siteIds;
  }

  public void setSiteIds(List<Integer> siteIds) {
    this.siteIds = siteIds;
  }

  @Override
  public List<String> validateFields(String fieldPrefix) {
    return new ArrayList<>();
  }
}
