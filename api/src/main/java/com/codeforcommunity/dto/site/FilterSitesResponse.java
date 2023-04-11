package com.codeforcommunity.dto.site;

public class FilterSitesResponse {

  private int siteId;
  private String address;
  private int adopterId;
  private String adopterName;

  private String adopterEmail;
  private String dateAdopted;
  private int adopterActivityCount;
  private int neighborhoodId;
  private Integer lastActivityWeeks;

  public FilterSitesResponse(int siteId, String address, int adopterId, String adopterName, String adopterEmail, String dateAdopted, int adopterActivityCount, int neighborhoodId, Integer lastActivityWeeks) {
    this.siteId = siteId;
    this.address = address;
    this.adopterId = adopterId;
    this.adopterName = adopterName;
    this.adopterEmail = adopterEmail;
    this.dateAdopted = dateAdopted;
    this.adopterActivityCount = adopterActivityCount;
    this.neighborhoodId = neighborhoodId;
    this.lastActivityWeeks = lastActivityWeeks;
  }

  public int getSiteId() {
    return siteId;
  }

  public void setSiteId(int siteId) {
    this.siteId = siteId;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public int getAdopterId() {
    return adopterId;
  }

  public void setAdopterId(int adopterId) {
    this.adopterId = adopterId;
  }

  public String getAdopterName() {
    return adopterName;
  }

  public void setAdopterName(String adopterName) {
    this.adopterName = adopterName;
  }

  public String getAdopterEmail() {
    return adopterEmail;
  }

  public void setAdopterEmail(String adopterEmail) {
    this.adopterEmail = adopterEmail;
  }

  public String getDateAdopted() {
    return dateAdopted;
  }

  public void setDateAdopted(String dateAdopted) {
    this.dateAdopted = dateAdopted;
  }

  public int getAdopterActivityCount() {
    return adopterActivityCount;
  }

  public void setAdopterActivityCount(int adopterActivityCount) {
    this.adopterActivityCount = adopterActivityCount;
  }

  public int getNeighborhoodId() {
    return neighborhoodId;
  }

  public void setNeighborhoodId(int neighborhoodId) {
    this.neighborhoodId = neighborhoodId;
  }

  public Integer getLastActivityWeeks() {
    return lastActivityWeeks;
  }

  public void setLastActivityWeeks(Integer lastActivityWeeks) {
    this.lastActivityWeeks = lastActivityWeeks;
  }
}
