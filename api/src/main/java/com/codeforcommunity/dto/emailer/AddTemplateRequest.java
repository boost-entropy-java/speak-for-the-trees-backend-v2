package com.codeforcommunity.dto.emailer;

import com.codeforcommunity.dto.ApiDto;
import com.codeforcommunity.exceptions.HandledException;
import java.util.ArrayList;
import java.util.List;

public class AddTemplateRequest extends ApiDto {
  private String name;
  private String template;

  public AddTemplateRequest(String template, String name) {
    this.template = template;
    this.name = name;
  }

  private AddTemplateRequest() {}

  public String getTemplate() {
    return this.template;
  }

  public String getName() {
    return this.name;
  }

  public void setTemplate(String template) {
    this.template = template;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public List<String> validateFields(String fieldPrefix) throws HandledException {
    String fieldName = fieldPrefix + "add_email_template_request";
    List<String> fields = new ArrayList<>();

    if (template == null) {
      fields.add(fieldName + "template");
    }
    if (name == null) {
      fields.add(fieldName + "name");
    }

    return fields;
  }
}
