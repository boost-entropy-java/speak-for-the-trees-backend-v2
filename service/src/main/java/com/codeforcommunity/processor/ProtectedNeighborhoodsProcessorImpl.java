package com.codeforcommunity.processor;

import static org.jooq.generated.Tables.NEIGHBORHOODS;

import com.codeforcommunity.api.IProtectedNeighborhoodsProcessor;
import com.codeforcommunity.auth.JWTData;
import com.codeforcommunity.dto.neighborhoods.EditCanopyCoverageRequest;
import com.codeforcommunity.dto.neighborhoods.SendEmailRequest;
import com.codeforcommunity.exceptions.MalformedParameterException;
import com.codeforcommunity.exceptions.ResourceDoesNotExistException;
import com.codeforcommunity.requester.Emailer;
import java.util.HashSet;
import java.util.List;
import org.jooq.DSLContext;
import org.jooq.generated.tables.records.NeighborhoodsRecord;

public class ProtectedNeighborhoodsProcessorImpl extends AbstractProcessor
    implements IProtectedNeighborhoodsProcessor {
  private final DSLContext db;
  private final Emailer emailer;

  public ProtectedNeighborhoodsProcessorImpl(DSLContext db, Emailer emailer) {
    this.db = db;
    this.emailer = emailer;
  }

  @Override
  public void sendEmail(JWTData userData, SendEmailRequest sendEmailRequest) {
    List<String> emails = sendEmailRequest.getEmails();
    if (emails.size() == 0) {
      return;
    }

    String emailSubject = sendEmailRequest.getEmailSubject();
    String emailBody = sendEmailRequest.getEmailBody();

    emailer.sendArbitraryEmail(new HashSet<>(emails), emailSubject, emailBody);
  }

  public void editCanopyCoverage(
      JWTData userData, int neighborhoodID, EditCanopyCoverageRequest editCanopyCoverageRequest) {
    assertAdminOrSuperAdmin(userData.getPrivilegeLevel());
    checkNeighborhoodExists(neighborhoodID);

    Double canopyCoverage = editCanopyCoverageRequest.getCanopyCoverage();
    checkIfCanopyCoverageValid(canopyCoverage);
    NeighborhoodsRecord record =
        db.selectFrom(NEIGHBORHOODS).where(NEIGHBORHOODS.ID.eq(neighborhoodID)).fetchOne();

    record.setCanopyCoverage(canopyCoverage);

    record.store();
  }

  /**
   * Check if a neighborhood with the given neighborhoodId exists.
   *
   * @param neighborhoodId to check
   */
  private void checkNeighborhoodExists(int neighborhoodId) {
    if (!db.fetchExists(db.selectFrom(NEIGHBORHOODS).where(NEIGHBORHOODS.ID.eq(neighborhoodId)))) {
      throw new ResourceDoesNotExistException(neighborhoodId, "Neighborhood");
    }
  }

  /**
   * Checks if the given canopyCoverage is valid. An invalid canopyCoverage is negative or greater
   * than one.
   *
   * @param canopyCoverage to check
   */
  private void checkIfCanopyCoverageValid(Double canopyCoverage) {
    if (canopyCoverage < 0 || canopyCoverage > 1) {
      throw new MalformedParameterException(
          "Given canopy coverage is not valid: " + canopyCoverage);
    }
  }
}
