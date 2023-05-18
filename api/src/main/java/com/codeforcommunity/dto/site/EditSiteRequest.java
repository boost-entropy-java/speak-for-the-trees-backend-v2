package com.codeforcommunity.dto.site;

import com.codeforcommunity.dto.ApiDto;
import com.codeforcommunity.enums.SiteOwner;
import com.codeforcommunity.exceptions.HandledException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class EditSiteRequest extends ApiDto {
  private Integer blockId;
  private String address;
  private String city;
  private String zip;
  private BigDecimal lat;
  private BigDecimal lng;
  private Integer neighborhoodId;
  private SiteOwner owner;

  public EditSiteRequest(
      Integer blockId,
      String address,
      String city,
      String zip,
      BigDecimal lat,
      BigDecimal lng,
      Integer neighborhoodId,
      SiteOwner owner) {
    this.blockId = blockId;
    this.address = address;
    this.city = city;
    this.zip = zip;
    this.lat = lat;
    this.lng = lng;
    this.neighborhoodId = neighborhoodId;
    this.owner = owner;
  }

  public EditSiteRequest() {}

  public Integer getBlockId() {
    return blockId;
  }

  public void setBlockId(Integer blockId) {
    this.blockId = blockId;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getZip() {
    return zip;
  }

  public void setZip(String zip) {
    this.zip = zip;
  }

  public BigDecimal getLat() {
    return lat;
  }

  public void setLat(BigDecimal lat) {
    this.lat = lat;
  }

  public BigDecimal getLng() {
    return lng;
  }

  public void setLng(BigDecimal lng) {
    this.lng = lng;
  }

  public Integer getNeighborhoodId() {
    return neighborhoodId;
  }

  public void setNeighborhoodId(Integer neighborhoodId) {
    this.neighborhoodId = neighborhoodId;
  }

  public SiteOwner getOwner() {
    return owner;
  }

  public void setOwner(SiteOwner owner) {
    this.owner = owner;
  }

  @Override
  public List<String> validateFields(String fieldPrefix) throws HandledException {
    String fieldName = fieldPrefix + "edit_site_request.";
    List<String> fields = new ArrayList<>();

    if (address == null) {
      fields.add(fieldName + "address");
    }
    if (city == null) {
      fields.add(fieldName + "city");
    }
    if (zip == null) {
      fields.add(fieldName + "zip");
    }
    if (lat == null) {
      fields.add(fieldName + "lat");
    }
    if (lng == null) {
      fields.add(fieldName + "lng");
    }
    if (neighborhoodId == null) {
      fields.add(fieldName + "neighborhoodId");
    }
    if (owner == null) {
      fields.add(fieldName + "owner");
    }

    return fields;
  }
}
