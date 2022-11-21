package com.codeforcommunity.api;

import com.codeforcommunity.auth.JWTData;
import com.codeforcommunity.dto.site.AddSiteRequest;
import com.codeforcommunity.dto.site.AddSitesRequest;
import com.codeforcommunity.dto.site.AdoptedSitesResponse;
import com.codeforcommunity.dto.site.EditSiteRequest;
import com.codeforcommunity.dto.site.NameSiteEntryRequest;
import com.codeforcommunity.dto.site.ParentAdoptSiteRequest;
import com.codeforcommunity.dto.site.ParentRecordStewardshipRequest;
import com.codeforcommunity.dto.site.RecordStewardshipRequest;
import com.codeforcommunity.dto.site.UpdateSiteRequest;
import java.sql.Date;

public interface IProtectedSiteProcessor {

  /**
   * Creates a record in the adopted sites table linking the user, the site, and the date adopted
   */
  void adoptSite(JWTData userData, int siteId, Date dateAdopted);

  /** Removes the record in the adopted sites table linking the user and the site */
  void unadoptSite(JWTData userData, int siteId);

  /** Removes the record in the adopted sites table linking the site to its current adopter */
  void forceUnadoptSite(JWTData userData, int siteId);

  /** Adopts a tree for a child account through the parent account **/
  void parentAdoptSite(JWTData parentUserData, int siteId, ParentAdoptSiteRequest parentAdoptSiteRequest,
                       Date dateAdopted);

  /** Get users adopted sites */
  AdoptedSitesResponse getAdoptedSites(JWTData userData);

  /** Records a new stewardship activity in the stewardship table linked to the given site */
  void recordStewardship(
      JWTData userData, int siteId, RecordStewardshipRequest recordStewardshipRequest);

  /** Records a new stewardship activity for a child account through a parent account */
  void parentRecordStewardship(
      JWTData userData, int siteId, ParentRecordStewardshipRequest parentRecordStewardshipRequest);


  /** Creates a new site with entries in the sites and siteEntries tables */
  void addSite(JWTData userData, AddSiteRequest addSiteRequest);

  /** Updates the latest state of the site and creates a new entry in the siteEntries table */
  void updateSite(JWTData userData, int siteId, UpdateSiteRequest updateSiteRequest);

  /** Edits features of the site */
  void editSite(JWTData userData, int siteId, EditSiteRequest editSiteRequest);

  /** Creates new sites with entries for each item in the list */
  void addSites(JWTData userData, AddSitesRequest addSitesRequest);

  /** Removes the site */
  void deleteSite(JWTData userData, int siteId);

  /** Removes the given stewardship activity */
  void deleteStewardship(JWTData userData, int activityId);

  /**
   * Renames the latest site entry of the site with the given siteId using the new name specified in
   * the nameSiteEntryRequest
   */
  void nameSiteEntry(JWTData userData, int siteId, NameSiteEntryRequest nameSiteEntryRequest);
}
