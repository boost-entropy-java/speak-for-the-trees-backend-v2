package com.codeforcommunity.rest.subrouter;

import static com.codeforcommunity.rest.ApiRouter.end;

import com.codeforcommunity.api.IProtectedEmailerProcessor;
import com.codeforcommunity.auth.JWTData;
import com.codeforcommunity.dto.emailer.AddTemplateRequest;
import com.codeforcommunity.dto.emailer.LoadTemplateResponse;
import com.codeforcommunity.rest.IRouter;
import com.codeforcommunity.rest.RestFunctions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class ProtectedEmailerRouter implements IRouter {

  private final IProtectedEmailerProcessor processor;

  public ProtectedEmailerRouter(IProtectedEmailerProcessor processor) {
    this.processor = processor;
  }

  @Override
  public Router initializeRouter(Vertx vertx) {
    Router router = Router.router(vertx);

    registerAddTemplate(router);
    registerLoadTemplate(router);
    registerDeleteTemplate(router);

    return router;
  }

  private void registerAddTemplate(Router router) {
    Route addTemplate = router.post("/add_template");
    addTemplate.handler(this::handleAddTemplate);
  }

  private void handleAddTemplate(RoutingContext ctx) {
    JWTData userData = ctx.get("jwt_data");
    AddTemplateRequest addTemplateRequest =
        RestFunctions.getJsonBodyAsClass(ctx, AddTemplateRequest.class);

    processor.addTemplate(userData, addTemplateRequest);

    end(ctx.response(), 200);
  }
  
  private void registerLoadTemplate(Router router) {
    Route loadTemplate = router.get("/load_template/:template_name");
    loadTemplate.handler(this::handleLoadTemplate);
  }

  private void handleLoadTemplate(RoutingContext ctx) {
    JWTData userData = ctx.get("jwt_data");
    String templateName = RestFunctions.getRequestParameterAsString(ctx.request(), "template_name");

    LoadTemplateResponse loadTemplateResponse = processor.loadTemplate(userData, templateName);

    end(ctx.response(), 200, JsonObject.mapFrom(loadTemplateResponse).toString());
  }

  private void registerDeleteTemplate(Router router) {
    Route deleteTemplate = router.post("/delete_template/:template_name");
    deleteTemplate.handler(this::handleDeleteTemplate);
  }

  private void handleDeleteTemplate(RoutingContext ctx) {
    JWTData userData = ctx.get("jwt_data");
    String templateName = RestFunctions.getRequestParameterAsString(ctx.request(), "template_name");

    processor.deleteTemplate(userData, templateName);

    end(ctx.response(), 200);
  }
}
