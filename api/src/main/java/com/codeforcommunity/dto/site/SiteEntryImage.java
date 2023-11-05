package com.codeforcommunity.dto.site;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.sql.Timestamp;

public class SiteEntryImage {
  private final Integer imageId;

  private final String uploaderUsername;

  private final Integer uploaderId;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
  private final Timestamp uploadedAt;

  private final String imageUrl;

  public SiteEntryImage(String imageUrl) {
    this(null, null, null, null, imageUrl);
  }

  public SiteEntryImage(
      Integer imageId, String uploaderUsername, Integer uploaderId, Timestamp uploadedAt, String imageUrl) {
    this.imageId = imageId;
    this.uploaderUsername = uploaderUsername;
    this.uploaderId = uploaderId;
    this.uploadedAt = uploadedAt;
    this.imageUrl = imageUrl;
  }

  public Integer getImageId() {
    return imageId;
  }

  public String getUploaderUsername() {
    return uploaderUsername;
  }

  public Integer getUploaderId() {
    return uploaderId;
  }

  public Timestamp getUploadedAt() {
    return uploadedAt;
  }

  public String getImageUrl() {
    return imageUrl;
  }
}
