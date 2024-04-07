package com.codeforcommunity.dto.emailer;

import com.codeforcommunity.dto.ApiDto;
import com.codeforcommunity.exceptions.BadRequestImageException;
import com.codeforcommunity.exceptions.HandledException;
import com.codeforcommunity.exceptions.MalformedParameterException;

import org.simplejavamail.api.email.AttachmentResource;

import javax.mail.util.ByteArrayDataSource;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class EmailAttachment extends ApiDto {
    private String name;
    private String data;

    public EmailAttachment(String name, String data) {
        this.name = name;
        this.data = data;
    }

    private EmailAttachment() {}

    private static String getFileMimeType(String base64File) {
        if (base64File == null || base64File.length() < 10) {
            return null;
        }

        String[] base64ImageSplit = base64File.split(",", 2);
        if (base64ImageSplit.length != 2) {
            return null;
        }

        String meta = base64ImageSplit[0];
        String[] metaSplit = meta.split(";", 2);

        if (metaSplit.length != 2 || !metaSplit[1].equals("base64")) {
            return null;
        }

        String[] dataSplit = metaSplit[0].split(":", 2);

        if (dataSplit.length != 2) {
            return null;
        }

        String fileExtension = dataSplit[1];
        return fileExtension;
    }
    private static ByteArrayDataSource stringToDataSource(String data) {
        byte[] imageData;
        String mimeType;

        try {
            mimeType = getFileMimeType(data);
            if (mimeType==null) {
                throw new MalformedParameterException("data");
            }

            String[] base64Split = data.split(",", 2);
            imageData = Base64.getDecoder().decode(base64Split[1]);

        } catch (IllegalArgumentException e) {
            // The image failed to decode
            throw new BadRequestImageException();
        }
        return new ByteArrayDataSource(imageData, mimeType);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) { this.name = name; }

    public String getData() {
        return data;
    }

    public void setData(String data) { this.data = data; }

    public AttachmentResource getAttachmentResource() {
        return new AttachmentResource(name, EmailAttachment.stringToDataSource(data));
    }

    @Override
    public List<String> validateFields(String fieldPrefix) throws HandledException {
        String fieldName = fieldPrefix + "email_attachment";
        List<String> fields = new ArrayList<>();

        if (this.name == null) {
            fields.add(fieldName + "name");
        }
        if (this.data == null) {
            fields.add(fieldName + "data");
        }

        return fields;
    }
}
