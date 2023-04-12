package com.codeforcommunity.rest.subrouter;

import static com.codeforcommunity.rest.ApiRouter.end;

import com.codeforcommunity.api.IProtectedNeighborhoodsProcessor;
import com.codeforcommunity.auth.JWTData;
import com.codeforcommunity.dto.neighborhoods.EditCanopyCoverageRequest;
import com.codeforcommunity.dto.neighborhoods.SendEmailRequest;
import com.codeforcommunity.rest.IRouter;
import com.codeforcommunity.rest.RestFunctions;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class ProtectedNeighborhoodsRouter implements IRouter {

  private final IProtectedNeighborhoodsProcessor processor;

  public ProtectedNeighborhoodsRouter(IProtectedNeighborhoodsProcessor processor) {
    this.processor = processor;
  }

  @Override
  public Router initializeRouter(Vertx vertx) {
    Router router = Router.router(vertx);

    registerSendEmail(router);
    registerEditCanopyCoverage(router);

    return router;
  }

  private void registerSendEmail(Router router) {
    Route adoptSiteRoute = router.post("/send_email");
    adoptSiteRoute.handler(this::handleSendEmail);
  }

  private void handleSendEmail(RoutingContext ctx) {
    JWTData userData = ctx.get("jwt_data");
    SendEmailRequest sendEmailRequest =
        RestFunctions.getJsonBodyAsClass(ctx, SendEmailRequest.class);

    processor.sendEmail(userData, sendEmailRequest);

    end(ctx.response(), 200);
  }

  private void registerEditCanopyCoverage(Router router) {
    Route editCanopyCoverageRoute = router.post("/:neighborhood_id/edit_canopy");
    editCanopyCoverageRoute.handler(this::handleEditCanopyCoverage);
  }

  private void handleEditCanopyCoverage(RoutingContext ctx) {
    JWTData userData = ctx.get("jwt_data");
    EditCanopyCoverageRequest editCanopyCoverageRequest =
        RestFunctions.getJsonBodyAsClass(ctx, EditCanopyCoverageRequest.class);
    int neighborhoodId = RestFunctions.getRequestParameterAsInt(ctx.request(), "neighborhood_id");

    processor.editCanopyCoverage(userData, neighborhoodId, editCanopyCoverageRequest);

    end(ctx.response(), 200);
  }
}
