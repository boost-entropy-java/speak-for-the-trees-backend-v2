package com.codeforcommunity.dto.site;

import com.codeforcommunity.dto.ApiDto;
import com.codeforcommunity.exceptions.HandledException;
import java.util.ArrayList;
import java.util.List;

public class ParentAdoptSiteRequest extends ApiDto {
  private Integer childUserId;

  public Integer getChildUserId() {
    return childUserId;
  }

  public void setChildUserId(Integer childUserId) {
    this.childUserId = childUserId;
  }

  @Override
  public List<String> validateFields(String fieldPrefix) throws HandledException {
    String fieldName = fieldPrefix + "parent_adopt_site_request.";
    List<String> fields = new ArrayList<>();

    if (childUserId == null) {
      fields.add(fieldName + "child_user_id");
    }

    return fields;
  }
}
