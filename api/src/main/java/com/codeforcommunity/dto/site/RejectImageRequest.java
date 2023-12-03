package com.codeforcommunity.dto.site;

import com.codeforcommunity.dto.ApiDto;
import java.util.ArrayList;
import java.util.List;

public class RejectImageRequest extends ApiDto {
    private String reason;

    public RejectImageRequest(String reason) {
        this.reason = reason;
    }

    private RejectImageRequest() {}

    public String getReason() {
        return reason;
    }

    public void setNewReason(String newEmail) {
        this.reason = reason;
    }

    @Override
    public List<String> validateFields(String fieldPrefix) {
        List<String> fields = new ArrayList<>();
        return fields;
    }
}
