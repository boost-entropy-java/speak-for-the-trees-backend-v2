package com.codeforcommunity.rest.subrouter;

import static com.codeforcommunity.rest.ApiRouter.end;

import com.codeforcommunity.api.IProtectedReportProcessor;
import com.codeforcommunity.auth.JWTData;
import com.codeforcommunity.dto.report.GetAdoptionReportResponse;
import com.codeforcommunity.dto.report.GetReportCSVRequest;
import com.codeforcommunity.dto.report.GetSiteActivityReportCSVRequest;
import com.codeforcommunity.dto.report.GetStewardshipReportResponse;
import com.codeforcommunity.rest.IRouter;
import com.codeforcommunity.rest.RestFunctions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import java.time.LocalDate;
import java.util.Optional;

public class ProtectedReportRouter implements IRouter {
  private static final String PREVIOUS_DAYS_QUERY_PARAM_NAME = "previousDays";
  private static final String SITE_ID_QUERY_PARAM_NAME = "siteId";

  private final IProtectedReportProcessor processor;

  public ProtectedReportRouter(IProtectedReportProcessor processor) {
    this.processor = processor;
  }

  @Override
  public Router initializeRouter(Vertx vertx) {
    Router router = Router.router(vertx);

    registerGetAdoptionReport(router);
    registerGetAdoptionReportCSV(router);
    registerGetStewardshipReport(router);
    registerGetStewardshipReportCSV(router);
    registerGetSiteActivityReportCSV(router);

    return router;
  }

  private void registerGetAdoptionReport(Router router) {
    Route getAdoptionReportRoute = router.get("/adoption");
    getAdoptionReportRoute.handler(this::handleGetAdoptionReportRoute);
  }

  private void handleGetAdoptionReportRoute(RoutingContext ctx) {
    JWTData userData = ctx.get("jwt_data");

    GetAdoptionReportResponse adoptionReportResponse = processor.getAdoptionReport(userData);

    end(ctx.response(), 200, JsonObject.mapFrom(adoptionReportResponse).toString());
  }

  private void registerGetAdoptionReportCSV(Router router) {
    Route getAdoptionReportCSVRoute = router.get("/csv/adoption");
    getAdoptionReportCSVRoute.handler(this::handleGetAdoptionReportCSVRoute);
  }

  private void handleGetAdoptionReportCSVRoute(RoutingContext ctx) {
    JWTData userData = ctx.get("jwt_data");

    Optional<Long> maybePreviousDays =
        RestFunctions.getOptionalQueryParam(ctx, PREVIOUS_DAYS_QUERY_PARAM_NAME, Long::parseLong);
    Long previousDays = maybePreviousDays.orElse(LocalDate.now().toEpochDay());

    GetReportCSVRequest getAdoptionReportCSVRequest = new GetReportCSVRequest(previousDays);

    String adoptionReportCSVResponse =
        processor.getAdoptionReportCSV(userData, getAdoptionReportCSVRequest);

    end(ctx.response(), 200, adoptionReportCSVResponse);
  }

  private void registerGetStewardshipReport(Router router) {
    Route getStewardshipReportRoute = router.get("/stewardship");
    getStewardshipReportRoute.handler(this::handleGetStewardshipReportRoute);
  }

  private void handleGetStewardshipReportRoute(RoutingContext ctx) {
    JWTData userData = ctx.get("jwt_data");

    GetStewardshipReportResponse stewardshipReportResponse =
        processor.getStewardshipReport(userData);

    end(ctx.response(), 200, JsonObject.mapFrom(stewardshipReportResponse).toString());
  }

  private void registerGetStewardshipReportCSV(Router router) {
    Route getStewardshipReportCSVRoute = router.get("/csv/stewardship");
    getStewardshipReportCSVRoute.handler(this::handleGetStewardshipReportCSVRoute);
  }

  private void handleGetStewardshipReportCSVRoute(RoutingContext ctx) {
    JWTData userData = ctx.get("jwt_data");

    Optional<Long> maybePreviousDays =
        RestFunctions.getOptionalQueryParam(ctx, PREVIOUS_DAYS_QUERY_PARAM_NAME, Long::parseLong);
    Long previousDays = maybePreviousDays.orElse(LocalDate.now().toEpochDay());

    GetReportCSVRequest getStewardshipReportCSVRequest = new GetReportCSVRequest(previousDays);

    String stewardshipReportCSVResponse =
        processor.getStewardshipReportCSV(userData, getStewardshipReportCSVRequest);

    end(ctx.response(), 200, stewardshipReportCSVResponse);
  }

  private void registerGetSiteActivityReportCSV(Router router) {
    Route getSiteActivityReportCSVRoute = router.get("/csv/site-activity");
    getSiteActivityReportCSVRoute.handler(this::handleGetSiteActivityReportCSVRoute);
  }

  private void handleGetSiteActivityReportCSVRoute(RoutingContext ctx) {
    JWTData userData = ctx.get("jwt_data");

    Optional<Long> maybePreviousDays =
        RestFunctions.getOptionalQueryParam(ctx, PREVIOUS_DAYS_QUERY_PARAM_NAME, Long::parseLong);
    Optional<Integer> maybeSiteId =
        RestFunctions.getOptionalQueryParam(ctx, SITE_ID_QUERY_PARAM_NAME, Integer::parseInt);

    GetSiteActivityReportCSVRequest request =
        new GetSiteActivityReportCSVRequest(
            maybePreviousDays.orElse(null), maybeSiteId.orElse(null));

    String csv = processor.getSiteActivityReportCSV(userData, request);

    end(ctx.response(), 200, csv);
  }
}
