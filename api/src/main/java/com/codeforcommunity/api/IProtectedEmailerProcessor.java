package com.codeforcommunity.api;

import com.codeforcommunity.auth.JWTData;
import com.codeforcommunity.dto.emailer.AddTemplateRequest;
import com.codeforcommunity.dto.emailer.LoadTemplateResponse;

public interface IProtectedEmailerProcessor {
  /** Adds an HTML email template to cloud storage. */
  void addTemplate(JWTData userData, AddTemplateRequest addTemplateRequest);

  /** Loads an HTML email template from cloud storage. */
  LoadTemplateResponse loadTemplate(JWTData userData, String templateName);

  /** Deletes an HTML email template with the given name in cloud storage. */
  void deleteTemplate(JWTData userData, String templateName);
}
