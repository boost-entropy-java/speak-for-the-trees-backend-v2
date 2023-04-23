package com.codeforcommunity.rest.subrouter;

import static com.codeforcommunity.rest.ApiRouter.end;

import com.codeforcommunity.api.IProtectedSiteProcessor;
import com.codeforcommunity.auth.JWTData;
import com.codeforcommunity.dto.site.AddSiteRequest;
import com.codeforcommunity.dto.site.AddSitesRequest;
import com.codeforcommunity.dto.site.AdoptedSitesResponse;
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
import com.codeforcommunity.rest.IRouter;
import com.codeforcommunity.rest.RestFunctions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProtectedSiteRouter implements IRouter {

  private final IProtectedSiteProcessor processor;

  public ProtectedSiteRouter(IProtectedSiteProcessor processor) {
    this.processor = processor;
  }

  @Override
  public Router initializeRouter(Vertx vertx) {
    Router router = Router.router(vertx);

    registerAdoptSite(router);
    registerParentAdoptSite(router);
    registerUnadoptSite(router);
    registerForceUnadoptSite(router);
    registerGetAdoptedSitesRoute(router);
    registerRecordStewardship(router);
    registerParentRecordStewardship(router);
    registerAddSite(router);
    registerUpdateSite(router);
    registerDeleteSite(router);
    registerEditSite(router);
    registerAddSites(router);
    registerDeleteStewardship(router);
    registerEditStewardship(router);
    registerNameSiteEntry(router);
    registerUploadSiteImage(router);
    registerDeleteSiteImage(router);
    registerFilterSites(router);
    registerEditSiteEntry(router);

    return router;
  }

  private void registerAdoptSite(Router router) {
    Route adoptSiteRoute = router.post("/:site_id/adopt");
    adoptSiteRoute.handler(this::handleAdoptSiteRoute);
  }

  private void handleAdoptSiteRoute(RoutingContext ctx) {
    JWTData userData = ctx.get("jwt_data");
    int siteId = RestFunctions.getRequestParameterAsInt(ctx.request(), "site_id");

    processor.adoptSite(userData, siteId, Date.valueOf(LocalDate.now()));

    end(ctx.response(), 200);
  }

  private void registerParentAdoptSite(Router router) {
    Route parentAdoptSiteRoute = router.post("/:site_id/parent_adopt");
    parentAdoptSiteRoute.handler(this::handleParentAdoptSiteRoute);
  }

  private void handleParentAdoptSiteRoute(RoutingContext ctx) {
    JWTData parentUserData = ctx.get("jwt_data");
    int siteId = RestFunctions.getRequestParameterAsInt(ctx.request(), "site_id");

    ParentAdoptSiteRequest parentAdoptSiteRequest =
        RestFunctions.getJsonBodyAsClass(ctx, ParentAdoptSiteRequest.class);

    processor.parentAdoptSite(
        parentUserData, siteId, parentAdoptSiteRequest, Date.valueOf(LocalDate.now()));

    end(ctx.response(), 200);
  }

  private void registerUnadoptSite(Router router) {
    Route unadoptSiteRoute = router.post("/:site_id/unadopt");
    unadoptSiteRoute.handler(this::handleUnadoptSiteRoute);
  }

  private void handleUnadoptSiteRoute(RoutingContext ctx) {
    JWTData userData = ctx.get("jwt_data");
    int siteId = RestFunctions.getRequestParameterAsInt(ctx.request(), "site_id");

    processor.unadoptSite(userData, siteId);

    end(ctx.response(), 200);
  }

  private void registerForceUnadoptSite(Router router) {
    Route forceUnadoptSiteRoute = router.post("/:site_id/force_unadopt");
    forceUnadoptSiteRoute.handler(this::handleForceUnadoptSiteRoute);
  }

  private void handleForceUnadoptSiteRoute(RoutingContext ctx) {
    JWTData userData = ctx.get("jwt_data");
    int siteId = RestFunctions.getRequestParameterAsInt(ctx.request(), "site_id");

    processor.forceUnadoptSite(userData, siteId);

    end(ctx.response(), 200);
  }

  private void registerGetAdoptedSitesRoute(Router router) {
    Route getAdoptedSitesRoute = router.get("/adopted_sites");
    getAdoptedSitesRoute.handler(this::handleGetAdoptedSitesRoute);
  }

  private void handleGetAdoptedSitesRoute(RoutingContext ctx) {
    JWTData userData = ctx.get("jwt_data");

    AdoptedSitesResponse adoptedSitesResponse = processor.getAdoptedSites(userData);

    end(ctx.response(), 200, JsonObject.mapFrom(adoptedSitesResponse).toString());
  }

  private void registerRecordStewardship(Router router) {
    Route recordStewardshipRoute = router.post("/:site_id/record_stewardship");
    recordStewardshipRoute.handler(this::handleRecordStewardshipRoute);
  }

  private void handleRecordStewardshipRoute(RoutingContext ctx) {
    JWTData userData = ctx.get("jwt_data");
    int siteId = RestFunctions.getRequestParameterAsInt(ctx.request(), "site_id");

    RecordStewardshipRequest recordStewardshipRequest =
        RestFunctions.getJsonBodyAsClass(ctx, RecordStewardshipRequest.class);

    processor.recordStewardship(userData, siteId, recordStewardshipRequest);

    end(ctx.response(), 200);
  }

  private void registerParentRecordStewardship(Router router) {
    Route recordParentStewardshipRoute = router.post("/:site_id/parent_record_stewardship");
    recordParentStewardshipRoute.handler(this::handleParentRecordStewardshipRoute);
  }

  private void handleParentRecordStewardshipRoute(RoutingContext ctx) {
    JWTData userData = ctx.get("jwt_data");
    int siteId = RestFunctions.getRequestParameterAsInt(ctx.request(), "site_id");

    ParentRecordStewardshipRequest parentRecordStewardshipRequest =
        RestFunctions.getJsonBodyAsClass(ctx, ParentRecordStewardshipRequest.class);

    processor.parentRecordStewardship(userData, siteId, parentRecordStewardshipRequest);

    end(ctx.response(), 200);
  }

  private void registerUpdateSite(Router router) {
    Route updateSiteRoute = router.post("/:site_id/update");
    updateSiteRoute.handler(this::handleUpdateSiteRoute);
  }

  private void handleUpdateSiteRoute(RoutingContext ctx) {
    JWTData userData = ctx.get("jwt_data");
    int siteId = RestFunctions.getRequestParameterAsInt(ctx.request(), "site_id");

    UpdateSiteRequest updateSiteRequest =
        RestFunctions.getJsonBodyAsClass(ctx, UpdateSiteRequest.class);

    processor.updateSite(userData, siteId, updateSiteRequest);

    end(ctx.response(), 200);
  }

  private void registerDeleteStewardship(Router router) {
    Route deleteStewardshipRoute = router.post("/delete_stewardship/:activity_id");
    deleteStewardshipRoute.handler(this::handleDeleteStewardshipRoute);
  }

  private void handleDeleteStewardshipRoute(RoutingContext ctx) {
    JWTData userData = ctx.get("jwt_data");
    int activityId = RestFunctions.getRequestParameterAsInt(ctx.request(), "activity_id");

    processor.deleteStewardship(userData, activityId);

    end(ctx.response(), 200);
  }

  private void registerEditStewardship(Router router) {
    Route editStewardshipRoute = router.post("/edit_stewardship/:activity_id");
    editStewardshipRoute.handler(this::handleEditStewardshipRoute);
  }

  private void handleEditStewardshipRoute(RoutingContext ctx) {
    JWTData userData = ctx.get("jwt_data");
    int activityId = RestFunctions.getRequestParameterAsInt(ctx.request(), "activity_id");
    EditStewardshipRequest editStewardshipRequest =
        RestFunctions.getJsonBodyAsClass(ctx, EditStewardshipRequest.class);

    processor.editStewardship(userData, activityId, editStewardshipRequest);

    end(ctx.response(), 200);
  }

  private void registerAddSite(Router router) {
    Route addSiteRoute = router.post("/add");
    addSiteRoute.handler(this::handleAddSiteRoute);
  }

  private void handleAddSiteRoute(RoutingContext ctx) {
    JWTData userData = ctx.get("jwt_data");

    AddSiteRequest addSiteRequest = RestFunctions.getJsonBodyAsClass(ctx, AddSiteRequest.class);

    processor.addSite(userData, addSiteRequest);

    end(ctx.response(), 200);
  }

  private void registerAddSites(Router router) {
    Route addSiteRoute = router.post("/add_sites");
    addSiteRoute.handler(this::handleAddSitesRoute);
  }

  private void handleAddSitesRoute(RoutingContext ctx) {
    JWTData userData = ctx.get("jwt_data");

    AddSitesRequest addSitesRequest = RestFunctions.getJsonBodyAsClass(ctx, AddSitesRequest.class);

    processor.addSites(userData, addSitesRequest);

    end(ctx.response(), 200);
  }

  private void registerDeleteSite(Router router) {
    Route deleteSiteRoute = router.post("/:site_id/delete");
    deleteSiteRoute.handler(this::handleDeleteSiteRoute);
  }

  private void handleDeleteSiteRoute(RoutingContext ctx) {
    JWTData userData = ctx.get("jwt_data");
    int siteId = RestFunctions.getRequestParameterAsInt(ctx.request(), "site_id");

    processor.deleteSite(userData, siteId);

    end(ctx.response(), 200);
  }

  private void registerEditSite(Router router) {
    Route editSiteRoute = router.post("/:site_id/edit");
    editSiteRoute.handler(this::handleEditSiteRoute);
  }

  private void handleEditSiteRoute(RoutingContext ctx) {
    JWTData userData = ctx.get("jwt_data");
    int siteId = RestFunctions.getRequestParameterAsInt(ctx.request(), "site_id");

    EditSiteRequest editSiteRequest = RestFunctions.getJsonBodyAsClass(ctx, EditSiteRequest.class);

    processor.editSite(userData, siteId, editSiteRequest);

    end(ctx.response(), 200);
  }

  private void registerNameSiteEntry(Router router) {
    Route nameSiteEntry = router.post("/:site_id/name_entry");
    nameSiteEntry.handler(this::handleNameSiteEntry);
  }

  private void handleNameSiteEntry(RoutingContext ctx) {
    JWTData userData = ctx.get("jwt_data");
    int siteId = RestFunctions.getRequestParameterAsInt(ctx.request(), "site_id");

    NameSiteEntryRequest nameSiteEntryRequest =
        RestFunctions.getJsonBodyAsClass(ctx, NameSiteEntryRequest.class);

    processor.nameSiteEntry(userData, siteId, nameSiteEntryRequest);

    end(ctx.response(), 200);
  }

  private void registerUploadSiteImage(Router router) {
    Route uploadImage = router.post("/:site_id/upload_image");
    uploadImage.handler(this::handleUploadSiteImage);
  }

  private void handleUploadSiteImage(RoutingContext ctx) {
    JWTData userData = ctx.get("jwt_data");
    int siteId = RestFunctions.getRequestParameterAsInt(ctx.request(), "site_id");

    UploadSiteImageRequest uploadSiteImageRequest =
        RestFunctions.getJsonBodyAsClass(ctx, UploadSiteImageRequest.class);

    processor.uploadSiteImage(userData, siteId, uploadSiteImageRequest);

    end(ctx.response(), 200);
  }

  private void registerDeleteSiteImage(Router router) {
    Route deleteImage = router.post("/delete_image/:image_id");
    deleteImage.handler(this::handleDeleteSiteImage);
  }

  private void handleDeleteSiteImage(RoutingContext ctx) {
    JWTData userData = ctx.get("jwt_data");
    int imageId = RestFunctions.getRequestParameterAsInt(ctx.request(), "image_id");

    processor.deleteSiteImage(userData, imageId);

    end(ctx.response(), 200);
  }

  private void registerFilterSites(Router router) {
    Route filterSites = router.get("/filter_sites");
    filterSites.handler(this::handleFilterSites);
  }

  private void handleFilterSites(RoutingContext ctx) {
    JWTData userData = ctx.get("jwt_data");

    Optional<List<String>> treeCommonNames =
        RestFunctions.getOptionalQueryParam(ctx, "treeCommonNames", (string) -> Arrays.stream(string.split(",")).collect(Collectors.toList()));
    Optional<Date> adoptedStart =
        RestFunctions.getOptionalQueryParam(ctx, "adoptedStart", Date::valueOf);
    Optional<Date> adoptedEnd =
        RestFunctions.getOptionalQueryParam(ctx, "adoptedEnd", Date::valueOf);
    Optional<Date> lastActivityStart =
        RestFunctions.getOptionalQueryParam(ctx, "lastActivityStart", Date::valueOf);
    Optional<Date> lastActivityEnd =
        RestFunctions.getOptionalQueryParam(ctx, "lastActivityEnd", Date::valueOf);
    Optional<List<Integer>> neighborhoodIds =
        RestFunctions.getOptionalQueryParam(ctx, "neighborhoodIds", (string) -> Arrays.stream(string.split(",")).map(Integer::parseInt).collect(Collectors.toList()));
    Optional<Integer> activityCountMin =
        RestFunctions.getOptionalQueryParam(ctx, "activityCountMin", Integer::parseInt);
    Optional<Integer> activityCountMax =
        RestFunctions.getOptionalQueryParam(ctx, "activityCountMax", Integer::parseInt);

    FilterSitesRequest filterSitesRequest = new FilterSitesRequest(
        treeCommonNames.orElse(null),
        adoptedStart.orElse(null),
        adoptedEnd.orElse(null),
        lastActivityStart.orElse(null),
        lastActivityEnd.orElse(null),
        neighborhoodIds.orElse(null),
        activityCountMin.orElse(null),
        activityCountMax.orElse(null)
    );

    List<FilterSitesResponse> filterSitesResponse =
        processor.filterSites(userData, filterSitesRequest);

    end(
        ctx.response(),
        200,
        JsonObject.mapFrom(Collections.singletonMap("filteredSites", filterSitesResponse))
            .toString());
  }

  private void registerEditSiteEntry(Router router) {
    Route editSiteEntry = router.post("/edit_entry/:entry_id");
    editSiteEntry.handler(this::handleEditSiteEntry);
  }

  private void handleEditSiteEntry(RoutingContext ctx) {
    JWTData userData = ctx.get("jwt_data");
    int entryId = RestFunctions.getRequestParameterAsInt(ctx.request(), "entry_id");

    UpdateSiteRequest editSiteEntryRequest =
        RestFunctions.getJsonBodyAsClass(ctx, UpdateSiteRequest.class);

    processor.editSiteEntry(userData, entryId, editSiteEntryRequest);

    end(ctx.response(), 200);
  }
}
