package com.codeforcommunity.api;

import com.codeforcommunity.dto.site.GetSiteResponse;
import com.codeforcommunity.dto.site.StewardshipActivitiesResponse;
import com.codeforcommunity.dto.site.TreeBenefitsResponse;

import java.util.List;

public interface ISiteProcessor {

  /** Returns all the info about a specific site, including all site entries */
  GetSiteResponse getSite(int siteId);

  /** Returns all stewardship activities for the given site */
  StewardshipActivitiesResponse getStewardshipActivities(int siteId);

  /** Retrieves all common names among all site entries, in ascending alphabetical order */
  List<String> getAllCommonNames();

  /** Calculates and returns the environmental impacts of the latest site entry of the site with the given site_id.
   * This includes the following values: energy conserved, stormwater filtered, air quality improved,
   * carbon dioxide removed, and carbon dioxide stored, as well as the amount of money saved for each category.*/
  TreeBenefitsResponse calculateBenefits(int siteId);
}
