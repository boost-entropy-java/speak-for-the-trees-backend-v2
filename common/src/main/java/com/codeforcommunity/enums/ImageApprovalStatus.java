package com.codeforcommunity.enums;

public enum ImageApprovalStatus {
  SUBMITTED("SUBMITTED"),
  APPROVED("APPROVED");

  private final String approvalStatus;

  ImageApprovalStatus(String approvalStatus) {
    this.approvalStatus = approvalStatus;
  }

  public String getApprovalStatus() {
    return approvalStatus;
  }

  public static ImageApprovalStatus from(String approvalStatus) {
    for (ImageApprovalStatus imageApprovalStatus : ImageApprovalStatus.values()) {
      if (imageApprovalStatus.approvalStatus.equalsIgnoreCase(approvalStatus)) {
        return imageApprovalStatus;
      }
    }

    throw new IllegalArgumentException(
        String.format(
            "Given approvalStatus `%s` doesn't correspond to any `ImageApprovalStatus`",
            approvalStatus));
  }
}
