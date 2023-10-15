package com.codeforcommunity.dto.site;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;

public class SiteEntryImage {
  private final Integer imageId;
  private final String uploaderUsername;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
  private final Timestamp uploadedAt;

  private final String imageUrl;

  public SiteEntryImage(String imageUrl) {
    this(null, null, null, imageUrl);
  }

  public SiteEntryImage(Integer imageId, String uploaderUsername, Timestamp uploadedAt, String imageUrl) {
    this.imageId = imageId;
    this.uploaderUsername = uploaderUsername;
    this.uploadedAt = uploadedAt;
    this.imageUrl = imageUrl;
  }

  public Integer getImageId() {
    return imageId;
  }

  public String getUploaderUsername() {
    return uploaderUsername;
  }

  public Timestamp getUploadedAt() {
    return uploadedAt;
  }

  public String getImageUrl() {
    return imageUrl;
  }
}
