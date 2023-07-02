package com.codeforcommunity.processor;

import static org.jooq.generated.Tables.ADOPTED_SITES;
import static org.jooq.generated.Tables.NEIGHBORHOODS;
import static org.jooq.generated.Tables.SITES;
import static org.jooq.generated.Tables.STEWARDSHIP;
import static org.jooq.generated.tables.Users.USERS;
import static org.jooq.impl.DSL.concat;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.val;

import com.codeforcommunity.api.IProtectedReportProcessor;
import com.codeforcommunity.auth.JWTData;
import com.codeforcommunity.dto.report.AdoptedSite;
import com.codeforcommunity.dto.report.GetAdoptionReportResponse;
import com.codeforcommunity.dto.report.GetReportCSVRequest;
import com.codeforcommunity.dto.report.GetStewardshipReportResponse;
import com.codeforcommunity.dto.report.Stewardship;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.jooq.DSLContext;

public class ProtectedReportProcessorImpl extends AbstractProcessor
    implements IProtectedReportProcessor {
  private final DSLContext db;

  public ProtectedReportProcessorImpl(DSLContext db) {
    this.db = db;
  }

  /**
   * Converts the previousDays parameter into a Date object. The earliest possible date returned is
   * January 1, 1970 (Unix epoch).
   *
   * @param getReportCSVRequest CSV report route request DTO
   * @return a Date object which is previousDays days before the current date
   */
  private Date getStartDate(GetReportCSVRequest getReportCSVRequest) {
    Long previousDays = getReportCSVRequest.getPreviousDays();
    java.util.Date startDate =
        java.util.Date.from(
            LocalDate.now()
                .minusDays(previousDays)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant());

    // ensure that the date is after the Unix epoch
    if (startDate.getTime() < 0) {
      startDate = new java.util.Date(0);
    }

    return new Date(startDate.getTime());
  }

  @Override
  public GetAdoptionReportResponse getAdoptionReport(JWTData userData) {
    assertAdminOrSuperAdmin(userData.getPrivilegeLevel());

    // get all adopted sites
    List<AdoptedSite> adoptedSites = queryAdoptedSites(new Date(0));

    return new GetAdoptionReportResponse(adoptedSites);
  }

  @Override
  public String getAdoptionReportCSV(JWTData userData, GetReportCSVRequest getReportCSVRequest) {
    assertAdminOrSuperAdmin(userData.getPrivilegeLevel());

    Date startDate = getStartDate(getReportCSVRequest);

    List<AdoptedSite> adoptedSites = queryAdoptedSites(startDate);

    StringBuilder builder = new StringBuilder();
    builder.append("Site ID, Address, Name, Email, Date Adopted, Activity Count, Neighborhood\n");
    for (AdoptedSite site : adoptedSites) {
      builder
          .append(site.getSiteId())
          .append(", ")
          .append(site.getAddress())
          .append(", ")
          .append(site.getName())
          .append(", ")
          .append(site.getEmail())
          .append(", ")
          .append(site.getDateAdopted())
          .append(", ")
          .append(site.getActivityCount())
          .append(", ")
          .append(site.getNeighborhood())
          .append("\n");
    }

    return builder.toString();
  }

  /**
   * Query sites that have been adopted at or after the given time.
   *
   * @param startDate sites adopted after this time, in milliseconds, are included in the returned
   *     list
   * @return list of sites that have been adopted at or after the given time
   */
  private List<AdoptedSite> queryAdoptedSites(Date startDate) {
    List<AdoptedSite> adoptedSites =
        db.select(
                SITES.ID,
                SITES.ADDRESS,
                concat(USERS.FIRST_NAME, val(" "), USERS.LAST_NAME),
                USERS.EMAIL,
                ADOPTED_SITES.DATE_ADOPTED,
                count(STEWARDSHIP.ID),
                NEIGHBORHOODS.NEIGHBORHOOD_NAME)
            .from(ADOPTED_SITES)
            .leftJoin(SITES)
            .on(ADOPTED_SITES.SITE_ID.eq(SITES.ID))
            .leftJoin(USERS)
            .on(ADOPTED_SITES.USER_ID.eq(USERS.ID))
            .leftJoin(STEWARDSHIP)
            .on(ADOPTED_SITES.SITE_ID.eq(STEWARDSHIP.SITE_ID))
            .leftJoin(NEIGHBORHOODS)
            .on(SITES.NEIGHBORHOOD_ID.eq(NEIGHBORHOODS.ID))
            .where(ADOPTED_SITES.DATE_ADOPTED.ge(startDate))
            .or(TimeUnit.MILLISECONDS.toDays(startDate.getTime()) == 0)
            .groupBy(
                SITES.ID,
                SITES.ADDRESS,
                USERS.FIRST_NAME,
                USERS.LAST_NAME,
                USERS.EMAIL,
                ADOPTED_SITES.DATE_ADOPTED,
                NEIGHBORHOODS.NEIGHBORHOOD_NAME)
            .orderBy(USERS.FIRST_NAME, USERS.LAST_NAME, ADOPTED_SITES.DATE_ADOPTED, SITES.ID)
            .fetchInto(AdoptedSite.class);

    return adoptedSites;
  }

  @Override
  public GetStewardshipReportResponse getStewardshipReport(JWTData userData) {
    assertAdminOrSuperAdmin(userData.getPrivilegeLevel());

    // get all stewardships
    List<Stewardship> stewardships = queryStewardships(new Date(0));

    return new GetStewardshipReportResponse(stewardships);
  }

  @Override
  public String getStewardshipReportCSV(JWTData userData, GetReportCSVRequest getReportCSVRequest) {
    assertAdminOrSuperAdmin(userData.getPrivilegeLevel());

    Date startDate = getStartDate(getReportCSVRequest);

    List<Stewardship> stewardships = queryStewardships(startDate);

    StringBuilder builder = new StringBuilder();
    builder.append(
        "Site ID, Address, Name, Email, Date Performed, Watered, Mulched, Cleaned, Weeded, Installed Watering Bag, Neighborhood\n");
    for (Stewardship site : stewardships) {
      builder
          .append(site.getSiteId())
          .append(", ")
          .append(site.getAddress())
          .append(", ")
          .append(site.getName())
          .append(", ")
          .append(site.getEmail())
          .append(", ")
          .append(site.getDatePerformed())
          .append(", ")
          .append(site.getWatered())
          .append(", ")
          .append(site.getMulched())
          .append(", ")
          .append(site.getCleaned())
          .append(", ")
          .append(site.getWeeded())
          .append(", ")
          .append(site.getInstalledWateringBag())
          .append(", ")
          .append(site.getNeighborhood())
          .append("\n");
    }

    return builder.toString();
  }

  /**
   * Query stewardship activities that have been performed at or after the given date.
   *
   * @param startDate stewardship activities performed after this date are included in the returned
   *     list
   * @return list of stewardship activities that have been performed at or after the given date
   */
  private List<Stewardship> queryStewardships(Date startDate) {
    List<Stewardship> stewardships =
        db.select(
                SITES.ID,
                SITES.ADDRESS,
                concat(USERS.FIRST_NAME, val(" "), USERS.LAST_NAME),
                USERS.EMAIL,
                STEWARDSHIP.PERFORMED_ON,
                STEWARDSHIP.WATERED,
                STEWARDSHIP.MULCHED,
                STEWARDSHIP.CLEANED,
                STEWARDSHIP.WEEDED,
                STEWARDSHIP.INSTALLED_WATERING_BAG,
                NEIGHBORHOODS.NEIGHBORHOOD_NAME)
            .from(STEWARDSHIP)
            .leftJoin(SITES)
            .on(STEWARDSHIP.SITE_ID.eq(SITES.ID))
            .leftJoin(USERS)
            .on(USERS.ID.eq(STEWARDSHIP.USER_ID))
            .leftJoin(NEIGHBORHOODS)
            .on(SITES.NEIGHBORHOOD_ID.eq(NEIGHBORHOODS.ID))
            .where(STEWARDSHIP.PERFORMED_ON.ge(startDate))
            .orderBy(USERS.FIRST_NAME, USERS.LAST_NAME, STEWARDSHIP.PERFORMED_ON, SITES.ID)
            .fetchInto(Stewardship.class);

    return stewardships;
  }
}
