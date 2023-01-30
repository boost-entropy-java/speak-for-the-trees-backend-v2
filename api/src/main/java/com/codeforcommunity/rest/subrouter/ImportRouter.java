package com.codeforcommunity.rest.subrouter;

import static com.codeforcommunity.rest.ApiRouter.end;

import com.codeforcommunity.api.IImportProcessor;
import com.codeforcommunity.auth.JWTData;
import com.codeforcommunity.dto.imports.ImportBlocksRequest;
import com.codeforcommunity.dto.imports.ImportNeighborhoodsRequest;
import com.codeforcommunity.dto.imports.ImportReservationsRequest;
import com.codeforcommunity.dto.imports.ImportSitesRequest;
import com.codeforcommunity.dto.imports.ImportTreeBenefitsRequest;
import com.codeforcommunity.dto.imports.ImportTreeSpeciesRequest;
import com.codeforcommunity.rest.IRouter;
import com.codeforcommunity.rest.RestFunctions;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class ImportRouter implements IRouter {

  private final IImportProcessor processor;

  public ImportRouter(IImportProcessor processor) {
    this.processor = processor;
  }

  @Override
  public Router initializeRouter(Vertx vertx) {
    Router router = Router.router(vertx);

    registerImportBlocks(router);
    registerImportNeighborhoods(router);
    registerImportReservations(router);
    registerImportSites(router);
    registerImportTreeSpecies(router);
    registerImportTreeBenefits(router);

    return router;
  }

  private void registerImportBlocks(Router router) {
    Route importBlocksRoute = router.post("/blocks");
    importBlocksRoute.handler(this::handleImportBlocksRoute);
  }

  private void registerImportNeighborhoods(Router router) {
    Route importNeighborhoodsRoute = router.post("/neighborhoods");
    importNeighborhoodsRoute.handler(this::handleImportNeighborhoodsRoute);
  }

  private void registerImportReservations(Router router) {
    Route importReservationsRoute = router.post("/reservations");
    importReservationsRoute.handler(this::handleImportReservationsRoute);
  }

  private void registerImportSites(Router router) {
    Route importSitesRoute = router.post("/sites");
    importSitesRoute.handler(this::handleImportSitesRoute);
  }

  private void registerImportTreeSpecies(Router router) {
    Route importTreeSpeciesRoute = router.post("/tree_species");
    importTreeSpeciesRoute.handler(this::handleImportTreeSpeciesRoute);
  }

  private void registerImportTreeBenefits(Router router) {
    Route importTreeBenefitsRoute = router.post("/tree_benefits");
    importTreeBenefitsRoute.handler(this::handleImportTreeBenefitsRoute);
  }

  private void handleImportBlocksRoute(RoutingContext ctx) {
    JWTData userData = ctx.get("jwt_data");
    ImportBlocksRequest importBlocksRequest =
        RestFunctions.getJsonBodyAsClass(ctx, ImportBlocksRequest.class);

    processor.importBlocks(userData, importBlocksRequest);

    end(ctx.response(), 200);
  }

  private void handleImportNeighborhoodsRoute(RoutingContext ctx) {
    JWTData userData = ctx.get("jwt_data");
    ImportNeighborhoodsRequest importNeighborhoodsRequest =
        RestFunctions.getJsonBodyAsClass(ctx, ImportNeighborhoodsRequest.class);

    processor.importNeighborhoods(userData, importNeighborhoodsRequest);

    end(ctx.response(), 200);
  }

  private void handleImportReservationsRoute(RoutingContext ctx) {
    JWTData userData = ctx.get("jwt_data");
    ImportReservationsRequest importReservationsRequest =
        RestFunctions.getJsonBodyAsClass(ctx, ImportReservationsRequest.class);

    processor.importReservations(userData, importReservationsRequest);

    end(ctx.response(), 200);
  }

  private void handleImportSitesRoute(RoutingContext ctx) {
    JWTData userData = ctx.get("jwt_data");
    ImportSitesRequest importSitesRequest =
        RestFunctions.getJsonBodyAsClass(ctx, ImportSitesRequest.class);

    processor.importSites(userData, importSitesRequest);

    end(ctx.response(), 200);
  }

  private void handleImportTreeSpeciesRoute(RoutingContext ctx) {
    JWTData userData = ctx.get("jwt_data");
    ImportTreeSpeciesRequest importTreeSpeciesRequest =
        RestFunctions.getJsonBodyAsClass(ctx, ImportTreeSpeciesRequest.class);

    processor.importTreeSpecies(userData, importTreeSpeciesRequest);

    end(ctx.response(), 200);
  }

  private void handleImportTreeBenefitsRoute(RoutingContext ctx) {
    JWTData userData = ctx.get("jwt_data");
    ImportTreeBenefitsRequest importTreeBenefitsRequest =
        RestFunctions.getJsonBodyAsClass(ctx, ImportTreeBenefitsRequest.class);

    processor.importTreeBenefits(userData, importTreeBenefitsRequest);

    end(ctx.response(), 200);
  }
}
