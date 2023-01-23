package com.codeforcommunity.dto.site;

import java.math.BigDecimal;
import java.util.List;

public class GetSiteResponse {
  private final Integer siteId;
  private final Integer blockId;
  private final BigDecimal lat;
  private final BigDecimal lng;
  private final String city;
  private final String zip;
  private final String address;
  private final Integer neighborhoodId;
  private final List<SiteEntry> entries;

  public GetSiteResponse(
      Integer siteId,
      Integer blockId,
      BigDecimal lat,
      BigDecimal lng,
      String city,
      String zip,
      String address,
      Integer neighborhoodId,
      List<SiteEntry> entries) {
    this.siteId = siteId;
    this.blockId = blockId;
    this.lat = lat;
    this.lng = lng;
    this.city = city;
    this.zip = zip;
    this.address = address;
    this.neighborhoodId = neighborhoodId;
    this.entries = entries;
  }

  public Integer getSiteId() {
    return siteId;
  }

  public Integer getBlockId() {
    return blockId;
  }

  public BigDecimal getLat() {
    return lat;
  }

  public BigDecimal getLng() {
    return lng;
  }

  public String getCity() {
    return city;
  }

  public String getZip() {
    return zip;
  }

  public String getAddress() {
    return address;
  }

  public Integer getNeighborhoodId() {
    return neighborhoodId;
  }

  public List<SiteEntry> getEntries() {
    return entries;
  }
}
