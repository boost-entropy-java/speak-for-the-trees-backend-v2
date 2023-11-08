package com.codeforcommunity.dto.emailer;

import com.codeforcommunity.aws.EncodedImage;
import com.codeforcommunity.dto.ApiDto;
import com.codeforcommunity.exceptions.BadRequestImageException;
import com.codeforcommunity.exceptions.HandledException;

import org.simplejavamail.api.email.AttachmentResource;

import javax.mail.util.ByteArrayDataSource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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

    private static String getFileExtension(String base64Image) {
        // Expected Base64 format: "data:image/{extension};base64,{imageData}"

        if (base64Image == null || base64Image.length() < 10) {
            return null;
        }

        String[] base64ImageSplit = base64Image.split(",", 2); // Split the metadata from the image data
        if (base64ImageSplit.length != 2) {
            return null;
        }

        String meta = base64ImageSplit[0]; // The image metadata (e.g. "data:image/png;base64")
        String[] metaSplit = meta.split(";", 2); // Split the metadata into data type and encoding type

        if (metaSplit.length != 2 || !metaSplit[1].equals("base64")) {
            // Ensure the encoding type is base64
            return null;
        }

        String[] dataSplit = metaSplit[0].split(":", 2); // Split the data type
        if (dataSplit.length != 2) {
            return null;
        }

        String[] data = dataSplit[1].split("/", 2); // Split the image type here (e.g. "image/png")
        if (data.length != 2 || !data[0].equals("image")) {
            // Ensure the encoded data is an image
            return null;
        }

        String fileExtension = data[1]; // The image type (e.g. "png")
        return fileExtension;
    }
    private static ByteArrayDataSource stringToDataSource(String data) {
        byte[] imageData;
        String mimeType;

        try {
            // Temporarily writes the image to disk to decode
            mimeType = getFileExtension(data);
            System.out.println(mimeType);
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
