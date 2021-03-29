package com.codeforcommunity.enums;

public enum ReservationAction {
  RESERVE("reserve"),
  COMPLETE("complete"),
  RELEASE("release"),
  INCOMPLETE("incomplete"),
  QA("qa");

  private String name;

  ReservationAction(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public static ReservationAction from(String name) {
    for (ReservationAction reservationAction : ReservationAction.values()) {
      if (reservationAction.name.equals(name)) {
        return reservationAction;
      }
    }
    throw new IllegalArgumentException(
        String.format("Given action `%s` doesn't correspond to any `ReservationAction`", name));
  }
}
