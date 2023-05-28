package com.codeforcommunity.rest;

import com.codeforcommunity.api.IAuthProcessor;
import com.codeforcommunity.api.IImportProcessor;
import com.codeforcommunity.api.ILeaderboardProcessor;
import com.codeforcommunity.api.IMapProcessor;
import com.codeforcommunity.api.IProtectedEmailerProcessor;
import com.codeforcommunity.api.IProtectedNeighborhoodsProcessor;
import com.codeforcommunity.api.IProtectedReportProcessor;
import com.codeforcommunity.api.IProtectedSiteProcessor;
import com.codeforcommunity.api.IProtectedUserProcessor;
import com.codeforcommunity.api.IReportProcessor;
import com.codeforcommunity.api.IReservationProcessor;
import com.codeforcommunity.api.ISiteProcessor;
import com.codeforcommunity.api.ITeamsProcessor;
import com.codeforcommunity.auth.JWTAuthorizer;
import com.codeforcommunity.rest.subrouter.AuthRouter;
import com.codeforcommunity.rest.subrouter.CommonRouter;
import com.codeforcommunity.rest.subrouter.ImportRouter;
import com.codeforcommunity.rest.subrouter.LeaderboardRouter;
import com.codeforcommunity.rest.subrouter.MapRouter;
import com.codeforcommunity.rest.subrouter.ProtectedEmailerRouter;
import com.codeforcommunity.rest.subrouter.ProtectedNeighborhoodsRouter;
import com.codeforcommunity.rest.subrouter.ProtectedReportRouter;
import com.codeforcommunity.rest.subrouter.ProtectedSiteRouter;
import com.codeforcommunity.rest.subrouter.ProtectedUserRouter;
import com.codeforcommunity.rest.subrouter.ReportRouter;
import com.codeforcommunity.rest.subrouter.ReservationRouter;
import com.codeforcommunity.rest.subrouter.SiteRouter;
import com.codeforcommunity.rest.subrouter.TeamsRouter;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;

public class ApiRouter implements IRouter {
  private final CommonRouter commonRouter;
  private final AuthRouter authRouter;
  private final ProtectedUserRouter protectedUserRouter;
  private final ImportRouter importRouter;
  private final ReservationRouter reservationRouter;
  private final LeaderboardRouter leaderboardRouter;
  private final MapRouter mapRouter;
  private final TeamsRouter teamsRouter;
  private final ProtectedSiteRouter protectedSiteRouter;
  private final SiteRouter siteRouter;
  private final ProtectedReportRouter protectedReportRouter;
  private final ReportRouter reportRouter;
  private final ProtectedNeighborhoodsRouter protectedNeighborhoodsRouter;
  private final ProtectedEmailerRouter protectedEmailerRouter;

  public ApiRouter(
      IAuthProcessor authProcessor,
      IProtectedUserProcessor protectedUserProcessor,
      IImportProcessor importProcessor,
      IReservationProcessor reservationProcessor,
      ILeaderboardProcessor leaderboardProcessor,
      IMapProcessor mapProcessor,
      ITeamsProcessor teamsProcessor,
      IProtectedSiteProcessor protectedSiteProcessor,
      ISiteProcessor siteProcessor,
      IProtectedReportProcessor protectedReportProcessor,
      IReportProcessor reportProcessor,
      IProtectedNeighborhoodsProcessor protectedNeighborhoodsProcessor,
      IProtectedEmailerProcessor emailerProcessor,
      JWTAuthorizer jwtAuthorizer) {
    this.commonRouter = new CommonRouter(jwtAuthorizer);
    this.authRouter = new AuthRouter(authProcessor);
    this.protectedUserRouter = new ProtectedUserRouter(protectedUserProcessor);
    this.importRouter = new ImportRouter(importProcessor);
    this.reservationRouter = new ReservationRouter(reservationProcessor);
    this.leaderboardRouter = new LeaderboardRouter(leaderboardProcessor);
    this.mapRouter = new MapRouter(mapProcessor);
    this.teamsRouter = new TeamsRouter(teamsProcessor);
    this.protectedSiteRouter = new ProtectedSiteRouter(protectedSiteProcessor);
    this.siteRouter = new SiteRouter(siteProcessor);
    this.protectedReportRouter = new ProtectedReportRouter(protectedReportProcessor);
    this.reportRouter = new ReportRouter(reportProcessor);
    this.protectedNeighborhoodsRouter =
        new ProtectedNeighborhoodsRouter(protectedNeighborhoodsProcessor);
    this.protectedEmailerRouter = new ProtectedEmailerRouter(emailerProcessor);
  }

  /** Initialize a router and register all route handlers on it. */
  public Router initializeRouter(Vertx vertx) {
    Router router = commonRouter.initializeRouter(vertx);

    router.mountSubRouter("/user", authRouter.initializeRouter(vertx));
    router.mountSubRouter("/protected", defineProtectedRoutes(vertx));
    router.mountSubRouter("/leaderboard", leaderboardRouter.initializeRouter(vertx));
    router.mountSubRouter("/map", mapRouter.initializeRouter(vertx));
    router.mountSubRouter("/sites", siteRouter.initializeRouter(vertx));
    router.mountSubRouter("/report", reportRouter.initializeRouter(vertx));

    return router;
  }

  /**
   * Mounts all routes that require a user to be logged in. All routes defined here require a user
   * to have a valid JWT access token in their header.
   */
  private Router defineProtectedRoutes(Vertx vertx) {
    Router router = Router.router(vertx);

    router.mountSubRouter("/user", protectedUserRouter.initializeRouter(vertx));
    router.mountSubRouter("/import", importRouter.initializeRouter(vertx));
    router.mountSubRouter("/reservations", reservationRouter.initializeRouter(vertx));
    router.mountSubRouter("/teams", teamsRouter.initializeRouter(vertx));
    router.mountSubRouter("/sites", protectedSiteRouter.initializeRouter(vertx));
    router.mountSubRouter("/report", protectedReportRouter.initializeRouter(vertx));
    router.mountSubRouter("/neighborhoods", protectedNeighborhoodsRouter.initializeRouter(vertx));
    router.mountSubRouter("/emailer", protectedEmailerRouter.initializeRouter(vertx));

    return router;
  }

  public static void end(HttpServerResponse response, int statusCode) {
    end(response, statusCode, null);
  }

  public static void end(HttpServerResponse response, int statusCode, String jsonBody) {
    end(response, statusCode, jsonBody, "application/json");
  }

  public static void end(
      HttpServerResponse response, int statusCode, String jsonBody, String contentType) {
    response
        .setStatusCode(statusCode)
        .putHeader("Content-Type", contentType)
        .putHeader("Access-Control-Allow-Origin", "*")
        .putHeader("Access-Control-Allow-Methods", "DELETE, POST, GET, OPTIONS")
        .putHeader(
            "Access-Control-Allow-Headers",
            "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");
    if (jsonBody == null || jsonBody.equals("")) {
      response.end();
    } else {
      response.end(jsonBody);
    }
  }
}
