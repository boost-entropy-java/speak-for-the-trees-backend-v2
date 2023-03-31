package com.codeforcommunity.api;

import com.codeforcommunity.auth.JWTData;
import com.codeforcommunity.dto.auth.NewUserRequest;
import com.codeforcommunity.dto.user.ChangeEmailRequest;
import com.codeforcommunity.dto.user.ChangePasswordRequest;
import com.codeforcommunity.dto.user.ChangePrivilegeLevelRequest;
import com.codeforcommunity.dto.user.ChangeUsernameRequest;
import com.codeforcommunity.dto.user.DeleteUserRequest;
import com.codeforcommunity.dto.user.GetChildUserResponse;
import com.codeforcommunity.dto.user.UserDataResponse;
import com.codeforcommunity.dto.user.UserTeamsResponse;

public interface IProtectedUserProcessor {

  /** Deletes the given user from the database. Does NOT invalidate the user's JWTs */
  void deleteUser(JWTData userData, DeleteUserRequest deleteUserRequest);

  /**
   * If the given current password matches the user's current password, update the user's password
   * to the new password value.
   */
  void changePassword(JWTData userData, ChangePasswordRequest changePasswordRequest);

  /** Get the user's data for use in the site. */
  UserDataResponse getUserData(JWTData userData);

  /** Get the all the teams a user is part of */
  UserTeamsResponse getUserTeams(JWTData userData);

  /** Change the user's email to the provided one */
  void changeEmail(JWTData userData, ChangeEmailRequest changeEmailRequest);

  /** Change the user's username to the provided one */
  void changeUsername(JWTData userData, ChangeUsernameRequest changeUsernameRequest);

  /** Change the given user's privilege level to the provided one */
  void changePrivilegeLevel(
      JWTData userData, ChangePrivilegeLevelRequest changePrivilegeLevelRequest);

  /**
   * Add new child user to users table and link child to calling admin through parent_accounts table
   */
  void createChildUser(JWTData userData, NewUserRequest newUserRequest);

  /**Gets first name,last name, email, and username of the calling user's child accounts */
  GetChildUserResponse getChildUser(JWTData userData);
}
