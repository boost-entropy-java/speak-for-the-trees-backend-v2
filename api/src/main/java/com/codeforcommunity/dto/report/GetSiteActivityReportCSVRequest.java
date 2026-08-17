package com.codeforcommunity.dto.report;

import com.codeforcommunity.dto.ApiDto;
import com.codeforcommunity.exceptions.HandledException;
import java.util.ArrayList;
import java.util.List;

public class GetSiteActivityReportCSVRequest extends ApiDto {
  private Long previousDays;
  private Integer siteId;

  public GetSiteActivityReportCSVRequest(Long previousDays, Integer siteId) {
    this.previousDays = previousDays;
    this.siteId = siteId;
  }

  private GetSiteActivityReportCSVRequest() {}

  public Long getPreviousDays() {
    return previousDays;
  }

  public void setPreviousDays(Long previousDays) {
    this.previousDays = previousDays;
  }

  public Integer getSiteId() {
    return siteId;
  }

  public void setSiteId(Integer siteId) {
    this.siteId = siteId;
  }

  @Override
  public List<String> validateFields(String fieldPrefix) throws HandledException {
    return new ArrayList<>();
  }
}
