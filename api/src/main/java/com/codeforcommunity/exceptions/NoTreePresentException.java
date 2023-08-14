package com.codeforcommunity.exceptions;

import com.codeforcommunity.rest.FailureHandler;

import io.vertx.ext.web.RoutingContext;

public class NoTreePresentException extends HandledException {

  private final int siteId;

  public NoTreePresentException(int siteId) {
    this.siteId = siteId;
  }

  @Override
  public void callHandler(FailureHandler handler, RoutingContext ctx) {
    handler.handleNoTreePresentException(ctx, this.siteId);
  }
}
