package com.codeforcommunity.enums;

public enum SiteOwner {
  ROW("ROW"),
  Park("Park"),
  State("State"),
  Federal("Federal"),
  Private("Private");

  private final String owner;

  SiteOwner(String owner) {
    this.owner = owner;
  }

  public String getOwner() {
    return owner;
  }

  public static SiteOwner from(String owner) {
    for (SiteOwner siteOwner : SiteOwner.values()) {
      if (siteOwner.owner.equals(owner)) {
        return siteOwner;
      }
    }
    throw new IllegalArgumentException(
        String.format("Given owner `%s` doesn't correspond to any `SiteOwner`", owner));
  }
}
