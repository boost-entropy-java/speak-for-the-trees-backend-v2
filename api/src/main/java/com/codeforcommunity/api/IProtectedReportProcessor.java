package com.codeforcommunity.api;

import com.codeforcommunity.auth.JWTData;
import com.codeforcommunity.dto.report.GetAdoptionReportResponse;
import com.codeforcommunity.dto.report.GetReportCSVRequest;
import com.codeforcommunity.dto.report.GetSiteActivityReportCSVRequest;
import com.codeforcommunity.dto.report.GetStewardshipReportResponse;

public interface IProtectedReportProcessor {

  /**
   * Returns a list of information about adopters and stewardship activities for each currently
   * adopted site, grouped by user and in alphabetical order
   */
  GetAdoptionReportResponse getAdoptionReport(JWTData userData);

  /**
   * Returns a list of information about adopters and stewardship activities for each currently
   * adopted site, grouped by user and in alphabetical order, as a CSV-formatted string.
   */
  String getAdoptionReportCSV(JWTData userData, GetReportCSVRequest getAdoptionReportCSVRequest);

  /**
   * Returns a list of information about adopters and the actions performed for each stewardship
   * activity ever performed, grouped by siteId and in reverse chronological order (most recent
   * first).
   */
  GetStewardshipReportResponse getStewardshipReport(JWTData userData);

  /**
   * Returns a list of information about adopters and the actions performed for each stewardship
   * activity ever performed, grouped by site_id and in reverse chronological order (most recent
   * first), as a CSV-formatted string.
   */
  String getStewardshipReportCSV(
      JWTData userData, GetReportCSVRequest getStewardshipReportCSVRequest);

  /**
   * Returns stewardship activities for all adopted sites (or a specific site if siteId is provided)
   * as a CSV-formatted string. Each row represents a single activity type performed on a given
   * date. Columns: Site ID, Latitude, Longitude, Address, Date, Activity Type.
   */
  String getSiteActivityReportCSV(JWTData userData, GetSiteActivityReportCSVRequest request);
}
