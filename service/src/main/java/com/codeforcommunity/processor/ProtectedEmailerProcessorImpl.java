package com.codeforcommunity.processor;

import com.codeforcommunity.api.IProtectedEmailerProcessor;
import com.codeforcommunity.auth.JWTData;
import com.codeforcommunity.dto.emailer.AddTemplateRequest;
import com.codeforcommunity.dto.emailer.LoadTemplateResponse;
import com.codeforcommunity.requester.S3Requester;

public class ProtectedEmailerProcessorImpl extends AbstractProcessor
    implements IProtectedEmailerProcessor {

  @Override
  public void addTemplate(JWTData userData, AddTemplateRequest addTemplateRequest) {
    assertAdminOrSuperAdmin(userData.getPrivilegeLevel());
    S3Requester.uploadHTML(
        addTemplateRequest.getName(),
        "email_templates",
        userData.getUserId(),
        addTemplateRequest.getTemplate());
  }

  @Override
  public LoadTemplateResponse loadTemplate(JWTData userData, String templateName) {
    assertAdminOrSuperAdmin(userData.getPrivilegeLevel());
    LoadTemplateResponse loadTemplateResponse = S3Requester.loadHTML(templateName, "email_templates");
    return loadTemplateResponse;
  }
}
