package com.codeforcommunity.rest.subrouter;

import static com.codeforcommunity.rest.ApiRouter.end;

import com.codeforcommunity.api.IProtectedUserProcessor;
import com.codeforcommunity.auth.JWTData;
import com.codeforcommunity.dto.auth.NewUserRequest;
import com.codeforcommunity.dto.user.ChangeEmailRequest;
import com.codeforcommunity.dto.user.ChangePasswordRequest;
import com.codeforcommunity.dto.user.ChangePrivilegeLevelRequest;
import com.codeforcommunity.dto.user.ChangeUsernameRequest;
import com.codeforcommunity.dto.user.DeleteUserRequest;
import com.codeforcommunity.dto.user.GetChildUserResponse;
import com.codeforcommunity.dto.user.UserDataResponse;
import com.codeforcommunity.dto.user.UserTeamsResponse;
import com.codeforcommunity.rest.IRouter;
import com.codeforcommunity.rest.RestFunctions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class ProtectedUserRouter implements IRouter {

  private final IProtectedUserProcessor processor;

  public ProtectedUserRouter(IProtectedUserProcessor processor) {
    this.processor = processor;
  }

  @Override
  public Router initializeRouter(Vertx vertx) {
    Router router = Router.router(vertx);

    registerDeleteUser(router);
    registerChangePassword(router);
    registerGetUserData(router);
    registerGetUserTeams(router);
    registerChangeEmail(router);
    registerChangeUsername(router);
    registerChangePrivilegeLevel(router);
    registerCreateChildUser(router);
    registerGetChildUser(router);

    return router;
  }

  private void registerDeleteUser(Router router) {
    Route deleteUserRoute = router.post("/delete");
    deleteUserRoute.handler(this::handleDeleteUserRoute);
  }

  private void registerChangePassword(Router router) {
    Route changePasswordRoute = router.post("/change_password");
    changePasswordRoute.handler(this::handleChangePasswordRoute);
  }

  private void registerGetUserData(Router router) {
    Route getUserDataRoute = router.get("/data");
    getUserDataRoute.handler(this::handleGetUserDataRoute);
  }

  private void registerGetUserTeams(Router router) {
    Route getUserTeamsRoute = router.get("/teams");
    getUserTeamsRoute.handler(this::handleGetUserTeamsRoute);
  }

  private void registerChangeEmail(Router router) {
    Route changePasswordRoute = router.post("/change_email");
    changePasswordRoute.handler(this::handleChangeEmailRoute);
  }

  private void registerChangeUsername(Router router) {
    Route changeUsernameRoute = router.post("/change_username");
    changeUsernameRoute.handler(this::handleChangeUsernameRoute);
  }

  private void registerChangePrivilegeLevel(Router router) {
    Route changePrivilegeLevelRoute = router.post("/change_privilege");
    changePrivilegeLevelRoute.handler(this::handleChangePrivilegeLevelRoute);
  }

  private void handleDeleteUserRoute(RoutingContext ctx) {
    JWTData userData = ctx.get("jwt_data");

    DeleteUserRequest deleteUserRequest =
        RestFunctions.getJsonBodyAsClass(ctx, DeleteUserRequest.class);

    processor.deleteUser(userData, deleteUserRequest);

    end(ctx.response(), 200);
  }

  private void handleChangePasswordRoute(RoutingContext ctx) {
    JWTData userData = ctx.get("jwt_data");
    ChangePasswordRequest changePasswordRequest =
        RestFunctions.getJsonBodyAsClass(ctx, ChangePasswordRequest.class);

    processor.changePassword(userData, changePasswordRequest);

    end(ctx.response(), 200);
  }

  private void handleGetUserDataRoute(RoutingContext ctx) {
    JWTData userData = ctx.get("jwt_data");

    UserDataResponse response = processor.getUserData(userData);

    end(ctx.response(), 200, JsonObject.mapFrom(response).toString());
  }

  private void handleGetUserTeamsRoute(RoutingContext ctx) {
    JWTData userData = ctx.get("jwt_data");

    UserTeamsResponse response = processor.getUserTeams(userData);

    end(ctx.response(), 200, JsonObject.mapFrom(response).toString());
  }

  private void handleChangeEmailRoute(RoutingContext ctx) {
    JWTData userData = ctx.get("jwt_data");
    ChangeEmailRequest changeEmailRequest =
        RestFunctions.getJsonBodyAsClass(ctx, ChangeEmailRequest.class);

    processor.changeEmail(userData, changeEmailRequest);

    end(ctx.response(), 200);
  }

  private void handleChangeUsernameRoute(RoutingContext ctx) {
    JWTData userData = ctx.get("jwt_data");
    ChangeUsernameRequest changeUsernameRequest =
        RestFunctions.getJsonBodyAsClass(ctx, ChangeUsernameRequest.class);

    processor.changeUsername(userData, changeUsernameRequest);

    end(ctx.response(), 200);
  }

  private void handleChangePrivilegeLevelRoute(RoutingContext ctx) {
    JWTData userData = ctx.get("jwt_data");
    ChangePrivilegeLevelRequest changePrivilegeLevelRequest =
        RestFunctions.getJsonBodyAsClass(ctx, ChangePrivilegeLevelRequest.class);

    processor.changePrivilegeLevel(userData, changePrivilegeLevelRequest);

    end(ctx.response(), 200);
  }

  private void registerCreateChildUser(Router router) {
    Route createChildUser = router.post("/create_child");
    createChildUser.handler(this::handleCreateChildUser);
  }

  private void handleCreateChildUser(RoutingContext ctx) {
    JWTData userData = ctx.get("jwt_data");
    NewUserRequest newUserRequest = RestFunctions.getJsonBodyAsClass(ctx, NewUserRequest.class);

    processor.createChildUser(userData, newUserRequest);

    end(ctx.response(), 201);
  }

  private void registerGetChildUser(Router router) {
    Route getChildUser = router.get("/child_data");
    getChildUser.handler(this::handleGetChildUser);
  }

  private void handleGetChildUser(RoutingContext ctx) {
    JWTData userData = ctx.get("jwt_data");

    GetChildUserResponse response = processor.getChildUser(userData);

    end(ctx.response(), 200, JsonObject.mapFrom(response).toString());
  }
}
