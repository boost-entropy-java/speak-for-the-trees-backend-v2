package com.codeforcommunity.dto.emailer;

import com.codeforcommunity.aws.EncodedImage;
import com.codeforcommunity.exceptions.BadRequestImageException;
import org.simplejavamail.api.email.AttachmentResource;

import javax.mail.util.ByteArrayDataSource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

public class EmailAttachment extends AttachmentResource {


    public EmailAttachment(String name, ByteArrayDataSource data) {
        super(name, data);
    }

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
    private static ByteArrayDataSource StringToDataSource(String data) {
        byte[] imageData;
        String mimeType;

        try {
            // Temporarily writes the image to disk to decode
            mimeType = getFileExtension(data);
            imageData = Base64.getDecoder().decode(data);

        } catch (IllegalArgumentException e) {
            // The image failed to decode
            throw new BadRequestImageException();
        }
        return new ByteArrayDataSource(imageData, mimeType);
    }
}
