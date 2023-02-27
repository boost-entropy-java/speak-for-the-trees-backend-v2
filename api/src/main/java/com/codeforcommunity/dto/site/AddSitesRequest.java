package com.codeforcommunity.dto.site;

import com.codeforcommunity.dto.ApiDto;
import com.codeforcommunity.exceptions.HandledException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AddSitesRequest extends ApiDto {
  private String csvText;

  public AddSitesRequest(String csvText) {
    this.csvText = csvText;
  }

  public AddSitesRequest() {}

  public String getCsvText() {
    return csvText;
  }

  @Override
  public List<String> validateFields(String fieldPrefix) throws HandledException {
    String fieldName = fieldPrefix + "add_sites_request.";
    List<String> fields = new ArrayList<>();

    if (csvText == null) {
      fields.add(fieldName + "csvText");
    }

    return fields;
  }
}
