package com.codeforcommunity.exceptions;

import com.codeforcommunity.rest.FailureHandler;
import io.vertx.ext.web.RoutingContext;

public class ForbiddenException extends HandledException {
  private final String message;

  public ForbiddenException(String message) {
    this.message = message;
  }

  @Override
  public void callHandler(FailureHandler handler, RoutingContext ctx) {
    handler.handleForbidden(ctx, this.message);
  }
}
