package com.codeforcommunity.processor;

import static org.jooq.generated.Tables.ADOPTED_SITES;
import static org.jooq.generated.Tables.BLOCKS;
import static org.jooq.generated.Tables.NEIGHBORHOODS;
import static org.jooq.generated.Tables.PARENT_ACCOUNTS;
import static org.jooq.generated.Tables.SITES;
import static org.jooq.generated.Tables.SITE_ENTRIES;
import static org.jooq.generated.Tables.STEWARDSHIP;
import static org.jooq.generated.Tables.USERS;
import static org.jooq.impl.DSL.max;

import com.codeforcommunity.api.IProtectedSiteProcessor;
import com.codeforcommunity.auth.JWTData;
import com.codeforcommunity.dto.site.AddSiteRequest;
import com.codeforcommunity.dto.site.AddSitesRequest;
import com.codeforcommunity.dto.site.AdoptedSitesResponse;
import com.codeforcommunity.dto.site.EditSiteRequest;
import com.codeforcommunity.dto.site.EditStewardshipRequest;
import com.codeforcommunity.dto.site.NameSiteEntryRequest;
import com.codeforcommunity.dto.site.ParentAdoptSiteRequest;
import com.codeforcommunity.dto.site.ParentRecordStewardshipRequest;
import com.codeforcommunity.dto.site.RecordStewardshipRequest;
import com.codeforcommunity.dto.site.UpdateSiteRequest;
import com.codeforcommunity.enums.PrivilegeLevel;
import com.codeforcommunity.exceptions.AuthException;
import com.codeforcommunity.exceptions.LinkedResourceDoesNotExistException;
import com.codeforcommunity.exceptions.ResourceDoesNotExistException;
import com.codeforcommunity.exceptions.WrongAdoptionStatusException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import org.jooq.DSLContext;
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
      throw new LinkedResourceDoesNotExistException("Parent->Child",
          parentUserId,
          "Parent User",
          childUserId,
          "Child User");
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
    ParentAccountsRecord parentAccountsRecord = db.selectFrom(PARENT_ACCOUNTS)
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
    UsersRecord user = db.selectFrom(USERS)
        .where(USERS.ID.eq(userId))
        .fetchOne();
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
      JWTData parentUserData, int siteId, ParentAdoptSiteRequest parentAdoptSiteRequest, Date dateAdopted) {
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
      JWTData parentUserData, int siteId, ParentRecordStewardshipRequest parentRecordStewardshipRequest) {
    Integer parentId = parentUserData.getUserId();
    Integer childId = parentRecordStewardshipRequest.getChildUserId();
    checkParent(parentId, childId);

    JWTData childUserData = getUserData(childId);

    recordStewardship(childUserData, siteId, parentRecordStewardshipRequest);
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
    siteEntriesRecord.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
    siteEntriesRecord.setTreePresent(addSiteRequest.isTreePresent());
    siteEntriesRecord.setStatus(addSiteRequest.getStatus());
    siteEntriesRecord.setGenus(addSiteRequest.getGenus());
    siteEntriesRecord.setSpecies(addSiteRequest.getSpecies());
    siteEntriesRecord.setCommonName(addSiteRequest.getCommonName());
    siteEntriesRecord.setConfidence(addSiteRequest.getConfidence());
    siteEntriesRecord.setDiameter(addSiteRequest.getDiameter());
    siteEntriesRecord.setCircumference(addSiteRequest.getCircumference());
    siteEntriesRecord.setMultistem(addSiteRequest.isMultistem());
    siteEntriesRecord.setCoverage(addSiteRequest.getCoverage());
    siteEntriesRecord.setPruning(addSiteRequest.getPruning());
    siteEntriesRecord.setCondition(addSiteRequest.getCondition());
    siteEntriesRecord.setDiscoloring(addSiteRequest.isDiscoloring());
    siteEntriesRecord.setLeaning(addSiteRequest.isLeaning());
    siteEntriesRecord.setConstrictingGrate(addSiteRequest.isConstrictingGrate());
    siteEntriesRecord.setWounds(addSiteRequest.isWounds());
    siteEntriesRecord.setPooling(addSiteRequest.isPooling());
    siteEntriesRecord.setStakesWithWires(addSiteRequest.isStakesWithWires());
    siteEntriesRecord.setStakesWithoutWires(addSiteRequest.isStakesWithoutWires());
    siteEntriesRecord.setLight(addSiteRequest.isLight());
    siteEntriesRecord.setBicycle(addSiteRequest.isBicycle());
    siteEntriesRecord.setBagEmpty(addSiteRequest.isBagEmpty());
    siteEntriesRecord.setBagFilled(addSiteRequest.isBagFilled());
    siteEntriesRecord.setTape(addSiteRequest.isTape());
    siteEntriesRecord.setSuckerGrowth(addSiteRequest.isSuckerGrowth());
    siteEntriesRecord.setSiteType(addSiteRequest.getSiteType());
    siteEntriesRecord.setSidewalkWidth(addSiteRequest.getSidewalkWidth());
    siteEntriesRecord.setSiteWidth(addSiteRequest.getSiteWidth());
    siteEntriesRecord.setSiteLength(addSiteRequest.getSiteLength());
    siteEntriesRecord.setMaterial(addSiteRequest.getMaterial());
    siteEntriesRecord.setRaisedBed(addSiteRequest.isRaisedBed());
    siteEntriesRecord.setFence(addSiteRequest.isFence());
    siteEntriesRecord.setTrash(addSiteRequest.isTrash());
    siteEntriesRecord.setWires(addSiteRequest.isWires());
    siteEntriesRecord.setGrate(addSiteRequest.isGrate());
    siteEntriesRecord.setStump(addSiteRequest.isStump());
    siteEntriesRecord.setTreeNotes(addSiteRequest.getTreeNotes());
    siteEntriesRecord.setSiteNotes(addSiteRequest.getSiteNotes());

    siteEntriesRecord.store();
  }

  public void updateSite(JWTData userData, int siteId, UpdateSiteRequest updateSiteRequest) {
    checkSiteExists(siteId);

    SiteEntriesRecord record = db.newRecord(SITE_ENTRIES);

    int newId = db.select(max(SITE_ENTRIES.ID)).from(SITE_ENTRIES).fetchOne(0, Integer.class) + 1;

    record.setId(newId);
    record.setUserId(userData.getUserId());
    record.setSiteId(siteId);
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

    addSitesRequest
        .getSites()
        .forEach(
            newSite -> {
              addSite(userData, newSite);
            });
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
  public void editStewardship(JWTData userData, int activityId, EditStewardshipRequest editStewardshipRequest) {
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
    if (!isAlreadyAdoptedByUser(userData.getUserId(), siteId)) {
      throw new AuthException("User is not the site's adopter.");
    }

    SiteEntriesRecord siteEntry =
        db.selectFrom(SITE_ENTRIES)
            .where(SITE_ENTRIES.SITE_ID.eq(siteId))
            .orderBy(SITE_ENTRIES.UPDATED_AT)
            .limit(1)
            .fetchOne();

    if (siteEntry == null) {
      throw new LinkedResourceDoesNotExistException(
          "Site Entry", userData.getUserId(), "User", siteId, "Site");
    }

    siteEntry.setTreeName(nameSiteEntryRequest.getName());
    siteEntry.store();
  }
}
