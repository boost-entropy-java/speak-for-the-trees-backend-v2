package com.codeforcommunity.rest;

import com.codeforcommunity.exceptions.CreateUserException;
import com.codeforcommunity.exceptions.EmailAlreadyInUseException;
import com.codeforcommunity.exceptions.ExpiredSecretKeyException;
import com.codeforcommunity.exceptions.HandledException;
import com.codeforcommunity.exceptions.IncorrectBlockStatusException;
import com.codeforcommunity.exceptions.InvalidSecretKeyException;
import com.codeforcommunity.exceptions.LeaderCannotLeaveTeamException;
import com.codeforcommunity.exceptions.LinkedResourceDoesNotExistException;
import com.codeforcommunity.exceptions.MalformedParameterException;
import com.codeforcommunity.exceptions.MemberApplicationException;
import com.codeforcommunity.exceptions.MemberStatusException;
import com.codeforcommunity.exceptions.MissingHeaderException;
import com.codeforcommunity.exceptions.MissingParameterException;
import com.codeforcommunity.exceptions.ResourceDoesNotExistException;
import com.codeforcommunity.exceptions.TokenInvalidException;
import com.codeforcommunity.exceptions.UsedSecretKeyException;
import com.codeforcommunity.exceptions.UserDeletedException;
import com.codeforcommunity.exceptions.UserDoesNotExistException;
import com.codeforcommunity.exceptions.UserNotOnTeamException;
import com.codeforcommunity.exceptions.UsernameAlreadyInUseException;
import com.codeforcommunity.exceptions.WrongAdoptionStatusException;
import com.codeforcommunity.exceptions.WrongTeamRoleException;
import com.codeforcommunity.logger.SLogger;
import io.vertx.ext.web.RoutingContext;

public class FailureHandler {
  private final SLogger logger = new SLogger(FailureHandler.class);

  public void handleFailure(RoutingContext ctx) {
    Throwable throwable = ctx.failure();

    if (throwable instanceof HandledException) {
      ((HandledException) throwable).callHandler(this, ctx);
    } else {
      this.handleUncaughtError(ctx, throwable);
    }
  }

  public void handleAuth(RoutingContext ctx) {
    end(ctx, "Unauthorized user", 401);
  }

  public void handleMissingParameter(RoutingContext ctx, MissingParameterException e) {
    String message =
        String.format("Missing required path parameter: %s", e.getMissingParameterName());
    end(ctx, message, 400);
  }

  public void handleEmailAlreadyInUse(RoutingContext ctx, EmailAlreadyInUseException exception) {
    String message =
        String.format("Error creating new user, given email %s already used", exception.getEmail());

    end(ctx, message, 409);
  }

  public void handleUsernameAlreadyInUse(
      RoutingContext ctx, UsernameAlreadyInUseException exception) {
    String message =
        String.format(
            "Error creating new user, given username %s already used", exception.getUsername());

    end(ctx, message, 409);
  }

  public void handleInvalidSecretKey(RoutingContext ctx, InvalidSecretKeyException exception) {
    String message = String.format("Given %s token is invalid", exception.getType());
    end(ctx, message, 401);
  }

  public void handleUsedSecretKey(RoutingContext ctx, UsedSecretKeyException exception) {
    String message = String.format("Given %s token has already been used", exception.getType());
    end(ctx, message, 401);
  }

  public void handleExpiredSecretKey(RoutingContext ctx, ExpiredSecretKeyException exception) {
    String message = String.format("Given %s token is expired", exception.getType());
    end(ctx, message, 401);
  }

  public void handleInvalidPassword(RoutingContext ctx) {
    String message = "Given password does not meet the security requirements";
    end(ctx, message, 400);
  }

  public void handleUserDoesNotExist(RoutingContext ctx, UserDoesNotExistException exception) {
    String message =
        String.format("No user with property <%s> exists", exception.getIdentifierMessage());
    end(ctx, message, 400);
  }

  public void handleUserDeleted(RoutingContext ctx, UserDeletedException e) {
    String message =
        String.format("User with property <%s> has been deleted", e.getIdentifierMessage());
    end(ctx, message, 400);
  }

  public void handleResourceDoesNotExist(RoutingContext ctx, ResourceDoesNotExistException e) {
    String message =
        String.format("No <%s> with id <%d> exists", e.getResourceType(), e.getResourceId());
    end(ctx, message, 400);
  }

  public void handleLinkedResourceDoesNotExist(
      RoutingContext ctx, LinkedResourceDoesNotExistException e) {
    String message =
        String.format(
            "No linked resource <%s> with resource <%s> of id <%d> and resource <%s> of id <%d> exists",
            e.getlinkedResourceType(),
            e.getResource1Type(),
            e.getResource1Id(),
            e.getResource2Type(),
            e.getResource2Id());
    end(ctx, message, 400);
  }

  public void handleIncorrectBlockStatus(RoutingContext ctx, IncorrectBlockStatusException e) {
    String message =
        String.format("Status of block id <%d> is not <%s>", e.getBlockId(), e.getExpectedStatus());
    end(ctx, message, 400);
  }

  public void handleUserNotOnTeam(RoutingContext ctx, UserNotOnTeamException e) {
    String message =
        String.format("The user <%d> is not on a team with id <%d>", e.getUserId(), e.getTeamId());
    end(ctx, message, 401);
  }

  public void handleMissingHeader(RoutingContext ctx, MissingHeaderException e) {
    String message = String.format("Missing required request header: %s", e.getMissingHeaderName());
    end(ctx, message, 400);
  }

  public void handleRequestBodyMapping(RoutingContext ctx) {
    String message = "Malformed json request body";
    end(ctx, message, 400);
  }

  public void handleMissingBody(RoutingContext ctx) {
    String message = "Missing required request body";
    end(ctx, message, 400);
  }

  public void handleCreateUser(RoutingContext ctx, CreateUserException exception) {
    CreateUserException.UsedField reason = exception.getUsedField();

    String reasonMessage =
        reason.equals(CreateUserException.UsedField.BOTH)
            ? "email and user name"
            : reason.toString();

    String message = String.format("Error creating new user, given %s already used", reasonMessage);

    end(ctx, message, 409);
  }

  public void handleWrongTeamRoleException(RoutingContext ctx, WrongTeamRoleException e) {
    String message =
        String.format(
            "This route is only available users with team role %s for team %d",
            e.getTeamRole(), e.getTeamId());
    end(ctx, message, 401);
  }

  public void handleTokenInvalid(RoutingContext ctx, TokenInvalidException e) {
    String message = String.format("Given %s token is expired or invalid", e.getTokenType());
    end(ctx, message, 401);
  }

  public void handleWrongPassword(RoutingContext ctx) {
    String message = "Given password is not correct";
    end(ctx, message, 401);
  }

  public void handleInvalidToken(RoutingContext ctx) {
    String message = "Given token is invalid";
    end(ctx, message, 401);
  }

  public void handleInvalidURL(RoutingContext ctx) {
    String message = "Given URL is invalid";
    end(ctx, message, 400);
  }

  public void handleExpiredToken(RoutingContext ctx) {
    String message = "Given token is expired";
    end(ctx, message, 401);
  }

  public void handleMalformedParameter(RoutingContext ctx, MalformedParameterException exception) {
    String message =
        String.format("Given parameter(s) %s is (are) malformed", exception.getParameterName());
    end(ctx, message, 400);
    end(ctx, message, 400);
  }

  public void handleBadImageRequest(RoutingContext ctx) {
    String message = "The uploaded file could not be processed as an image";
    end(ctx, message, 400);
  }

  public void handleS3FailedUpload(RoutingContext ctx, String exceptionMessage) {
    String message = "The given file could not be uploaded to AWS S3: " + exceptionMessage;
    end(ctx, message, 502);
  }

  public void handleSamePrivilegeLevel(RoutingContext ctx) {
    String message = "The user already has the given privilege level";
    end(ctx, message, 400);
  }

  public void handleRouteInvalid(RoutingContext ctx, String reason) {
    String message = "This route is not callable because: " + reason;
    end(ctx, message, 400);
  }

  public void handleMemberApplicationException(RoutingContext ctx, MemberApplicationException e) {
    String message = "User " + e.getUserId() + " has already applied for team " + e.getTeamId();
    end(ctx, message, 400);
  }

  public void handleLeaderCannotLeaveTeamException(
      RoutingContext ctx, LeaderCannotLeaveTeamException e) {
    String message =
        "User "
            + e.getUserId()
            + " is the leader of team "
            + e.getTeamId()
            + " and must transfer ownership before leaving";
    end(ctx, message, 400);
  }

  public void handleMemberStatusException(RoutingContext ctx, MemberStatusException e) {
    String message =
        "User " + e.getUserId() + " must be a pending member of Team " + e.getMessage();
    end(ctx, message, 400);
  }

  public void handleWrongAdoptionStatusException(
      RoutingContext ctx, WrongAdoptionStatusException e) {
    String message;

    if (e.getAlreadyAdopted()) {
      message = "Site is already marked as adopted";
    } else {
      message = "Site is not marked as adopted";
    }

    end(ctx, message, 400);
  }

  private void handleUncaughtError(RoutingContext ctx, Throwable throwable) {
    String message = String.format("Internal server error caused by: %s", throwable.getMessage());
    logger.error(message);
    throwable.printStackTrace();
    end(ctx, message, 500);
  }

  private void end(RoutingContext ctx, String message, int statusCode) {
    ctx.response().setStatusCode(statusCode).end(message);
  }
}
