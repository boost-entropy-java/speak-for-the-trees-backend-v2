package com.codeforcommunity.dto.site;

import com.codeforcommunity.dto.ApiDto;
import com.codeforcommunity.exceptions.HandledException;
import java.util.ArrayList;
import java.util.List;

public class UploadSiteImageRequest extends ApiDto {
  private String image;
  private Boolean anonymous;

  public UploadSiteImageRequest(String image, Boolean anonymous) {
    this.image = image;
    this.anonymous = anonymous;
  }

  public UploadSiteImageRequest() {}

  public String getImage() {
    return image;
  }

  public Boolean getAnonymous() {
    return anonymous;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public void setAnon(Boolean anonymous) {
    this.anonymous = anonymous;
  }

  @Override
  public List<String> validateFields(String fieldPrefix) throws HandledException {
    String fieldName = fieldPrefix + "upload_image_request.";
    List<String> fields = new ArrayList<>();

    if (image == null) {
      fields.add(fieldName + "image");
    }
    if (anonymous == null) {
      fields.add(fieldName + "anonymous");
    }

    return fields;
  }
}
