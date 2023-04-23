package com.codeforcommunity.processor;

import static org.jooq.generated.Tables.ADOPTED_SITES;
import static org.jooq.generated.Tables.BLOCKS;
import static org.jooq.generated.Tables.NEIGHBORHOODS;
import static org.jooq.generated.Tables.PARENT_ACCOUNTS;
import static org.jooq.generated.Tables.SITES;
import static org.jooq.generated.Tables.SITE_ENTRIES;
import static org.jooq.generated.Tables.SITE_IMAGES;
import static org.jooq.generated.Tables.STEWARDSHIP;
import static org.jooq.generated.Tables.USERS;
import static org.jooq.impl.DSL.coalesce;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.noCondition;

import com.codeforcommunity.api.IProtectedSiteProcessor;
import com.codeforcommunity.auth.JWTData;
import com.codeforcommunity.dto.site.AddSiteRequest;
import com.codeforcommunity.dto.site.AddSitesRequest;
import com.codeforcommunity.dto.site.AdoptedSitesResponse;
import com.codeforcommunity.dto.site.CSVSiteUpload;
import com.codeforcommunity.dto.site.EditSiteRequest;
import com.codeforcommunity.dto.site.EditStewardshipRequest;
import com.codeforcommunity.dto.site.FilterSitesRequest;
import com.codeforcommunity.dto.site.FilterSitesResponse;
import com.codeforcommunity.dto.site.NameSiteEntryRequest;
import com.codeforcommunity.dto.site.ParentAdoptSiteRequest;
import com.codeforcommunity.dto.site.ParentRecordStewardshipRequest;
import com.codeforcommunity.dto.site.RecordStewardshipRequest;
import com.codeforcommunity.dto.site.UpdateSiteRequest;
import com.codeforcommunity.dto.site.UploadSiteImageRequest;
import com.codeforcommunity.enums.PrivilegeLevel;
import com.codeforcommunity.exceptions.AuthException;
import com.codeforcommunity.exceptions.HandledException;
import com.codeforcommunity.exceptions.InvalidCSVException;
import com.codeforcommunity.exceptions.LinkedResourceDoesNotExistException;
import com.codeforcommunity.exceptions.ResourceDoesNotExistException;
import com.codeforcommunity.exceptions.WrongAdoptionStatusException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.Table;
import org.jooq.generated.tables.records.AdoptedSitesRecord;
import org.jooq.generated.tables.records.ParentAccountsRecord;
import org.jooq.generated.tables.records.SiteEntriesRecord;
import org.jooq.generated.tables.records.SitesRecord;
import org.jooq.generated.tables.records.StewardshipRecord;
import org.jooq.generated.tables.records.UsersRecord;

public class ProtectedSiteProcessorImpl extends AbstractProcessor
    implements IProtectedSiteProcessor {

  private final DSLContext db;

  public ProtectedSiteProcessorImpl(DSLContext db) {
    this.db = db;
  }

  /**
   * Check if a site with the given siteId exists.
   *
   * @param siteId to check
   */
  private void checkSiteExists(int siteId) {
    if (!db.fetchExists(db.selectFrom(SITES).where(SITES.ID.eq(siteId)))) {
      throw new ResourceDoesNotExistException(siteId, "Site");
    }
  }

  /**
   * Check if an entry with the given entryId exists.
   *
   * @param entryId to check
   */
  private void checkEntryExists(int entryId) {
    if (!db.fetchExists(db.selectFrom(SITE_ENTRIES).where(SITE_ENTRIES.ID.eq(entryId)))) {
      throw new ResourceDoesNotExistException(entryId, "Entry");
    }
  }

  /**
   * Check if a block with the given blockId exists.
   *
   * @param blockId to check
   */
  private void checkBlockExists(int blockId) {
    if (!db.fetchExists(db.selectFrom(BLOCKS).where(BLOCKS.ID.eq(blockId)))) {
      throw new ResourceDoesNotExistException(blockId, "Block");
    }
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
   * Check if a stewardship record exists.
   *
   * @param activityId to check
   */
  private void checkStewardshipExists(int activityId) {
    if (!db.fetchExists(db.selectFrom(STEWARDSHIP).where(STEWARDSHIP.ID.eq(activityId)))) {
      throw new ResourceDoesNotExistException(activityId, "Stewardship Activity");
    }
  }

  /**
   * Check if an image exists
   *
   * @param imageId to check
   */
  private void checkImageExists(int imageId) {
    if (!db.fetchExists(db.selectFrom(SITE_IMAGES).where(SITE_IMAGES.ID.eq(imageId)))) {
      throw new ResourceDoesNotExistException(imageId, "Site Image");
    }
  }

  /**
   * Check if the user is an admin or the adopter of the site with the given siteId
   *
   * @param userData the user's data
   * @param siteId the ID of the site to check
   * @throws AuthException if the user is not an admin or the site's adopter
   */
  private void checkAdminOrSiteAdopter(JWTData userData, int siteId) throws AuthException {
    if (!(isAdmin(userData.getPrivilegeLevel())
        || isAlreadyAdoptedByUser(userData.getUserId(), siteId))) {
      throw new AuthException("User needs to be an admin or the site's adopter.");
    }
  }

  private Boolean isAlreadyAdopted(int siteId) {
    return db.fetchExists(db.selectFrom(ADOPTED_SITES).where(ADOPTED_SITES.SITE_ID.eq(siteId)));
  }

  private Boolean isAlreadyAdoptedByUser(int userId, int siteId) {
    return db.fetchExists(
        db.selectFrom(ADOPTED_SITES)
            .where(ADOPTED_SITES.USER_ID.eq(userId))
            .and(ADOPTED_SITES.SITE_ID.eq(siteId)));
  }

  /**
   * Is the user an admin or super admin.
   *
   * @param level the privilege level of the user calling the route
   * @return true if user is ADMIN or SUPER_ADMIN, else false
   */
  boolean isAdmin(PrivilegeLevel level) {
    return level.equals(PrivilegeLevel.ADMIN) || level.equals(PrivilegeLevel.SUPER_ADMIN);
  }

  private boolean isAdminOrOwner(JWTData userData, Integer ownerId) {
    return isAdmin(userData.getPrivilegeLevel()) || userData.getUserId().equals(ownerId);
  }

  /**
   * Throws an exception if the user account is not the parent of the other user account.
   *
   * @param parentUserId the user id of the parent account
   * @param childUserId the user id of the child account
   */
  void checkParent(int parentUserId, int childUserId) {
    if (!isParent(parentUserId, childUserId)) {
      throw new LinkedResourceDoesNotExistException(
          "Parent->Child", parentUserId, "Parent User", childUserId, "Child User");
    }
  }

  /**
   * Determines if a user account is the parent of another user account.
   *
   * @param parentUserId the user id of the parent account
   * @param childUserId the user if of the child account
   * @return true if the user is a parent of the other user, else false
   */
  boolean isParent(int parentUserId, int childUserId) {
    ParentAccountsRecord parentAccountsRecord =
        db.selectFrom(PARENT_ACCOUNTS)
            .where(PARENT_ACCOUNTS.PARENT_ID.eq(parentUserId))
            .and(PARENT_ACCOUNTS.CHILD_ID.eq(childUserId))
            .fetchOne();
    return parentAccountsRecord != null;
  }

  /**
   * Gets the JWTData of the user with the given userId.
   *
   * @param userId user id of the user to get JWTData for
   * @return JWTData of the user
   */
  private JWTData getUserData(int userId) {
    UsersRecord user = db.selectFrom(USERS).where(USERS.ID.eq(userId)).fetchOne();
    PrivilegeLevel userPrivilegeLevel = user.getPrivilegeLevel();

    return new JWTData(userId, userPrivilegeLevel);
  }

  @Override
  public void adoptSite(JWTData userData, int siteId, Date dateAdopted) {
    checkSiteExists(siteId);
    if (isAlreadyAdopted(siteId)) {
      throw new WrongAdoptionStatusException(true);
    }

    AdoptedSitesRecord record = db.newRecord(ADOPTED_SITES);
    record.setUserId(userData.getUserId());
    record.setSiteId(siteId);
    record.setDateAdopted(dateAdopted);
    record.store();
  }

  @Override
  public void unadoptSite(JWTData userData, int siteId) {
    checkSiteExists(siteId);
    if (!isAlreadyAdoptedByUser(userData.getUserId(), siteId)) {
      throw new WrongAdoptionStatusException(false);
    }

    db.deleteFrom(ADOPTED_SITES)
        .where(ADOPTED_SITES.USER_ID.eq(userData.getUserId()))
        .and(ADOPTED_SITES.SITE_ID.eq(siteId))
        .execute();
  }

  @Override
  public void forceUnadoptSite(JWTData userData, int siteId) {
    assertAdminOrSuperAdmin(userData.getPrivilegeLevel());
    checkSiteExists(siteId);
    if (!isAlreadyAdopted(siteId)) {
      throw new WrongAdoptionStatusException(false);
    }

    AdoptedSitesRecord adoptedSite =
        db.selectFrom(ADOPTED_SITES)
            .where(ADOPTED_SITES.SITE_ID.eq(siteId))
            .fetchInto(AdoptedSitesRecord.class)
            .get(0);

    Integer adopterId = adoptedSite.getUserId();

    UsersRecord adopter = db.selectFrom(USERS).where(USERS.ID.eq(adopterId)).fetchOne();

    if (isAdmin(adopter.getPrivilegeLevel())
        && !(userData.getPrivilegeLevel().equals(PrivilegeLevel.SUPER_ADMIN))) {
      throw new AuthException("User does not have the required privilege level.");
    }

    db.deleteFrom(ADOPTED_SITES).where(ADOPTED_SITES.SITE_ID.eq(siteId)).execute();
  }

  @Override
  public void parentAdoptSite(
      JWTData parentUserData,
      int siteId,
      ParentAdoptSiteRequest parentAdoptSiteRequest,
      Date dateAdopted) {
    Integer parentId = parentUserData.getUserId();
    Integer childId = parentAdoptSiteRequest.getChildUserId();
    checkParent(parentId, childId);

    JWTData childUserData = getUserData(childId);

    adoptSite(childUserData, siteId, dateAdopted);
  }

  @Override
  public AdoptedSitesResponse getAdoptedSites(JWTData userData) {
    List<Integer> favoriteSites =
        db.selectFrom(ADOPTED_SITES)
            .where(ADOPTED_SITES.USER_ID.eq(userData.getUserId()))
            .fetch(ADOPTED_SITES.SITE_ID);

    return new AdoptedSitesResponse(favoriteSites);
  }

  @Override
  public void recordStewardship(
      JWTData userData, int siteId, RecordStewardshipRequest recordStewardshipRequest) {
    checkSiteExists(siteId);
    if (!isAlreadyAdoptedByUser(userData.getUserId(), siteId)) {
      throw new WrongAdoptionStatusException(false);
    }

    StewardshipRecord record = db.newRecord(STEWARDSHIP);
    record.setUserId(userData.getUserId());
    record.setSiteId(siteId);
    record.setPerformedOn(recordStewardshipRequest.getDate());
    record.setWatered(recordStewardshipRequest.getWatered());
    record.setMulched(recordStewardshipRequest.getMulched());
    record.setCleaned(recordStewardshipRequest.getCleaned());
    record.setWeeded(recordStewardshipRequest.getWeeded());

    record.store();
  }

  @Override
  public void parentRecordStewardship(
      JWTData parentUserData,
      int siteId,
      ParentRecordStewardshipRequest parentRecordStewardshipRequest) {
    Integer parentId = parentUserData.getUserId();
    Integer childId = parentRecordStewardshipRequest.getChildUserId();
    checkParent(parentId, childId);

    JWTData childUserData = getUserData(childId);

    recordStewardship(childUserData, siteId, parentRecordStewardshipRequest);
  }

  private void populateSiteEntry(SiteEntriesRecord record, UpdateSiteRequest updateSiteRequest) {
    record.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
    record.setTreePresent(updateSiteRequest.isTreePresent());
    record.setStatus(updateSiteRequest.getStatus());
    record.setGenus(updateSiteRequest.getGenus());
    record.setSpecies(updateSiteRequest.getSpecies());
    record.setCommonName(updateSiteRequest.getCommonName());
    record.setConfidence(updateSiteRequest.getConfidence());
    record.setDiameter(updateSiteRequest.getDiameter());
    record.setCircumference(updateSiteRequest.getCircumference());
    record.setMultistem(updateSiteRequest.isMultistem());
    record.setCoverage(updateSiteRequest.getCoverage());
    record.setPruning(updateSiteRequest.getPruning());
    record.setCondition(updateSiteRequest.getCondition());
    record.setDiscoloring(updateSiteRequest.isDiscoloring());
    record.setLeaning(updateSiteRequest.isLeaning());
    record.setConstrictingGrate(updateSiteRequest.isConstrictingGrate());
    record.setWounds(updateSiteRequest.isWounds());
    record.setPooling(updateSiteRequest.isPooling());
    record.setStakesWithWires(updateSiteRequest.isStakesWithWires());
    record.setStakesWithoutWires(updateSiteRequest.isStakesWithoutWires());
    record.setLight(updateSiteRequest.isLight());
    record.setBicycle(updateSiteRequest.isBicycle());
    record.setBagEmpty(updateSiteRequest.isBagEmpty());
    record.setBagFilled(updateSiteRequest.isBagFilled());
    record.setTape(updateSiteRequest.isTape());
    record.setSuckerGrowth(updateSiteRequest.isSuckerGrowth());
    record.setSiteType(updateSiteRequest.getSiteType());
    record.setSidewalkWidth(updateSiteRequest.getSidewalkWidth());
    record.setSiteWidth(updateSiteRequest.getSiteWidth());
    record.setSiteLength(updateSiteRequest.getSiteLength());
    record.setMaterial(updateSiteRequest.getMaterial());
    record.setRaisedBed(updateSiteRequest.isRaisedBed());
    record.setFence(updateSiteRequest.isFence());
    record.setTrash(updateSiteRequest.isTrash());
    record.setWires(updateSiteRequest.isWires());
    record.setGrate(updateSiteRequest.isGrate());
    record.setStump(updateSiteRequest.isStump());
    record.setTreeNotes(updateSiteRequest.getTreeNotes());
    record.setSiteNotes(updateSiteRequest.getSiteNotes());
    record.setPlantingDate(updateSiteRequest.getPlantingDate());
  }

  @Override
  public void addSite(JWTData userData, AddSiteRequest addSiteRequest) {
    if (addSiteRequest.getBlockId() != null) {
      checkBlockExists(addSiteRequest.getBlockId());
    }

    checkNeighborhoodExists(addSiteRequest.getNeighborhoodId());

    SitesRecord sitesRecord = db.newRecord(SITES);

    int newId = db.select(max(SITES.ID)).from(SITES).fetchOne(0, Integer.class) + 1;

    sitesRecord.setId(newId);
    sitesRecord.setBlockId(addSiteRequest.getBlockId());
    sitesRecord.setLat(addSiteRequest.getLat());
    sitesRecord.setLng(addSiteRequest.getLng());
    sitesRecord.setCity(addSiteRequest.getCity());
    sitesRecord.setZip(addSiteRequest.getZip());
    sitesRecord.setAddress(addSiteRequest.getAddress());
    sitesRecord.setNeighborhoodId(addSiteRequest.getNeighborhoodId());

    sitesRecord.store();

    SiteEntriesRecord siteEntriesRecord = db.newRecord(SITE_ENTRIES);

    int newSiteEntriesId =
        db.select(max(SITE_ENTRIES.ID)).from(SITE_ENTRIES).fetchOne(0, Integer.class) + 1;

    siteEntriesRecord.setId(newSiteEntriesId);
    siteEntriesRecord.setUserId(userData.getUserId());
    siteEntriesRecord.setSiteId(sitesRecord.getId());
    populateSiteEntry(siteEntriesRecord, addSiteRequest);

    siteEntriesRecord.store();
  }

  public void updateSite(JWTData userData, int siteId, UpdateSiteRequest updateSiteRequest) {
    checkSiteExists(siteId);

    SiteEntriesRecord record = db.newRecord(SITE_ENTRIES);

    int newId = db.select(max(SITE_ENTRIES.ID)).from(SITE_ENTRIES).fetchOne(0, Integer.class) + 1;

    record.setId(newId);
    record.setUserId(userData.getUserId());
    record.setSiteId(siteId);
    populateSiteEntry(record, updateSiteRequest);

    record.store();
  }

  @Override
  public void editSite(JWTData userData, int siteId, EditSiteRequest editSiteRequest) {
    assertAdminOrSuperAdmin(userData.getPrivilegeLevel());
    checkSiteExists(siteId);
    if (editSiteRequest.getBlockId() != null) {
      checkBlockExists(editSiteRequest.getBlockId());
    }
    checkNeighborhoodExists(editSiteRequest.getNeighborhoodId());

    SitesRecord site = db.selectFrom(SITES).where(SITES.ID.eq(siteId)).fetchOne();

    site.setId(siteId);
    site.setBlockId(editSiteRequest.getBlockId());
    site.setAddress(editSiteRequest.getAddress());
    site.setCity(editSiteRequest.getCity());
    site.setZip(editSiteRequest.getZip());
    site.setLat(editSiteRequest.getLat());
    site.setLng(editSiteRequest.getLng());
    site.setNeighborhoodId(editSiteRequest.getNeighborhoodId());

    site.store();
  }

  @Override
  public void addSites(JWTData userData, AddSitesRequest addSitesRequest) {
    assertAdminOrSuperAdmin(userData.getPrivilegeLevel());

    List<AddSiteRequest> addSiteRequests = this.parseCSVString(addSitesRequest.getCsvText());

    addSiteRequests.forEach(siteRequest -> addSite(userData, siteRequest));
  }

  /**
   * Parses the given CSV string containing data on a site and site entry to a list of
   * AddSiteRequests.
   *
   * @param sitesCSV CSV string to parse
   * @throws HandledException if the given CSV string cannot be parsed properly
   * @return the parsed list of AddSiteRequests
   */
  private List<AddSiteRequest> parseCSVString(String sitesCSV) throws HandledException {
    try {
      CsvMapper mapper = new CsvMapper();
      CsvSchema schema = CsvSchema.emptySchema().withHeader();
      MappingIterator<CSVSiteUpload> sitesIterator =
          mapper.readerFor(CSVSiteUpload.class).with(schema).readValues(sitesCSV);
      List<CSVSiteUpload> csvSiteUploads = sitesIterator.readAll();
      List<AddSiteRequest> addSiteRequests =
          csvSiteUploads.stream().map(CSVSiteUpload::toAddSiteRequest).collect(Collectors.toList());
      if (addSiteRequests.size() == 0) {
        throw new InvalidCSVException();
      }
      addSiteRequests.forEach(siteRequest -> siteRequest.validate());
      return addSiteRequests;
    } catch (HandledException | IOException e) {
      throw new InvalidCSVException();
    }
  }

  @Override
  public void deleteSite(JWTData userData, int siteId) {
    assertAdminOrSuperAdmin(userData.getPrivilegeLevel());
    checkSiteExists(siteId);

    SitesRecord site = db.selectFrom(SITES).where(SITES.ID.eq(siteId)).fetchOne();
    site.setDeletedAt(new Timestamp(System.currentTimeMillis()));
    site.store();
  }

  @Override
  public void editStewardship(
      JWTData userData, int activityId, EditStewardshipRequest editStewardshipRequest) {
    checkStewardshipExists(activityId);
    StewardshipRecord activity =
        db.selectFrom(STEWARDSHIP).where(STEWARDSHIP.ID.eq(activityId)).fetchOne();

    if (!isAdminOrOwner(userData, activity.getUserId())) {
      throw new AuthException(
          "User needs to be an admin or the activity's author to edit the record.");
    }

    activity.setPerformedOn(editStewardshipRequest.getDate());
    activity.setWatered(editStewardshipRequest.getWatered());
    activity.setMulched(editStewardshipRequest.getMulched());
    activity.setCleaned(editStewardshipRequest.getCleaned());
    activity.setWeeded(editStewardshipRequest.getWeeded());

    activity.store();
  }

  public void deleteStewardship(JWTData userData, int activityId) {
    checkStewardshipExists(activityId);
    StewardshipRecord activity =
        db.selectFrom(STEWARDSHIP).where(STEWARDSHIP.ID.eq(activityId)).fetchOne();

    if (!isAdminOrOwner(userData, activity.getUserId())) {
      throw new AuthException(
          "User needs to be an admin or the activity's author to delete the record.");
    }

    db.deleteFrom(STEWARDSHIP).where(STEWARDSHIP.ID.eq(activityId)).execute();
  }

  public void nameSiteEntry(
      JWTData userData, int siteId, NameSiteEntryRequest nameSiteEntryRequest) {
    checkSiteExists(siteId);
    checkAdminOrSiteAdopter(userData, siteId);

    SiteEntriesRecord siteEntry =
        db.selectFrom(SITE_ENTRIES)
            .where(SITE_ENTRIES.SITE_ID.eq(siteId))
            .orderBy(SITE_ENTRIES.UPDATED_AT.desc())
            .fetchOne();

    if (siteEntry == null) {
      throw new LinkedResourceDoesNotExistException(
          "Site Entry", userData.getUserId(), "User", siteId, "Site");
    }

    if (nameSiteEntryRequest.getName().isEmpty()) {
      siteEntry.setTreeName(null);
    } else {
      siteEntry.setTreeName(nameSiteEntryRequest.getName());
    }

    siteEntry.store();
  }

  @Override
  public void uploadSiteImage(
      JWTData userData, int siteId, UploadSiteImageRequest uploadSiteImageRequest) {
    checkSiteExists(siteId);
    checkAdminOrSiteAdopter(userData, siteId);

    // TODO upload image to S3 and save URL to database

    // SitesRecord site = db.selectFrom(SITES).where(SITES.ID.eq(siteId)).fetchOne();

    // site.store();
  }

  @Override
  public void deleteSiteImage(JWTData userData, int imageId) {
    assertAdminOrSuperAdmin(userData.getPrivilegeLevel());
    checkImageExists(imageId);

    db.deleteFrom(SITE_IMAGES).where(SITE_IMAGES.ID.eq(imageId)).execute();
  }

  @Override
  public List<FilterSitesResponse> filterSites(
      JWTData userData, FilterSitesRequest filterSitesRequest) {
    assertAdminOrSuperAdmin(userData.getPrivilegeLevel());

    String ACTIVITY_COUNT_COLUMN = "act_count";

    // Table containing the number of stewardship activities performed by a user on a site
    Table<org.jooq.Record3<Integer, Integer, Integer>> activityCounts = db
        .select(count().as(ACTIVITY_COUNT_COLUMN), STEWARDSHIP.SITE_ID, STEWARDSHIP.USER_ID)
        .from(STEWARDSHIP)
        .groupBy(STEWARDSHIP.SITE_ID, STEWARDSHIP.USER_ID).asTable("activityCounts");

    Condition filterCondition = noCondition();
    if (filterSitesRequest.getTreeCommonNames() != null)
      filterCondition = filterCondition.and(SITE_ENTRIES.COMMON_NAME.in(filterSitesRequest.getTreeCommonNames()));
    if (filterSitesRequest.getAdoptedStart() != null)
      filterCondition =
          filterCondition.and(ADOPTED_SITES.DATE_ADOPTED.ge(filterSitesRequest.getAdoptedStart()));
    if (filterSitesRequest.getAdoptedEnd() != null)
      filterCondition =
          filterCondition.and(ADOPTED_SITES.DATE_ADOPTED.le(filterSitesRequest.getAdoptedEnd()));
    if (filterSitesRequest.getNeighborhoodIds() != null)
      filterCondition =
          filterCondition.and(SITES.NEIGHBORHOOD_ID.in(filterSitesRequest.getNeighborhoodIds()));
    if (filterSitesRequest.getActivityCountMin() != null)
      filterCondition = filterCondition.and(
          activityCounts.field(ACTIVITY_COUNT_COLUMN, Integer.class).ge(filterSitesRequest.getActivityCountMin()));
    if (filterSitesRequest.getActivityCountMax() != null)
      filterCondition = filterCondition.and(
        activityCounts.field(ACTIVITY_COUNT_COLUMN, Integer.class).le(filterSitesRequest.getActivityCountMax())
      );

    Condition stewardshipCondition = noCondition();
    if (filterSitesRequest.getLastActivityStart() != null)
      stewardshipCondition =
          stewardshipCondition.and(
              max(STEWARDSHIP.PERFORMED_ON).ge(filterSitesRequest.getLastActivityStart()));
    if (filterSitesRequest.getLastActivityEnd() != null)
      stewardshipCondition =
          stewardshipCondition.and(
              max(STEWARDSHIP.PERFORMED_ON).le(filterSitesRequest.getLastActivityEnd()));

    Result<org.jooq.Record12<Integer, String, Integer, Integer, Date, Date, Timestamp, String, String, String, String, Integer>>
        records = db
        .select(SITES.ID, SITES.ADDRESS, SITES.NEIGHBORHOOD_ID, ADOPTED_SITES.USER_ID, ADOPTED_SITES.DATE_ADOPTED,
            max(STEWARDSHIP.PERFORMED_ON).as(STEWARDSHIP.PERFORMED_ON),
            max(SITE_ENTRIES.UPDATED_AT).as(SITE_ENTRIES.UPDATED_AT),
            SITE_ENTRIES.COMMON_NAME, USERS.FIRST_NAME, USERS.LAST_NAME, USERS.EMAIL, coalesce(activityCounts.field(ACTIVITY_COUNT_COLUMN, Integer.class), 0).as(ACTIVITY_COUNT_COLUMN))
        .from(ADOPTED_SITES)
        .join(SITES).on(ADOPTED_SITES.SITE_ID.eq(SITES.ID))
        .join(USERS).on(ADOPTED_SITES.USER_ID.eq(USERS.ID))
        .leftJoin(STEWARDSHIP).on(ADOPTED_SITES.SITE_ID.eq(STEWARDSHIP.SITE_ID))
        .join(SITE_ENTRIES).on(ADOPTED_SITES.SITE_ID.eq(SITE_ENTRIES.SITE_ID))
        .leftJoin(activityCounts).on(
            ADOPTED_SITES.SITE_ID.eq(activityCounts.field(STEWARDSHIP.SITE_ID))
                .and(ADOPTED_SITES.USER_ID.eq(activityCounts.field(STEWARDSHIP.USER_ID))))
        .where(filterCondition)
        .groupBy(SITES.ID, SITES.ADDRESS, SITES.NEIGHBORHOOD_ID, ADOPTED_SITES.USER_ID, ADOPTED_SITES.DATE_ADOPTED, SITE_ENTRIES.COMMON_NAME, USERS.FIRST_NAME, USERS.LAST_NAME, USERS.EMAIL, activityCounts.field(ACTIVITY_COUNT_COLUMN))
        .having(stewardshipCondition)
        .fetch();

    return records.stream()
        .map(
            rec -> {
              String adopterName = rec.get(USERS.FIRST_NAME) + ' ' + rec.get(USERS.LAST_NAME);
              Integer lastActivityWeeks =
                  rec.get(STEWARDSHIP.PERFORMED_ON) != null
                      ? (int)
                          ChronoUnit.WEEKS.between(
                              rec.get(STEWARDSHIP.PERFORMED_ON).toLocalDate(), LocalDate.now())
                      : null;
              String dateAdopted =
                  rec.get(ADOPTED_SITES.DATE_ADOPTED) != null
                      ? rec.get(ADOPTED_SITES.DATE_ADOPTED).toString()
                      : "";

              return new FilterSitesResponse(
                  rec.get(SITES.ID),
                  rec.get(SITES.ADDRESS),
                  rec.get(ADOPTED_SITES.USER_ID),
                  adopterName,
                  rec.get(USERS.EMAIL),
                  dateAdopted,
                  rec.get(ACTIVITY_COUNT_COLUMN, Integer.class),
                  rec.get(SITES.NEIGHBORHOOD_ID),
                  lastActivityWeeks);
            })
        .collect(Collectors.toList());
  }

  public void editSiteEntry(JWTData userData, int entryId, UpdateSiteRequest editSiteEntryRequest) {
    checkEntryExists(entryId);
    assertAdminOrSuperAdmin(userData.getPrivilegeLevel());

    SiteEntriesRecord siteEntriesRecord =
        db.selectFrom(SITE_ENTRIES).where(SITE_ENTRIES.ID.eq(entryId)).fetchOne();

    siteEntriesRecord.setUserId(userData.getUserId());
    populateSiteEntry(siteEntriesRecord, editSiteEntryRequest);

    siteEntriesRecord.store();
  }
}
