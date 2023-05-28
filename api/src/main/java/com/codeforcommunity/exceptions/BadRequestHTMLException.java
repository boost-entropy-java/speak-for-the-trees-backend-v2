package com.codeforcommunity.exceptions;

import com.codeforcommunity.rest.FailureHandler;
import io.vertx.ext.web.RoutingContext;

public class BadRequestHTMLException extends HandledException {
  private final String errors;

  public BadRequestHTMLException(String errors) {
    this.errors = errors;
  }

  @Override
  public void callHandler(FailureHandler handler, RoutingContext ctx) {
    handler.handleBadHTMLRequest(ctx, this.errors);
  }
}
