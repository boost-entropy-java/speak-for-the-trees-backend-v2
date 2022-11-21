package com.codeforcommunity.dto.site;

import com.codeforcommunity.dto.ApiDto;
import com.codeforcommunity.exceptions.HandledException;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.ArrayList;
import java.util.List;

public class RecordStewardshipRequest extends ApiDto {
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy", timezone="America/New_York")
  private java.sql.Date date;

  private boolean watered;
  private boolean mulched;
  private boolean cleaned;
  private boolean weeded;

  public RecordStewardshipRequest(
      java.sql.Date date, boolean watered, boolean mulched, boolean cleaned, boolean weeded) {
    this.date = date;
    this.watered = watered;
    this.mulched = mulched;
    this.cleaned = cleaned;
    this.weeded = weeded;
  }

  protected RecordStewardshipRequest() {}

  public java.sql.Date getDate() {
    return date;
  }

  public void setDate(java.sql.Date date) {
    this.date = date;
  }

  public boolean getWatered() {
    return watered;
  }

  public void setWatered(boolean watered) {
    this.watered = watered;
  }

  public boolean getMulched() {
    return mulched;
  }

  public void setMulched(boolean mulched) {
    this.mulched = mulched;
  }

  public boolean getCleaned() {
    return cleaned;
  }

  public void setCleaned(boolean cleaned) {
    this.cleaned = cleaned;
  }

  public boolean getWeeded() {
    return weeded;
  }

  public void setWeeded(boolean weeded) {
    this.weeded = weeded;
  }

  @Override
  public List<String> validateFields(String fieldPrefix) throws HandledException {
    String fieldName = fieldPrefix + "record_stewardship_request.";
    List<String> fields = new ArrayList<>();

    if (date == null) {
      fields.add(fieldName + "date");
    }
    if (!(watered || mulched || cleaned || weeded)) {
      fields.add(fieldName + "activities");
    }

    return fields;
  }
}
