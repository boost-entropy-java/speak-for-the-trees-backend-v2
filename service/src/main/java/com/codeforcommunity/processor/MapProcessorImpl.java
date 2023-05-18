package com.codeforcommunity.processor;

import static org.jooq.generated.tables.AdoptedSites.ADOPTED_SITES;
import static org.jooq.generated.tables.Blocks.BLOCKS;
import static org.jooq.generated.tables.Neighborhoods.NEIGHBORHOODS;
import static org.jooq.generated.tables.Reservations.RESERVATIONS;
import static org.jooq.generated.tables.SiteEntries.SITE_ENTRIES;
import static org.jooq.generated.tables.Sites.SITES;
import static org.jooq.impl.DSL.concat;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.when;

import com.codeforcommunity.api.IMapProcessor;
import com.codeforcommunity.dto.map.BlockFeature;
import com.codeforcommunity.dto.map.BlockFeatureProperties;
import com.codeforcommunity.dto.map.BlockGeoResponse;
import com.codeforcommunity.dto.map.GeometryPoint;
import com.codeforcommunity.dto.map.NeighborhoodFeature;
import com.codeforcommunity.dto.map.NeighborhoodFeatureProperties;
import com.codeforcommunity.dto.map.NeighborhoodGeoResponse;
import com.codeforcommunity.dto.map.SiteFeature;
import com.codeforcommunity.dto.map.SiteFeatureProperties;
import com.codeforcommunity.dto.map.SiteGeoResponse;
import com.codeforcommunity.enums.ReservationAction;
import com.codeforcommunity.logger.SLogger;
import io.vertx.core.json.JsonObject;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record2;
import org.jooq.Record6;
import org.jooq.Record9;
import org.jooq.Result;
import org.jooq.Select;
import org.jooq.Table;
import org.jooq.generated.tables.records.BlocksRecord;
import org.jooq.generated.tables.records.NeighborhoodsRecord;

public class MapProcessorImpl implements IMapProcessor {

  private final SLogger logger = new SLogger(MapProcessorImpl.class);
  private final DSLContext db;

  public MapProcessorImpl(DSLContext db) {
    this.db = db;
  }

  /** Create a corresponding BlockFeature from a BlocksRecord. */
  private BlockFeature blockFeatureFromRecord(BlocksRecord blocksRecord) {
    BlockFeatureProperties properties =
        new BlockFeatureProperties(
            blocksRecord.getId(), blocksRecord.getLat(), blocksRecord.getLng());
    try {
      JsonObject geometry = new JsonObject(blocksRecord.getGeometry());
      return new BlockFeature(properties, geometry);
    } catch (Exception e) {
      String errorMessage =
          String.format(
              "Exception thrown while processing conversion of geometry to JSON for block id [%d]",
              blocksRecord.getId());
      logger.error(errorMessage, e);
      throw e;
    }
  }

  /**
   * Given a neighborhoodId, return the percent of blocks that have been completed or are in QA in
   * it as an integer between 0 and 100.
   */
  private Integer getNeighborhoodCompletionPercentage(int neighborhoodId) {
    // This counts the number of blocks that are in the given neighborhood
    int totalNeighborhoodBlocks =
        db.select(count())
            .from(BLOCKS)
            .where(BLOCKS.NEIGHBORHOOD_ID.eq(neighborhoodId))
            .fetchOne(0, Integer.class);

    // This joins each block with their most recent reservations table entry
    Select<Record2<Integer, ReservationAction>> subquery =
        db.select(BLOCKS.ID, RESERVATIONS.ACTION_TYPE)
            .distinctOn(BLOCKS.ID)
            .from(BLOCKS)
            .join(RESERVATIONS)
            .onKey()
            .where(BLOCKS.NEIGHBORHOOD_ID.eq(neighborhoodId))
            .orderBy(BLOCKS.ID, RESERVATIONS.PERFORMED_AT.desc());

    // This counts the number of rows in the above query that have their most recent reservation
    // action as complete or qa
    int completedNeighborhoodBlocks =
        db.select(count())
            .from(subquery)
            .where(
                subquery
                    .field(1, ReservationAction.class)
                    .in(ReservationAction.COMPLETE, ReservationAction.QA))
            .fetchOne(0, Integer.class);

    double completionPercent = (double) completedNeighborhoodBlocks / totalNeighborhoodBlocks;
    return (int) Math.floor(completionPercent * 100);
  }

  /** Create a corresponding NeighborhoodFeature for a given neighborhoodsRecord */
  private NeighborhoodFeature neighborhoodFeatureFromRecord(
      NeighborhoodsRecord neighborhoodsRecord) {
    Integer neighborhoodCompletionPercentage =
        getNeighborhoodCompletionPercentage(neighborhoodsRecord.getId());
    NeighborhoodFeatureProperties properties =
        new NeighborhoodFeatureProperties(
            neighborhoodsRecord.getId(),
            neighborhoodsRecord.getNeighborhoodName(),
            neighborhoodCompletionPercentage,
            neighborhoodsRecord.getCanopyCoverage(),
            neighborhoodsRecord.getLat(),
            neighborhoodsRecord.getLng());
    try {
      JsonObject geometry = new JsonObject(neighborhoodsRecord.getGeometry());
      return new NeighborhoodFeature(properties, geometry);
    } catch (Exception e) {
      String errorMessage =
          String.format(
              "Exception thrown while processing conversion of geometry to JSON for neighborhood id [%d]",
              neighborhoodsRecord.getId());
      logger.error(errorMessage, e);
      throw e;
    }
  }

  private SiteFeature siteFeatureFromRecord(
      Record9<
              Integer, // #1 Site ID
              Boolean, // #2 Tree Present
              String, // #3 Common Name
              Date, // #4 Planting Date
              Integer, // #5 Adopter User ID
              String, // #6 Address
              String, // #7 Owner
              BigDecimal, // #8 Lat
              BigDecimal> // #9 Lng
          sitesRecord) {
    SiteFeatureProperties properties =
        new SiteFeatureProperties(
            sitesRecord.value1(),
            sitesRecord.value2(),
            sitesRecord.value3(),
            sitesRecord.value4(),
            sitesRecord.value5(),
            sitesRecord.value6(),
            sitesRecord.value7());
    GeometryPoint geometry = new GeometryPoint(sitesRecord.value8(), sitesRecord.value9());
    return new SiteFeature(properties, geometry);
  }

  @Override
  public BlockGeoResponse getBlockGeoJson() {
    List<BlockFeature> features =
        this.db.selectFrom(BLOCKS).stream()
            .map(this::blockFeatureFromRecord)
            .collect(Collectors.toList());
    return new BlockGeoResponse(features);
  }

  @Override
  public NeighborhoodGeoResponse getNeighborhoodGeoJson() {
    List<NeighborhoodFeature> features =
        this.db.selectFrom(NEIGHBORHOODS).stream()
            .map(this::neighborhoodFeatureFromRecord)
            .collect(Collectors.toList());
    return new NeighborhoodGeoResponse(features);
  }

  @Override
  public SiteGeoResponse getSiteGeoJson() {
    // Since this SQL query takes a long time, we cache the result
    if (!SiteGeoResponseCache.isExpired()) {
      return SiteGeoResponseCache.getResponse();
    }

    Field<Timestamp> maxDate = max(SITE_ENTRIES.UPDATED_AT).as("maxDate");

    Table<
            Record2<
                Integer, // Site Entry ID
                Timestamp // Updated At
            >>
        recentlyUpdated =
            table(
                    this.db
                        .select(SITE_ENTRIES.SITE_ID, maxDate)
                        .from(SITE_ENTRIES)
                        .groupBy(SITE_ENTRIES.SITE_ID))
                .as("recentlyUpdated");

    Table<
            Record6<
                Integer, // Site ID
                Boolean, // Tree Present
                String, // Common Name
                String, // Genus
                String, // Species
                Date // Planting Date
            >>
        newEntries =
            table(
                    this.db
                        .select(
                            SITE_ENTRIES.SITE_ID,
                            SITE_ENTRIES.TREE_PRESENT,
                            SITE_ENTRIES.COMMON_NAME,
                            SITE_ENTRIES.GENUS,
                            SITE_ENTRIES.SPECIES,
                            SITE_ENTRIES.PLANTING_DATE)
                        .from(SITE_ENTRIES)
                        .join(recentlyUpdated)
                        .on(SITE_ENTRIES.SITE_ID.eq(recentlyUpdated.field(SITE_ENTRIES.SITE_ID)))
                        .and(SITE_ENTRIES.UPDATED_AT.eq(recentlyUpdated.field(maxDate))))
                .as("newEntries");

    Field<String> treeName =
        when( // common name is empty
                newEntries.field(SITE_ENTRIES.COMMON_NAME).eq(""),
                when( // all 3 are empty
                        newEntries
                            .field(SITE_ENTRIES.GENUS)
                            .eq("")
                            .and(newEntries.field(SITE_ENTRIES.SPECIES).eq("")),
                        "Unknown Species")
                    .otherwise(
                        when( // common name and genus are empty
                                newEntries.field(SITE_ENTRIES.GENUS).eq(""),
                                newEntries.field(SITE_ENTRIES.SPECIES))
                            .otherwise( // common name and species are empty
                                when(
                                        newEntries.field(SITE_ENTRIES.SPECIES).eq(""),
                                        newEntries.field(SITE_ENTRIES.GENUS))
                                    .otherwise( // just common name is empty
                                        concat(
                                            newEntries.field(SITE_ENTRIES.GENUS).concat(" "),
                                            newEntries.field(SITE_ENTRIES.SPECIES))))))
            .otherwise(newEntries.field(SITE_ENTRIES.COMMON_NAME)); // common name is not empty

    Result<
            Record9<
                Integer, // Site ID
                Boolean, // Tree Present
                String, // Name
                Date, // Planting Date
                Integer, // Adopter User ID
                String, // Address
                String, // Owner
                BigDecimal, // Lat
                BigDecimal>> // Lng
        userRecords =
            this.db
                .select(
                    SITES.ID,
                    newEntries.field(SITE_ENTRIES.TREE_PRESENT),
                    treeName,
                    newEntries.field(SITE_ENTRIES.PLANTING_DATE),
                    ADOPTED_SITES.USER_ID,
                    SITES.ADDRESS,
                    SITES.OWNER,
                    SITES.LAT,
                    SITES.LNG)
                .from(SITES)
                .leftJoin(newEntries)
                .on(SITES.ID.eq(newEntries.field(SITE_ENTRIES.SITE_ID)))
                .leftJoin(ADOPTED_SITES)
                .on(ADOPTED_SITES.SITE_ID.eq(SITES.ID))
                .orderBy(SITES.ID)
                .fetch();

    List<SiteFeature> features =
        userRecords.stream().map(this::siteFeatureFromRecord).collect(Collectors.toList());

    SiteGeoResponse response = new SiteGeoResponse(features);
    SiteGeoResponseCache.setResponse(response);
    return response;
  }

  /**
   * SiteGeoResponseCache is a basic in-memory cache to remember previous SiteGeoResponse results,
   * and only recompute values when the data in the cache is expired. This was implemented because
   * getSiteGeoJson() takes too long, (an average of 5000ms as of writing) to perform and load
   * testing revealed the Vert.x event loop getting overloaded.
   */
  private static class SiteGeoResponseCache {
    private static SiteGeoResponse response;
    private static long expireTime = 0;
    private static final long timeToLive = 10000; // in milliseconds, 5000 is the avg. response time

    public static SiteGeoResponse getResponse() {
      return response;
    }

    public static void setResponse(SiteGeoResponse newResponse) {
      response = newResponse;
      expireTime = System.currentTimeMillis() + timeToLive;
    }

    public static boolean isExpired() {
      return System.currentTimeMillis() >= expireTime;
    }
  }
}
