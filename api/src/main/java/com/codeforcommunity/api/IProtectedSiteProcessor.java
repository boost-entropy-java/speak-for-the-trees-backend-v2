package com.codeforcommunity.api;

import com.codeforcommunity.auth.JWTData;
import com.codeforcommunity.dto.site.AddSiteRequest;
import com.codeforcommunity.dto.site.AddSitesRequest;
import com.codeforcommunity.dto.site.AdoptedSitesResponse;
import com.codeforcommunity.dto.site.EditSiteRequest;
import com.codeforcommunity.dto.site.EditStewardshipRequest;
import com.codeforcommunity.dto.site.FilterSitesRequest;
import com.codeforcommunity.dto.site.FilterSitesResponse;
import com.codeforcommunity.dto.site.NameSiteEntryRequest;
import com.codeforcommunity.dto.site.ParentAdoptSiteRequest;
import com.codeforcommunity.dto.site.ParentRecordStewardshipRequest;
import com.codeforcommunity.dto.site.RecordStewardshipRequest;
import com.codeforcommunity.dto.site.UpdateSiteRequest;
import com.codeforcommunity.dto.site.UploadSiteImageRequest;
import java.sql.Date;
import java.util.List;

public interface IProtectedSiteProcessor {

  /**
   * Creates a record in the adopted sites table linking the user, the site, and the date adopted
   */
  void adoptSite(JWTData userData, int siteId, Date dateAdopted);

  /** Removes the record in the adopted sites table linking the user and the site */
  void unadoptSite(JWTData userData, int siteId);

  /** Removes the record in the adopted sites table linking the site to its current adopter */
  void forceUnadoptSite(JWTData userData, int siteId);

  /** Adopts a tree for a child account through the parent account * */
  void parentAdoptSite(
      JWTData parentUserData,
      int siteId,
      ParentAdoptSiteRequest parentAdoptSiteRequest,
      Date dateAdopted);

  /** Get users adopted sites */
  AdoptedSitesResponse getAdoptedSites(JWTData userData);

  /** Records a new stewardship activity in the stewardship table linked to the given site */
  void recordStewardship(
      JWTData userData, int siteId, RecordStewardshipRequest recordStewardshipRequest);

  /** Records a new stewardship activity for a child account through a parent account */
  void parentRecordStewardship(
      JWTData userData, int siteId, ParentRecordStewardshipRequest parentRecordStewardshipRequest);

  /** Edit features of the given stewardship activity */
  void editStewardship(
      JWTData userData, int activityId, EditStewardshipRequest editStewardshipRequest);

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

  /**
   * Used to upload a new image for a site entry with the given ID. The image must be given in the
   * format of data:image/{extension};base64,{imageData}. For each image, a new row in the
   * `SITE_IMAGES` table will be created. Only users who are Admins, or Super Admins can perform
   * this action.
   */
  void uploadSiteImage(
      JWTData userData, int siteEntryId, UploadSiteImageRequest uploadSiteImageRequest);

  /** Removes the site image with the given image ID */
  void deleteSiteImage(JWTData userData, int imageId);

  /**
   * Retrieves site, adoption, and stewardship information about all sites that match the 6 optional
   * criteria in the given request.
   *
   * <p>The optional criteria are:
   *
   * <ul>
   *   <li>The species of the site's latest site entry is in `treeSpecies`
   *   <li>The site has been adopted by a user on or after `adoptedStart`
   *   <li>The site has been adopted by a user on or before `adoptedEnd`
   *   <li>The site's latest stewardship activity was recorded on or after `lastActivityStart`
   *   <li>The site's latest stewardship activity was recorded on or before `lastActivityEnd`
   *   <li>The site is located in a neighborhood in `neighborhoodIds`
   * </ul>
   */
  List<FilterSitesResponse> filterSites(JWTData userData, FilterSitesRequest filterSitesRequest);

  /** Edits the site entry with the given entryId */
  void editSiteEntry(JWTData userData, int entryId, UpdateSiteRequest editSiteEntryRequest);

  /** Allows Admin users to approve uploaded site images of the given ID */
  void approveSiteImage(JWTData userData, int imageID);
}
