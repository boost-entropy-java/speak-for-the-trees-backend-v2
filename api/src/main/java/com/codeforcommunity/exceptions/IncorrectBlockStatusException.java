package com.codeforcommunity.exceptions;

import com.codeforcommunity.enums.ReservationAction;
import com.codeforcommunity.rest.FailureHandler;
import io.vertx.ext.web.RoutingContext;

import java.util.List;

public class IncorrectBlockStatusException extends HandledException {

  private final int blockId;
  private final List<ReservationAction> expectedStatus;

  public IncorrectBlockStatusException(int blockId, List<ReservationAction> expectedStatus) {
    this.blockId = blockId;
    this.expectedStatus = expectedStatus;
  }

  public int getBlockId() {
    return this.blockId;
  }

  public List<ReservationAction> getExpectedStatus() {
    return this.expectedStatus;
  }

  @Override
  public void callHandler(FailureHandler handler, RoutingContext ctx) {
    handler.handleIncorrectBlockStatus(ctx, this);
  }
}
