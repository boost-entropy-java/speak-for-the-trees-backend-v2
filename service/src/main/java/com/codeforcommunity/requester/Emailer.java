package com.codeforcommunity.requester;

import com.codeforcommunity.dto.emailer.EmailAttachment;
import com.codeforcommunity.email.EmailOperations;
import com.codeforcommunity.propertiesLoader.PropertiesLoader;
import org.simplejavamail.api.email.AttachmentResource;

import java.util.*;

public class Emailer {
  private final EmailOperations emailOperations;
  private final String frontendUrl;
  private final String passwordResetTemplate;

  private final String subjectWelcome = PropertiesLoader.loadProperty("email_subject_welcome");
  private final String subjectEmailChange =
      PropertiesLoader.loadProperty("email_subject_email_change");
  private final String subjectPasswordResetRequest =
      PropertiesLoader.loadProperty("email_subject_password_reset_request");
  private final String subjectPasswordResetConfirm =
      PropertiesLoader.loadProperty("email_subject_password_reset_confirm");
  private final String subjectAccountDeleted =
      PropertiesLoader.loadProperty("email_subject_account_deleted");
  private final String subjectEmailNeighborhoods =
      PropertiesLoader.loadProperty("email_subject_neighborhood_notification");
  private final String subjectImageRejected =
      PropertiesLoader.loadProperty("email_subject_image_rejected");
  private final String subjectSiteReport =
      PropertiesLoader.loadProperty("email_subject_site_report");
  private final String reportEmailDestination =
      PropertiesLoader.loadProperty("email_report_destination");
  private final String senderName = PropertiesLoader.loadProperty("email_sender_name");

  public Emailer() {
    String sendEmail = PropertiesLoader.loadProperty("email_send_email");
    String sendPassword = PropertiesLoader.loadProperty("email_send_password");
    String emailHost = PropertiesLoader.loadProperty("email_host");
    int emailPort = Integer.parseInt(PropertiesLoader.loadProperty("email_port"));
    boolean shouldSendEmails =
        Boolean.parseBoolean(PropertiesLoader.loadProperty("email_should_send"));

    this.emailOperations =
        new EmailOperations(
            shouldSendEmails, senderName, sendEmail, sendPassword, emailHost, emailPort);

    this.frontendUrl = PropertiesLoader.loadProperty("frontend_base_url");
    this.passwordResetTemplate =
        this.frontendUrl + PropertiesLoader.loadProperty("frontend_password_reset_route");
  } 

  public void sendWelcomeEmail(String sendToEmail, String sendToName) {
    String filePath = "/emails/WelcomeEmail.html";

    Map<String, String> templateValues = new HashMap<>();
    templateValues.put("name", sendToName);
    templateValues.put("faqLink", frontendUrl + "/faq");
    templateValues.put("link", frontendUrl);
    Optional<String> emailBody = emailOperations.getTemplateString(filePath, templateValues);

    emailBody.ifPresent(
        s -> emailOperations.sendEmailToOneRecipient(sendToName, sendToEmail, subjectWelcome, s));
  }

  public void sendEmailChangeConfirmationEmail(
      String sendToEmail, String sendToName, String newEmail) {
    String filePath = "/emails/EmailChangeConfirmation.html";

    Map<String, String> templateValues = new HashMap<>();
    templateValues.put("new_email", newEmail);
    Optional<String> emailBody = emailOperations.getTemplateString(filePath, templateValues);

    emailBody.ifPresent(
        s ->
            emailOperations.sendEmailToOneRecipient(
                sendToName, sendToEmail, subjectEmailChange, s));
  }

  public void sendPasswordChangeRequestEmail(
      String sendToEmail, String sendToName, String passwordResetKey) {
    String filePath = "/emails/PasswordChangeRequest.html";

    Map<String, String> templateValues = new HashMap<>();
    templateValues.put("link", String.format(passwordResetTemplate, passwordResetKey));
    Optional<String> emailBody = emailOperations.getTemplateString(filePath, templateValues);

    emailBody.ifPresent(
        s ->
            emailOperations.sendEmailToOneRecipient(
                sendToName, sendToEmail, subjectPasswordResetRequest, s));
  }

  public void sendPasswordChangeConfirmationEmail(String sendToEmail, String sendToName) {
    String filePath = "/emails/PasswordChangeConfirmation.html";

    Map<String, String> templateValues = new HashMap<>();
    Optional<String> emailBody = emailOperations.getTemplateString(filePath, templateValues);

    emailBody.ifPresent(
        s ->
            emailOperations.sendEmailToOneRecipient(
                sendToName, sendToEmail, subjectPasswordResetConfirm, s));
  }

  public void sendAccountDeactivatedEmail(String sendToEmail, String sendToName) {
    String filePath = "/emails/AccountDeactivated.html";

    Map<String, String> templateValues = new HashMap<>();
    Optional<String> emailBody = emailOperations.getTemplateString(filePath, templateValues);

    emailBody.ifPresent(
        s ->
            emailOperations.sendEmailToOneRecipient(
                sendToName, sendToEmail, subjectAccountDeleted, s));
  }

  public void sendInviteTeamEmail(String sendToEmail, String sendToName, String teamName) {
    String filePath = "/emails/InviteEmail.html";

    Map<String, String> templateValues = new HashMap<>();
    templateValues.put("link", "");
    templateValues.put("team_name", teamName);
    Optional<String> emailBody = emailOperations.getTemplateString(filePath, templateValues);
    emailBody.ifPresent(
        s ->
            emailOperations.sendEmailToOneRecipient(
                sendToName, sendToEmail, subjectAccountDeleted, s));
    // TODO implement this
  }

  public void sendRejectImageEmail(String sendToEmail, String sendToName, String reason) {
    String filePath = "/emails/RejectImageEmail.html";

    Map<String, String> templateValues = new HashMap<>();
    templateValues.put("reason", reason);
    Optional<String> emailBody = emailOperations.getTemplateString(filePath, templateValues);

    emailBody.ifPresent(
            s ->
                    emailOperations.sendEmailToOneRecipient(
                            sendToName, sendToEmail, subjectImageRejected, s));
  }

  public void sendIssueReportEmail(
      String userFullName,
      String userEmail,
      int siteId,
      String reportReason,
      String reportDescription) {
    String filePath = "/emails/SiteIssueReport.html";

    Map<String, String> templateValues = new HashMap<>();
    templateValues.put("name", userFullName);
    templateValues.put("email", userEmail);
    templateValues.put("siteId", String.valueOf(siteId));
    templateValues.put("reportReason", reportReason);
    templateValues.put("reportDescription", reportDescription);

    Optional<String> emailBody = emailOperations.getTemplateString(filePath, templateValues);
    emailBody.ifPresent(
        s ->
            emailOperations.sendEmailToOneRecipient(
                senderName, reportEmailDestination, subjectSiteReport, s));
  }

  public void sendArbitraryEmail(HashSet<String> sendToEmails, String subject, String body,
                                 List<AttachmentResource> attachments) {
    String filePath = "/emails/Email.html";

    Map<String, String> templateValues = new HashMap<>();
    templateValues.put("body", body.replaceAll("\n", "<br />"));
    Optional<String> emailBody = emailOperations.getTemplateString(filePath, templateValues);

    emailBody.ifPresent(
        email -> emailOperations.sendEmailToMultipleRecipients(sendToEmails, subject, email, attachments));
  }
}
