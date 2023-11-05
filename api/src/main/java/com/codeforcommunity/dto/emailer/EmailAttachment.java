package com.codeforcommunity.dto.emailer;

import javax.mail.util.ByteArrayDataSource;

public class EmailAttachment {
    private final String name;
    private final ByteArrayDataSource data;


    public EmailAttachment(String name, ByteArrayDataSource data) {
        this.name = name;
        this.data = data;

    }

    public String getName() {
        return name;
    }
    public ByteArrayDataSource getData() {
        return data;
    }
}
