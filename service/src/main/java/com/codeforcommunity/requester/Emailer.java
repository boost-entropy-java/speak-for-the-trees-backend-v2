package com.codeforcommunity.requester;

import com.codeforcommunity.email.EmailOperations;
import com.codeforcommunity.propertiesLoader.PropertiesLoader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

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

  public Emailer() {
    String senderName = PropertiesLoader.loadProperty("email_sender_name");
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

  public void sendArbitraryEmail(HashSet<String> sendToEmails, String subject, String body) {
    String filePath = "/emails/Email.html";

    Map<String, String> templateValues = new HashMap<>();
    templateValues.put("body", body);
    Optional<String> emailBody = emailOperations.getTemplateString(filePath, templateValues);

    emailBody.ifPresent(email ->
        emailOperations.sendEmailToMultipleRecipients(sendToEmails, subject, email)
    );
  }
}
