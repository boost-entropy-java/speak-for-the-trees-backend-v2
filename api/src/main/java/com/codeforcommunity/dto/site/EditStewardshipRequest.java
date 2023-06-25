package com.codeforcommunity.dto.site;

import com.codeforcommunity.exceptions.HandledException;
import java.sql.Date;
import java.util.List;

public class EditStewardshipRequest extends RecordStewardshipRequest {

  public EditStewardshipRequest(
      Date date,
      boolean watered,
      boolean mulched,
      boolean cleaned,
      boolean weeded,
      boolean installedWateringBag) {
    super(date, watered, mulched, cleaned, weeded, installedWateringBag);
  }

  private EditStewardshipRequest() {}

  @Override
  public List<String> validateFields(String fieldPrefix) throws HandledException {
    return validateStewardshipFields(fieldPrefix + "edit_stewardship_request.");
  }
}
