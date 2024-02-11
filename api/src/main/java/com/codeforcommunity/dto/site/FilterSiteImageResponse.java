package com.codeforcommunity.dto.site;

import java.sql.Date;

public class FilterSiteImageResponse {

  private int imageId;
  private String imageUrl;
  private int siteId;
  private String uploaderName;
  private String uploaderEmail;
  private Date dateSubmitted;
  private String commonName;
  private int neighborhoodId;
  private String address;

  public FilterSiteImageResponse(
      int imageId,
      String imageUrl,
      int siteId,
      String uploaderName,
      String uploaderEmail,
      Date dateSubmitted,
      String commonName,
      int neighborhoodId,
      String address) {
    this.imageId = imageId;
    this.imageUrl = imageUrl;
    this.siteId = siteId;
    this.uploaderName = uploaderName;
    this.uploaderEmail = uploaderEmail;
    this.dateSubmitted = dateSubmitted;
    this.commonName = commonName;
    this.neighborhoodId = neighborhoodId;
    this.address = address;
  }

  public int getImageId() {
    return imageId;
  }

  public void setImageId(int imageId) {
    this.imageId = imageId;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public int getSiteId() {
    return siteId;
  }

  public void setSiteId(int siteId) {
    this.siteId = siteId;
  }

  public String getUploaderName() {
    return uploaderName;
  }

  public void setUploaderName(String uploaderName) {
    this.uploaderName = uploaderName;
  }

  public String getUploaderEmail() {
    return uploaderEmail;
  }

  public void setUploaderEmail(String uploaderEmail) {
    this.uploaderEmail = uploaderEmail;
  }

  public Date getDateSubmitted() {
    return dateSubmitted;
  }

  public void setDateSubmitted(Date dateSubmitted) {
    this.dateSubmitted = dateSubmitted;
  }

  public String getCommonName() {
    return commonName;
  }

  public void setCommonName(String commonName) {
    this.commonName = commonName;
  }

  public int getNeighborhoodId() {
    return neighborhoodId;
  }

  public void setNeighborhoodId(int neighborhoodId) {
    this.neighborhoodId = neighborhoodId;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }
}
