package com.codeforcommunity.processor;

import com.codeforcommunity.api.IProtectedEmailerProcessor;
import com.codeforcommunity.auth.JWTData;
import com.codeforcommunity.dto.emailer.AddTemplateRequest;
import com.codeforcommunity.dto.emailer.LoadTemplateResponse;
import com.codeforcommunity.dto.leaderboard.LeaderboardEntry;
import com.codeforcommunity.enums.PrivilegeLevel;
import com.codeforcommunity.enums.ReservationAction;
import com.codeforcommunity.exceptions.UserDoesNotExistException;
import com.codeforcommunity.requester.S3Requester;

import org.jooq.DSLContext;

import java.util.List;

import static org.jooq.impl.DSL.count;
import static org.jooq.generated.tables.Users.USERS;
import org.jooq.generated.tables.records.UsersRecord;

public class ProtectedEmailerProcessorImpl extends AbstractProcessor
    implements IProtectedEmailerProcessor {

  static public String TEMPLATE_DIR = "email_templates";

  private final DSLContext db;

  public ProtectedEmailerProcessorImpl(DSLContext db) { this.db = db; }

  @Override
  public void addTemplate(JWTData userData, AddTemplateRequest addTemplateRequest) {
    assertAdminOrSuperAdmin(userData.getPrivilegeLevel());
    S3Requester.uploadHTML(
        addTemplateRequest.getName(),
        TEMPLATE_DIR,
        userData.getUserId(),
        addTemplateRequest.getTemplate());
  }

  @Override
  public LoadTemplateResponse loadTemplate(JWTData userData, String templateName) {
    assertAdminOrSuperAdmin(userData.getPrivilegeLevel());
    LoadTemplateResponse s3Response = S3Requester.loadHTML(templateName, TEMPLATE_DIR);
    int userId = Integer.parseInt(s3Response.getAuthor());
    // has ID of author, replace with fullname of author
    UsersRecord user = db.selectFrom(USERS)
            .where(USERS.ID.eq(userId)).fetchOne();
    if (user == null) {
      throw new UserDoesNotExistException(userId);
    }

    String fullname = user.getFirstName() + " " + user.getLastName();
    LoadTemplateResponse loadTemplateResponse = new LoadTemplateResponse(
            s3Response.getTemplate(),
            s3Response.getName(),
            fullname
    );

    return loadTemplateResponse;
  }
}
