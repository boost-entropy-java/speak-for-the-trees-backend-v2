package com.codeforcommunity.processor;

import static org.jooq.generated.Tables.ADOPTED_SITES;
import static org.jooq.generated.Tables.ENTRY_USERNAMES;
import static org.jooq.generated.Tables.SITES;
import static org.jooq.generated.Tables.SITE_ENTRIES;
import static org.jooq.generated.Tables.STEWARDSHIP;
import static org.jooq.generated.Tables.USERS;

import com.codeforcommunity.api.ISiteProcessor;
import com.codeforcommunity.dto.site.GetSiteResponse;
import com.codeforcommunity.dto.site.SiteEntry;
import com.codeforcommunity.dto.site.StewardshipActivitiesResponse;
import com.codeforcommunity.dto.site.StewardshipActivity;
import com.codeforcommunity.exceptions.ResourceDoesNotExistException;
import com.codeforcommunity.logger.SLogger;

import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.generated.tables.records.AdoptedSitesRecord;
import org.jooq.generated.tables.records.SiteEntriesRecord;
import org.jooq.generated.tables.records.SitesRecord;
import org.jooq.generated.tables.records.StewardshipRecord;

public class SiteProcessorImpl implements ISiteProcessor {

  private final SLogger logger = new SLogger(SiteProcessorImpl.class);

  private final DSLContext db;

  public SiteProcessorImpl(DSLContext db) {
    this.db = db;
  }

  private void checkSiteExists(int siteId) {
    if (!db.fetchExists(db.selectFrom(SITES).where(SITES.ID.eq(siteId)))) {
      throw new ResourceDoesNotExistException(siteId, "Site");
    }
  }

  private List<SiteEntry> getSiteEntries(int siteId) {
    List<SiteEntriesRecord> records =
        db.selectFrom(SITE_ENTRIES)
            .where(SITE_ENTRIES.SITE_ID.eq(siteId))
            .orderBy(SITE_ENTRIES.UPDATED_AT.desc())
            .fetch();

    List<SiteEntry> siteEntries = new ArrayList<>();

    records.forEach(
        record -> {
          String username;
          if (record.getUserId() == null) {
            username =
                db.selectFrom(ENTRY_USERNAMES)
                    .where(ENTRY_USERNAMES.ENTRY_ID.eq(record.getId()))
                    .fetchOne(ENTRY_USERNAMES.USERNAME);
          } else {
            username =
                db.selectFrom(USERS)
                    .where(USERS.ID.eq(record.getUserId()))
                    .fetchOne(USERS.USERNAME);
          }

          // Finds if the site is adopted, and if it is returns the username of the adopter
          String adopter;
          AdoptedSitesRecord adoptedSitesRecord =
              db.selectFrom(ADOPTED_SITES).where(ADOPTED_SITES.SITE_ID.eq(siteId)).fetchOne();
          if (adoptedSitesRecord == null) {
            adopter = null;
          } else {
            adopter =
                db.select(USERS.USERNAME)
                    .from(USERS)
                    .where(USERS.ID.eq(adoptedSitesRecord.getUserId()))
                    .fetchOne()
                    .value1();
          }

          SiteEntry siteEntry =
              new SiteEntry(
                  record.getId(),
                  username,
                  record.getUpdatedAt(),
                  record.getTreePresent(),
                  record.getStatus(),
                  record.getGenus(),
                  record.getSpecies(),
                  record.getCommonName(),
                  record.getConfidence(),
                  record.getDiameter(),
                  record.getCircumference(),
                  record.getMultistem(),
                  record.getCoverage(),
                  record.getPruning(),
                  record.getCondition(),
                  record.getDiscoloring(),
                  record.getLeaning(),
                  record.getConstrictingGrate(),
                  record.getWounds(),
                  record.getPooling(),
                  record.getStakesWithWires(),
                  record.getStakesWithoutWires(),
                  record.getLight(),
                  record.getBicycle(),
                  record.getBagEmpty(),
                  record.getBagFilled(),
                  record.getTape(),
                  record.getSuckerGrowth(),
                  record.getSiteType(),
                  record.getSidewalkWidth(),
                  record.getSiteWidth(),
                  record.getSiteLength(),
                  record.getMaterial(),
                  record.getRaisedBed(),
                  record.getFence(),
                  record.getTrash(),
                  record.getWires(),
                  record.getGrate(),
                  record.getStump(),
                  record.getTreeNotes(),
                  record.getSiteNotes(),
                  record.getTreeName(),
                  adopter,
                  record.getPlantingDate(),

                  /* Cambridge fields */
                  record.getTrunks(),
                  record.getSpeciesShort(),
                  record.getLocation(),
                  record.getSiteRetiredReason(),
                  record.getInspectr(),
                  record.getAbutsOpenArea(),
                  record.getTreeWellCover(),
                  record.getTreeGrateActionReq(),
                  record.getGlobalId(),
                  record.getPb(),
                  record.getSiteReplanted(),
                  record.getOverheadWires(),
                  record.getOwnership(),
                  record.getScheduledRemoval(),
                  record.getStructuralSoil(),
                  record.getWateringResponsibility(),
                  record.getCultivar(),
                  record.getSolarRating(),
                  record.getBareRoot(),
                  record.getAdaCompliant(),
                  record.getCartegraphPlantDate(),
                  record.getLocationRetired(),
                  record.getCreatedDate(),
                  record.getOrder(),
                  record.getPlantingSeason(),
                  record.getExposedRootFlare(),
                  record.getStTreePruningZone(),
                  record.getMemTree(),
                  record.getCartegraphRetireDate(),
                  record.getRemovalReason(),
                  record.getOffStTreePruningZone(),
                  record.getPlantingContract(),
                  record.getTreeWellDepth(),
                  record.getRemovalDate(),
                  record.getScientificName(),
                  record.getBiocharAdded(),
                  record.getLastEditedUser());

          siteEntries.add(siteEntry);
        });

    return siteEntries;
  }

  @Override
  public GetSiteResponse getSite(int siteId) {
    SitesRecord sitesRecord = db.selectFrom(SITES).where(SITES.ID.eq(siteId)).fetchOne();

    if (sitesRecord == null) {
      throw new ResourceDoesNotExistException(siteId, "site");
    }

    return new GetSiteResponse(
        sitesRecord.getId(),
        sitesRecord.getBlockId(),
        sitesRecord.getLat(),
        sitesRecord.getLng(),
        sitesRecord.getCity(),
        sitesRecord.getZip(),
        sitesRecord.getAddress(),
        sitesRecord.getNeighborhoodId(),
        getSiteEntries(siteId));
  }

  @Override
  public StewardshipActivitiesResponse getStewardshipActivities(int siteId) {
    List<StewardshipRecord> records =
        db.selectFrom(STEWARDSHIP).where(STEWARDSHIP.SITE_ID.eq(siteId)).fetch();

    checkSiteExists(siteId);

    List<StewardshipActivity> activities = new ArrayList<>();

    records.forEach(
        record -> {
          logger.info("Stewardship activity recorded on: " + record.getPerformedOn());

          StewardshipActivity stewardshipActivity =
              new StewardshipActivity(
                  record.getId(),
                  record.getUserId(),
                  record.getPerformedOn(),
                  record.getWatered(),
                  record.getMulched(),
                  record.getCleaned(),
                  record.getWeeded());
          activities.add(stewardshipActivity);

          logger.info("Stewardship recorded on: " + stewardshipActivity.getDate());
        });

    return new StewardshipActivitiesResponse(activities);
  }

  @Override
  public List<String> getAllCommonNames() {
    return db
        .selectDistinct(SITE_ENTRIES.COMMON_NAME)
        .from(SITE_ENTRIES)
        .where(SITE_ENTRIES.COMMON_NAME.isNotNull())
        .and(SITE_ENTRIES.COMMON_NAME.notEqual(""))
        .orderBy(SITE_ENTRIES.COMMON_NAME.asc()).fetchInto(String.class);
  }
}
