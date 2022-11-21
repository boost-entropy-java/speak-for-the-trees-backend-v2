package com.codeforcommunity.dto.site;

import com.codeforcommunity.exceptions.HandledException;

import java.sql.Date;
import java.util.List;

public class ParentRecordStewardshipRequest extends RecordStewardshipRequest {

  private Integer childUserId;

  public ParentRecordStewardshipRequest(
      Date date,
      boolean watered,
      boolean mulched,
      boolean cleaned,
      boolean weeded,
      Integer childUserId) {
    super(date, watered, mulched, cleaned, weeded);
    this.childUserId = childUserId;
  }

  private ParentRecordStewardshipRequest() {
    super();
  };

  public Integer getChildUserId() {
    return childUserId;
  }

  public void setChildUserId(Integer childUserId) {
    this.childUserId = childUserId;
  }

  @Override
  public List<String> validateFields(String fieldPrefix) throws HandledException {
    String fieldName = fieldPrefix + "parent_record_stewardship_request.";
    List<String> fields = super.validateFields(fieldPrefix + "parent_");

    if (childUserId == null) {
      fields.add(fieldName + "child_user_id");
    }

    return fields;
  }

}
