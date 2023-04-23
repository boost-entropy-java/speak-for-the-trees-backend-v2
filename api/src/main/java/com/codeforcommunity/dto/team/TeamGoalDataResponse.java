package com.codeforcommunity.dto.team;

import java.sql.Timestamp;
import java.util.List;

public class TeamGoalDataResponse extends TeamDataResponse {
  private final List<TeamMembersResponse> members;
  private final List<GoalResponse> goals;

  public TeamGoalDataResponse(
      Integer id,
      String teamName,
      String bio,
      Boolean finished,
      List<TeamMembersResponse> members,
      List<GoalResponse> goals,
      Timestamp createdAt,
      Timestamp deletedAt) {
    super(id, teamName, bio, finished, createdAt, deletedAt);
    this.members = members;
    this.goals = goals;
  }

  public List<TeamMembersResponse> getMembers() {
    return members;
  }

  public List<GoalResponse> getGoals() {
    return goals;
  }
}
