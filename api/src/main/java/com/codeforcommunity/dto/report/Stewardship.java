package com.codeforcommunity.dto.report;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.sql.Timestamp;

public class Stewardship {
  private final int siteId;
  private final String address;
  private final String name;
  private final String email;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
  private final Timestamp datePerformed;

  private final Boolean watered;
  private final Boolean mulched;
  private final Boolean cleaned;
  private final Boolean weeded;
  private final Boolean installedWateringBag;
  private final String neighborhood;

  public Stewardship(
      int siteId,
      String address,
      String name,
      String email,
      Timestamp datePerformed,
      Boolean watered,
      Boolean mulched,
      Boolean cleaned,
      Boolean weeded,
      Boolean installedWateringBag,
      String neighborhood) {
    this.siteId = siteId;
    this.address = address;
    this.name = name;
    this.email = email;
    this.datePerformed = datePerformed;
    this.watered = watered;
    this.mulched = mulched;
    this.cleaned = cleaned;
    this.weeded = weeded;
    this.installedWateringBag = installedWateringBag;
    this.neighborhood = neighborhood;
  }

  public int getSiteId() {
    return siteId;
  }

  public String getAddress() {
    return address;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public Timestamp getDatePerformed() {
    return datePerformed;
  }

  public Boolean getWatered() {
    return watered;
  }

  public Boolean getMulched() {
    return mulched;
  }

  public Boolean getCleaned() {
    return cleaned;
  }

  public Boolean getWeeded() {
    return weeded;
  }

  public Boolean getInstalledWateringBag() {
    return installedWateringBag;
  }

  public String getNeighborhood() {
    return neighborhood;
  }
}
