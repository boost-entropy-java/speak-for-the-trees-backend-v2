package com.codeforcommunity.processor;

import static org.jooq.generated.Tables.TEAMS;
import static org.jooq.generated.Tables.USERS;
import static org.jooq.generated.tables.Goals.GOALS;
import static org.jooq.generated.tables.UsersTeams.USERS_TEAMS;

import com.codeforcommunity.api.ITeamsProcessor;
import com.codeforcommunity.auth.JWTData;
import com.codeforcommunity.dto.team.AddGoalRequest;
import com.codeforcommunity.dto.team.CreateTeamRequest;
import com.codeforcommunity.dto.team.GetTeamsResponse;
import com.codeforcommunity.dto.team.GoalResponse;
import com.codeforcommunity.dto.team.InviteUsersRequest;
import com.codeforcommunity.dto.team.TeamDataResponse;
import com.codeforcommunity.dto.team.TeamGoalDataResponse;
import com.codeforcommunity.dto.team.TeamMembersResponse;
import com.codeforcommunity.dto.team.TransferOwnershipRequest;
import com.codeforcommunity.dto.team.UsersResponse;
import com.codeforcommunity.enums.TeamRole;
import com.codeforcommunity.exceptions.LeaderCannotLeaveTeamException;
import com.codeforcommunity.exceptions.MemberApplicationException;
import com.codeforcommunity.exceptions.MemberStatusException;
import com.codeforcommunity.exceptions.ResourceDoesNotExistException;
import com.codeforcommunity.exceptions.UserDeletedException;
import com.codeforcommunity.exceptions.UserDoesNotExistException;
import com.codeforcommunity.exceptions.UserNotOnTeamException;
import com.codeforcommunity.exceptions.WrongTeamRoleException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.jooq.DSLContext;
import org.jooq.Record3;
import org.jooq.Result;
import org.jooq.generated.tables.records.GoalsRecord;
import org.jooq.generated.tables.records.TeamsRecord;
import org.jooq.generated.tables.records.UsersRecord;
import org.jooq.generated.tables.records.UsersTeamsRecord;

public class TeamsProcessorImpl implements ITeamsProcessor {
  private final DSLContext db;

  private void checkTeamExists(int teamId) {
    if (!db.fetchExists(db.selectFrom(TEAMS).where(TEAMS.ID.eq(teamId)))) {
      throw new ResourceDoesNotExistException(teamId, "Team");
    }
  }

  private void checkUserExists(int userId) {
    UsersRecord user = db.selectFrom(USERS).where(USERS.ID.eq(userId)).fetchOne();
    if (user == null) {
      throw new UserDoesNotExistException(userId);
    }
    if (user.getDeletedAt() != null) {
      throw new UserDeletedException(user.getId());
    }
  }

  private void checkGoalExists(int goalId) {
    if (!db.fetchExists(db.selectFrom(GOALS).where(GOALS.ID.eq(goalId)))) {
      throw new ResourceDoesNotExistException(goalId, "Goal");
    }
  }

  private UsersResponse getUsers(int teamId, TeamRole role) {
    checkTeamExists(teamId);
    List<UsersTeamsRecord> applicants =
        db.selectFrom(USERS_TEAMS).where(USERS_TEAMS.TEAM_ROLE.eq(role)).fetch();
    Map<Integer, TeamRole> users = new HashMap<>();
    applicants.forEach(app -> users.put(app.getUserId(), app.getTeamRole()));
    return new UsersResponse(users);
  }

  private UsersTeamsRecord getUsersLeadTeams(JWTData userData, int teamId) {
    return db.selectFrom(USERS_TEAMS)
        .where(USERS_TEAMS.USER_ID.eq(userData.getUserId()))
        .and(USERS_TEAMS.TEAM_ID.eq(teamId))
        .and(USERS_TEAMS.TEAM_ROLE.eq(TeamRole.LEADER))
        .fetchOne();
  }

  public TeamsProcessorImpl(DSLContext db) {
    this.db = db;
  }

  @Override
  public void disbandTeam(JWTData userData, int teamId) {
    checkTeamExists(teamId);
    UsersTeamsRecord usersTeamsRecord = getUsersLeadTeams(userData, teamId);

    if (usersTeamsRecord == null) {
      throw new WrongTeamRoleException(teamId, TeamRole.LEADER);
    }

    TeamsRecord team = db.selectFrom(TEAMS).where(TEAMS.ID.eq(teamId)).fetchOne();
    team.setDeletedAt(new Timestamp(System.currentTimeMillis()));
    team.store();
  }

  @Override
  public void createTeam(JWTData userData, CreateTeamRequest request) {
    TeamsRecord team = db.newRecord(TEAMS);
    team.setBio(request.getBio());
    team.setTeamName(request.getName());
    team.store();
    UsersTeamsRecord usersTeam = db.newRecord(USERS_TEAMS);
    usersTeam.setTeamId(team.getId());
    usersTeam.setTeamRole(TeamRole.LEADER);
    usersTeam.setUserId(userData.getUserId());
    usersTeam.store();
  }

  @Override
  public TeamGoalDataResponse getTeam(JWTData userData, int teamId) {
    TeamsRecord team = db.selectFrom(TEAMS).where(TEAMS.ID.eq(teamId)).fetchOne();
    if (team == null) {
      throw new ResourceDoesNotExistException(teamId, "Team");
    }

    List<
            Record3<
                Integer, // User Id
                String, // Username
                TeamRole // team role
            >>
        members =
            db.select(USERS_TEAMS.USER_ID, USERS.USERNAME, USERS_TEAMS.TEAM_ROLE)
                .from(USERS_TEAMS)
                .join(USERS)
                .on(USERS_TEAMS.USER_ID.eq(USERS.ID))
                .where(USERS_TEAMS.TEAM_ID.eq(teamId))
                .fetch();
    List<TeamMembersResponse> membersResponses =
        members.stream()
            .map(
                member ->
                    new TeamMembersResponse(member.value1(), member.value2(), member.value3()))
            .collect(Collectors.toList());

    List<GoalsRecord> goalsRecords = db.selectFrom(GOALS).where(GOALS.TEAM_ID.eq(teamId)).fetch();
    List<GoalResponse> goalsResponses =
        goalsRecords.stream()
            .map(
                goalsRecord ->
                    new GoalResponse(
                        goalsRecord.getId(),
                        goalsRecord.getGoal(),
                        goalsRecord.getStartAt(),
                        goalsRecord.getCompleteBy(),
                        goalsRecord.getCompletedAt()))
            .collect(Collectors.toList());

    return new TeamGoalDataResponse(
        team.getId(),
        team.getTeamName(),
        team.getBio(),
        team.getFinished(),
        membersResponses,
        goalsResponses,
        team.getCreatedAt(),
        team.getDeletedAt());
  }

  @Override
  public void addGoal(JWTData userData, AddGoalRequest addGoalRequest, int teamId) {
    checkTeamExists(teamId);
    UsersTeamsRecord usersTeamsRecord = getUsersLeadTeams(userData, teamId);

    if (usersTeamsRecord != null) {
      GoalsRecord goal = db.newRecord(GOALS);
      goal.setTeamId(teamId);
      goal.setGoal(addGoalRequest.getGoal());
      goal.setStartAt(addGoalRequest.getStartAt());
      goal.setCompleteBy(addGoalRequest.getCompleteBy());
      goal.store();
    } else {
      throw new WrongTeamRoleException(teamId, usersTeamsRecord.getTeamRole());
    }
  }

  @Override
  public void deleteGoal(JWTData userData, int teamId, int goalId) {
    checkTeamExists(teamId);
    checkGoalExists(goalId);

    UsersTeamsRecord usersTeamsRecord = getUsersLeadTeams(userData, teamId);

    if (usersTeamsRecord != null) {
      db.deleteFrom(GOALS).where(GOALS.ID.eq(goalId)).execute();
    } else {
      throw new WrongTeamRoleException(teamId, TeamRole.LEADER);
    }
  }

  @Override
  public void inviteUser(JWTData userData, InviteUsersRequest inviteUserRequest, int teamId) {
    checkTeamExists(teamId);
    UsersTeamsRecord inviterUserTeamsRecord = getUsersLeadTeams(userData, teamId);

    if (inviterUserTeamsRecord != null) {
      inviteUserRequest
          .getUsers()
          .forEach(
              inviteContact -> {
                if (!db.fetchExists(
                    USERS_TEAMS
                        .join(USERS)
                        .on(USERS.ID.eq(USERS_TEAMS.USER_ID))
                        .where(
                            USERS.EMAIL.eq(inviteContact.getEmail()).and(TEAMS.ID.eq(teamId))))) {
                  UsersRecord invitedUser =
                      db.selectFrom(USERS)
                          .where(USERS.EMAIL.eq(inviteContact.getEmail()))
                          .fetchOne();
                  if (invitedUser != null) {
                    UsersTeamsRecord usersTeams = db.newRecord(USERS_TEAMS);
                    usersTeams.setTeamId(teamId);
                    usersTeams.setUserId(invitedUser.getId());
                    usersTeams.setTeamRole(TeamRole.PENDING);
                    usersTeams.store();
                    // TODO SEND EMAIL TO ACCEPT ROLE ON TEAM
                  } else {
                    // TODO SEND EMAIL WITH INVITE TO CREATE USER / REQUEST TO JOIN TEAM
                  }
                }
              });
    } else {
      throw new WrongTeamRoleException(teamId, TeamRole.LEADER);
    }
  }

  @Override
  public UsersResponse getApplicants(JWTData userData, int teamId) {
    checkTeamExists(teamId);
    UsersTeamsRecord usersTeamsRecord = getUsersLeadTeams(userData, teamId);

    if (usersTeamsRecord != null) {
      return getUsers(teamId, TeamRole.PENDING);
    } else {
      throw new WrongTeamRoleException(teamId, TeamRole.LEADER);
    }
  }

  @Override
  public void applyToTeam(JWTData userData, int teamId) {
    checkTeamExists(teamId);
    if (db.fetchExists(
        USERS_TEAMS.where(
            USERS_TEAMS.TEAM_ID.eq(teamId).and(USERS_TEAMS.USER_ID.eq(userData.getUserId()))))) {
      throw new MemberApplicationException(teamId, userData.getUserId());
    } else {
      UsersRecord invitedUser =
          db.selectFrom(USERS).where(USERS.ID.eq(userData.getUserId())).fetchOne();
      if (invitedUser != null) {
        UsersTeamsRecord usersTeams = db.newRecord(USERS_TEAMS);
        usersTeams.setTeamId(teamId);
        usersTeams.setUserId(invitedUser.getId());
        usersTeams.setTeamRole(TeamRole.PENDING);
        usersTeams.store();
      } else {
        throw new UserDoesNotExistException(userData.getUserId());
      }
    }
  }

  @Override
  public void approveUser(JWTData userData, int teamId, int memberId) {
    checkUserExists(memberId);
    checkTeamExists(teamId);
    UsersTeamsRecord leaderTeamsRecord = getUsersLeadTeams(userData, teamId);

    if (leaderTeamsRecord != null) {
      UsersTeamsRecord approvedUserRecord =
          db.selectFrom(
                  USERS_TEAMS.where(
                      USERS_TEAMS
                          .USER_ID
                          .eq(memberId)
                          .and(USERS_TEAMS.TEAM_ROLE.eq(TeamRole.PENDING))))
              .fetchOne();
      if (approvedUserRecord != null) {
        approvedUserRecord.setTeamRole(TeamRole.MEMBER);
        approvedUserRecord.update();
      } else {
        throw new MemberStatusException(memberId, teamId);
      }
    } else {
      throw new WrongTeamRoleException(teamId, TeamRole.MEMBER);
    }
  }

  @Override
  public void rejectUser(JWTData userData, int teamId, int memberId) {
    checkUserExists(memberId);
    checkTeamExists(teamId);
    UsersTeamsRecord leaderTeamsRecord = getUsersLeadTeams(userData, teamId);

    if (leaderTeamsRecord != null) {
      UsersTeamsRecord approvedUserRecord =
          db.selectFrom(
                  USERS_TEAMS.where(
                      USERS_TEAMS
                          .TEAM_ID
                          .eq(teamId)
                          .and(USERS_TEAMS.USER_ID.eq(memberId))
                          .and(USERS_TEAMS.TEAM_ROLE.eq(TeamRole.PENDING))))
              .fetchOne();
      if (approvedUserRecord != null) {
        approvedUserRecord.setTeamRole(TeamRole.None);
        approvedUserRecord.update();
      } else {
        throw new MemberStatusException(userData.getUserId(), teamId);
      }
    } else {
      throw new WrongTeamRoleException(leaderTeamsRecord.getTeamId(), TeamRole.MEMBER);
    }
  }

  @Override
  public void kickUser(JWTData userData, int teamId, int memberId) {
    checkUserExists(memberId);
    checkTeamExists(teamId);
    UsersTeamsRecord leaderTeamsRecord = getUsersLeadTeams(userData, teamId);

    if (leaderTeamsRecord != null) {
      UsersTeamsRecord approvedUserRecord =
          db.selectFrom(
                  USERS_TEAMS.where(
                      USERS_TEAMS
                          .USER_ID
                          .eq(memberId)
                          .and(USERS_TEAMS.TEAM_ROLE.eq(TeamRole.MEMBER))))
              .fetchOne();
      if (approvedUserRecord != null) {
        approvedUserRecord.setTeamRole(TeamRole.None);
        approvedUserRecord.update();
      } else {
        throw new WrongTeamRoleException(teamId, TeamRole.MEMBER);
      }
    } else {
      throw new WrongTeamRoleException(teamId, TeamRole.LEADER);
    }
  }

  @Override
  public void leaveTeam(JWTData userData, int teamId) {
    checkTeamExists(teamId);
    UsersTeamsRecord usersTeamsRecord =
        db.selectFrom(USERS_TEAMS)
            .where(USERS_TEAMS.USER_ID.eq(userData.getUserId()))
            .and(USERS_TEAMS.TEAM_ID.eq(teamId))
            .fetchOne();
    if (usersTeamsRecord != null) {
      if (usersTeamsRecord.getTeamRole() == TeamRole.LEADER) {
        throw new LeaderCannotLeaveTeamException(teamId, userData.getUserId());
      }
      usersTeamsRecord.setTeamRole(TeamRole.None);
      usersTeamsRecord.update();
    } else {
      throw new MemberStatusException(teamId, userData.getUserId());
    }
  }

  @Override
  public UsersResponse getMembers(int teamId) {
    Map<Integer, TeamRole> users = getUsers(teamId, TeamRole.MEMBER).getUsers();
    users.putAll(getUsers(teamId, TeamRole.LEADER).getUsers());
    return new UsersResponse(users);
  }

  @Override
  public void transferOwnership(
      JWTData userData, TransferOwnershipRequest transferOwnershipRequest, int teamId) {
    checkUserExists(transferOwnershipRequest.getNewLeaderId());
    checkTeamExists(teamId);
    UsersTeamsRecord oldLeaderTeamsRecord = getUsersLeadTeams(userData, teamId);
    UsersTeamsRecord newLeaderTeamsRecord =
        db.selectFrom(USERS_TEAMS)
            .where(
                USERS_TEAMS
                    .USER_ID
                    .eq(transferOwnershipRequest.getNewLeaderId())
                    .and(USERS_TEAMS.TEAM_ID.eq(teamId)))
            .and(USERS_TEAMS.TEAM_ROLE.eq(TeamRole.MEMBER))
            .fetchOne();
    if (oldLeaderTeamsRecord == null) {
      throw new WrongTeamRoleException(teamId, TeamRole.LEADER);
    } else if (newLeaderTeamsRecord == null) {
      throw new UserNotOnTeamException(userData.getUserId(), teamId);
    } else {
      newLeaderTeamsRecord.setTeamRole(TeamRole.LEADER);
      oldLeaderTeamsRecord.setTeamRole(TeamRole.MEMBER);
      newLeaderTeamsRecord.update();
      oldLeaderTeamsRecord.update();
    }
  }

  @Override
  public GetTeamsResponse getTeams() {
    Result<TeamsRecord> teamsRecordResult =
        db.selectFrom(TEAMS).where(TEAMS.DELETED_AT.isNull()).fetch();
    return new GetTeamsResponse(
        teamsRecordResult.map(
            teamsRecord ->
                new TeamDataResponse(
                    teamsRecord.getId(),
                    teamsRecord.getTeamName(),
                    teamsRecord.getBio(),
                    teamsRecord.getFinished(),
                    teamsRecord.getCreatedAt(),
                    teamsRecord.getDeletedAt())));
  }
}
