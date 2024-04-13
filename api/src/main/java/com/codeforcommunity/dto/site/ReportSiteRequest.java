package com.codeforcommunity.dto.site;

import com.codeforcommunity.dto.ApiDto;
import java.util.ArrayList;
import java.util.List;

public class ReportSiteRequest extends ApiDto {

  private String reason;
  private String description;

  public ReportSiteRequest(String reason, String description) {
    this.reason = reason;
    this.description = description;
  }

  public ReportSiteRequest() {}

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public List<String> validateFields(String fieldPrefix) {
    String fieldName = fieldPrefix + "report_site_request.";
    List<String> fields = new ArrayList<>();

    if (reason == null || reason.isEmpty()) {
      fields.add(fieldName + "reason");
    }

    if (description == null) {
      fields.add(fieldName + "description");
    }

    return fields;
  }
}
