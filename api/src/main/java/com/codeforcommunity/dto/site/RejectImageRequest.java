package com.codeforcommunity.dto.site;

import com.codeforcommunity.dto.ApiDto;
import java.util.ArrayList;
import java.util.List;

public class RejectImageRequest extends ApiDto {
  private String rejectionReason;

  public RejectImageRequest(String rejectionReason) {
    this.rejectionReason = rejectionReason;
  }

  private RejectImageRequest() {}

  public String getRejectionReason() {
    return rejectionReason;
  }

  public void setNewReason(String newEmail) {
    this.rejectionReason = rejectionReason;
  }

  @Override
  public List<String> validateFields(String fieldPrefix) {
    List<String> fields = new ArrayList<>();
    return fields;
  }
}
