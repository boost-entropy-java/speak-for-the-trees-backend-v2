package com.codeforcommunity.dto.team;

import com.codeforcommunity.enums.TeamRole;

public class TeamMembersResponse {
  private final int userId;
  private final String username;
  private final TeamRole teamRole;

  public TeamMembersResponse(int userId, String username, TeamRole teamRole) {
    this.userId = userId;
    this.username = username;
    this.teamRole = teamRole;
  }

  public int getUserId() {
    return this.userId;
  }

  public String getUsername() {
    return this.username;
  }

  public TeamRole getTeamRole() {
    return this.teamRole;
  }
}
