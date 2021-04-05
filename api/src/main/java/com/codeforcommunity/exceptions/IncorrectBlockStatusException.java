package com.codeforcommunity.exceptions;

import com.codeforcommunity.enums.ReservationAction;
import com.codeforcommunity.rest.FailureHandler;
import io.vertx.ext.web.RoutingContext;

import java.util.List;

public class IncorrectBlockStatusException extends HandledException {

  private final int blockId;
  private final List<ReservationAction> expectedStatuses;

  public IncorrectBlockStatusException(int blockId, List<ReservationAction> expectedStatuses) {
    this.blockId = blockId;
    this.expectedStatuses = expectedStatuses;
  }

  public int getBlockId() {
    return this.blockId;
  }

  public List<ReservationAction> getExpectedStatuses() {
    return this.expectedStatuses;
  }

  @Override
  public void callHandler(FailureHandler handler, RoutingContext ctx) {
    handler.handleIncorrectBlockStatus(ctx, this);
  }
}
