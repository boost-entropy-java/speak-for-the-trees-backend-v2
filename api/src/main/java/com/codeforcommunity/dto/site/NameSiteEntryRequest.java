package com.codeforcommunity.dto.site;

import com.codeforcommunity.dto.ApiDto;
import com.codeforcommunity.exceptions.HandledException;
import java.util.ArrayList;
import java.util.List;

public class NameSiteEntryRequest extends ApiDto {
  private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public List<String> validateFields(String fieldPrefix) throws HandledException {
    String fieldName = fieldPrefix + "new_site_entry_name_request.";
    List<String> fields = new ArrayList<>();

    if (name == null || name.length() > 60) {
      fields.add(fieldName + "site_entry_name");
    }
    return fields;
  }
}
