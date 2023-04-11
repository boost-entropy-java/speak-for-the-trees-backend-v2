package com.codeforcommunity.dto.site;

import com.codeforcommunity.dto.ApiDto;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class FilterSitesRequest extends ApiDto {

  private List<String> treeSpecies;

  @JsonFormat(timezone = "America/New_York", pattern = "yyyy-MM-dd")
  private Date adoptedStart;

  @JsonFormat(timezone = "America/New_York", pattern = "yyyy-MM-dd")
  private Date adoptedEnd;

  @JsonFormat(timezone = "America/New_York", pattern = "yyyy-MM-dd")
  private Date lastActivityStart;

  @JsonFormat(timezone = "America/New_York", pattern = "yyyy-MM-dd")
  private Date lastActivityEnd;

  private List<Integer> neighborhoodIds;

  public List<String> getTreeSpecies() {
    return treeSpecies;
  }

  public void setTreeSpecies(List<String> treeSpecies) {
    this.treeSpecies = treeSpecies;
  }

  public Date getAdoptedStart() {
    return adoptedStart;
  }

  public void setAdoptedStart(Date adoptedStart) {
    this.adoptedStart = adoptedStart;
  }

  public Date getAdoptedEnd() {
    return adoptedEnd;
  }

  public void setAdoptedEnd(Date adoptedEnd) {
    this.adoptedEnd = adoptedEnd;
  }

  public Date getLastActivityStart() {
    return lastActivityStart;
  }

  public void setLastActivityStart(Date lastActivityStart) {
    this.lastActivityStart = lastActivityStart;
  }

  public Date getLastActivityEnd() {
    return lastActivityEnd;
  }

  public void setLastActivityEnd(Date lastActivityEnd) {
    this.lastActivityEnd = lastActivityEnd;
  }

  public List<Integer> getNeighborhoodIds() {
    return neighborhoodIds;
  }

  public void setNeighborhoodIds(List<Integer> neighborhoodIds) {
    this.neighborhoodIds = neighborhoodIds;
  }

  @Override
  public List<String> validateFields(String fieldPrefix) {
    return new ArrayList<>();
  }
}
