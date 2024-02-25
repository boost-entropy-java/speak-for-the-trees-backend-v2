package com.codeforcommunity.processor;

import static org.jooq.generated.tables.Users.USERS;

import com.codeforcommunity.api.IProtectedEmailerProcessor;
import com.codeforcommunity.auth.JWTData;
import com.codeforcommunity.dto.emailer.AddTemplateRequest;
import com.codeforcommunity.dto.emailer.LoadTemplateResponse;
import com.codeforcommunity.exceptions.UserDoesNotExistException;
import com.codeforcommunity.propertiesLoader.PropertiesLoader;
import com.codeforcommunity.requester.S3Requester;
import org.jooq.DSLContext;
import org.jooq.generated.tables.records.UsersRecord;

import java.util.List;

public class ProtectedEmailerProcessorImpl extends AbstractProcessor
    implements IProtectedEmailerProcessor {

  private final DSLContext db;

  public ProtectedEmailerProcessorImpl(DSLContext db) {
    this.db = db;
  }

  @Override
  public void addTemplate(JWTData userData, AddTemplateRequest addTemplateRequest) {
    assertAdminOrSuperAdmin(userData.getPrivilegeLevel());
    S3Requester.uploadHTML(
        addTemplateRequest.getName(), userData.getUserId(), addTemplateRequest.getTemplate());
  }

  @Override
  public LoadTemplateResponse loadTemplate(JWTData userData, String templateName) {
    assertAdminOrSuperAdmin(userData.getPrivilegeLevel());
    LoadTemplateResponse s3Response = S3Requester.loadHTML(templateName);
    int userId = Integer.parseInt(s3Response.getAuthor());
    // has ID of author, replace with fullname of author
    UsersRecord user = db.selectFrom(USERS).where(USERS.ID.eq(userId)).fetchOne();
    if (user == null) {
      throw new UserDoesNotExistException(userId);
    }

    String fullname = user.getFirstName() + " " + user.getLastName();
    LoadTemplateResponse loadTemplateResponse =
        new LoadTemplateResponse(s3Response.getTemplate(), s3Response.getName(), fullname);

    return loadTemplateResponse;
  }

  @Override
  public void deleteTemplate(JWTData userData, String templateName) {
    assertAdminOrSuperAdmin(userData.getPrivilegeLevel());
    S3Requester.deleteHTML(templateName);
  }

  @Override
  public List<String> loadTemplateNames(JWTData userData) {
    assertAdminOrSuperAdmin(userData.getPrivilegeLevel());
    List<String> names = S3Requester.getAllNamesinBucket(PropertiesLoader.loadProperty("aws_s3_bucket_name"));
    return names;
  }
}
