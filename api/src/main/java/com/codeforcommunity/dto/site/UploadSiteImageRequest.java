package com.codeforcommunity.dto.site;

import com.codeforcommunity.dto.ApiDto;
import com.codeforcommunity.exceptions.HandledException;

import java.util.ArrayList;
import java.util.List;

public class UploadSiteImageRequest extends ApiDto {
  private String image;

  public UploadSiteImageRequest(String image) {
    this.image = image;
  }

  public UploadSiteImageRequest() {}

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  @Override
  public List<String> validateFields(String fieldPrefix) throws HandledException {
    String fieldName = fieldPrefix + "upload_image_request.";
    List<String> fields = new ArrayList<>();

    if (image != null && urlInvalid(image)) {
      fields.add(fieldName + "image");
    }

    return fields;
  }
}
