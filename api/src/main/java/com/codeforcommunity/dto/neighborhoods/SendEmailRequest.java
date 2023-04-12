package com.codeforcommunity.dto.neighborhoods;

import com.codeforcommunity.dto.ApiDto;
import com.codeforcommunity.exceptions.HandledException;
import java.util.ArrayList;
import java.util.List;

public class SendEmailRequest extends ApiDto {
  private List<String> emails;
  private String emailSubject;
  private String emailBody;

  public SendEmailRequest(List<String> emails, String emailSubject, String emailBody) {
    this.emails = emails;
    this.emailSubject = emailSubject;
    this.emailBody = emailBody;
  }

  private SendEmailRequest() {}

  public List<String> getEmails() {
    return this.emails;
  }

  public void setEmails(List<String> emails) {
    this.emails = emails;
  }

  public String getEmailBody() {
    return this.emailBody;
  }

  public void setSites(String emailBody) {
    this.emailBody = emailBody;
  }

  public String getEmailSubject() {
    return this.emailSubject;
  }

  public void setEmailSubject(String emailSubject) {
    this.emailSubject = emailSubject;
  }

  @Override
  public List<String> validateFields(String fieldPrefix) throws HandledException {
    String fieldName = fieldPrefix + "send_email";
    List<String> fields = new ArrayList<>();

    if (emails == null) {
      fields.add(fieldName + "emails");
    }
    if (emailSubject == null) {
      fields.add(fieldName + "emailSubject");
    }
    if (emailBody == null) {
      fields.add(fieldName + "emailBody");
    }

    return fields;
  }
}
