package com.codeforcommunity.requester;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.codeforcommunity.aws.EncodedImage;
import com.codeforcommunity.dto.emailer.LoadTemplateResponse;
import com.codeforcommunity.exceptions.BadRequestHTMLException;
import com.codeforcommunity.exceptions.BadRequestImageException;
import com.codeforcommunity.exceptions.InvalidURLException;
import com.codeforcommunity.exceptions.S3FailedUploadException;
import com.codeforcommunity.propertiesLoader.PropertiesLoader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.jsoup.Jsoup;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Parser;

public class S3Requester {
  // Contains information about S3 that is not part of this class's implementation
  public static class Externs {
    private static final AmazonS3 s3Client =
        AmazonS3ClientBuilder.standard()
            .withRegion(Regions.US_EAST_2)
            .withCredentials(
                new AWSStaticCredentialsProvider(
                    new BasicAWSCredentials(
                        PropertiesLoader.loadProperty("aws_access_key"),
                        PropertiesLoader.loadProperty("aws_secret_key"))))
            .build();

    private static final String BUCKET_PUBLIC_URL =
        PropertiesLoader.loadProperty("aws_s3_bucket_url");
    private static final String BUCKET_PUBLIC = PropertiesLoader.loadProperty("aws_s3_bucket_name");
    private static final String DIR_PUBLIC = PropertiesLoader.loadProperty("aws_s3_upload_dir");

    public AmazonS3 getS3Client() {
      return s3Client;
    }

    public String getBucketPublicUrl() {
      return BUCKET_PUBLIC_URL;
    }

    public String getBucketPublic() {
      return BUCKET_PUBLIC;
    }

    public String getDirPublic() {
      return DIR_PUBLIC;
    }
  }

  private static Externs externs = new Externs();

  private static final String SITE_IMAGES_S3_DIR = "site_images";
  private static final String TEMPLATE_S3_DIR = "email_templates";
  private static final String TEMPLATE_FILE_EXTENSION = "_template.html";

  /**
   * This should only be used for testing purposes when we mock the s3Client.
   *
   * @param customExterns externs with a mocked s3Client
   */
  public static void setExterns(Externs customExterns) {
    externs = customExterns;
  }

  /**
   * Validates whether or not the given String is a base64 encoded image in the following format:
   * data:image/{extension};base64,{imageData}.
   *
   * @param base64Image the potential encoding of the image.
   * @return null if the String is not an encoded base64 image, otherwise an {@link EncodedImage}.
   */
  private static EncodedImage validateBase64Image(String base64Image) {
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
    String encodedImage = base64ImageSplit[1]; // The encoded image

    return new EncodedImage("image", fileExtension, encodedImage);
  }

  /**
   * Validate the given base64 encoding of an image and upload it to the LLB public S3 bucket.
   *
   * @param fileName the desired name of the new file in S3 (without a file extension).
   * @param directoryName the desired directory of the file in S3 (without leading or trailing '/').
   * @param base64Encoding the base64 encoding of the image to upload.
   * @return null if the encoding fails validation and image URL if the upload was successful.
   * @throws BadRequestImageException if the base64 image validation or image decoding failed.
   * @throws S3FailedUploadException if the upload to S3 failed.
   */
  private static String validateBase64ImageAndUploadToS3(
      String fileName, String directoryName, String base64Encoding)
      throws BadRequestImageException, S3FailedUploadException {
    if (base64Encoding == null) {
      // No validation/uploading required if given no data
      return null;
    }

    EncodedImage encodedImage = validateBase64Image(base64Encoding);
    if (encodedImage == null) {
      // Image failed to validate
      throw new BadRequestImageException();
    }

    String fullFileName = String.format("%s.%s", fileName, encodedImage.getFileExtension());
    File tempFile;

    try {
      // Temporarily writes the image to disk to decode
      byte[] imageData = Base64.getDecoder().decode(encodedImage.getBase64ImageEncoding());
      tempFile = File.createTempFile(fullFileName, null, null);
      FileOutputStream fos = new FileOutputStream(tempFile);
      fos.write(imageData);
      fos.flush();
      fos.close();
    } catch (IllegalArgumentException | IOException e) {
      // The image failed to decode
      throw new BadRequestImageException();
    }

    // Create the request to upload the image
    String objectName = String.join("/", directoryName, externs.getDirPublic(), fullFileName);
    PutObjectRequest awsRequest =
        new PutObjectRequest(externs.getBucketPublic(), objectName, tempFile);

    // Set the image file metadata (to be of type image)
    ObjectMetadata awsObjectMetadata = new ObjectMetadata();
    awsObjectMetadata.setContentType(encodedImage.getFileType() + encodedImage.getFileExtension());
    awsRequest.setMetadata(awsObjectMetadata);

    try {
      // Perform the upload to S3
      externs.getS3Client().putObject(awsRequest);
    } catch (SdkClientException e) {
      // The AWS S3 upload failed
      throw new S3FailedUploadException(e.getMessage());
    }

    // Delete the temporary file that was written to disk
    tempFile.delete();

    return String.format("%s/%s", externs.getBucketPublicUrl(), objectName);
  }

  /**
   * Validate the given base64 encoding of an image and upload it to the SFTT public S3 bucket for
   * Site Images.
   *
   * @param imageName the desired name of the new file in S3 (without a file extension).
   * @param base64Encoding the encoded image to upload.
   * @return image URL if the upload was successful.
   * @throws BadRequestImageException if the base64 decoding failed.
   * @throws S3FailedUploadException if the upload to S3 failed.
   */
  public static String uploadSiteImage(String imageName, String base64Encoding)
      throws BadRequestImageException, S3FailedUploadException {
    String fileName = getFileNameWithoutExtension(imageName, "");

    return validateBase64ImageAndUploadToS3(fileName, SITE_IMAGES_S3_DIR, base64Encoding);
  }

  /**
   * Delete the existing site image with the given URL from the user uploads S3 bucket.
   *
   * @param imageUrl the URL of the image file in S3 to delete.
   * @throws SdkClientException if the deletion from S3 failed.
   */
  public static void deleteSiteImage(String imageUrl) {
    // Get just the file path from the full URL
    String imagePath = imageUrl.split(externs.getBucketPublicUrl() + '/')[1];

    DeleteObjectRequest deleteRequest =
        new DeleteObjectRequest(externs.getBucketPublic(), imagePath);

    try {
      externs.getS3Client().deleteObject(deleteRequest);
    } catch (SdkClientException e) {
      // The AWS S3 delete failed
      throw new S3FailedUploadException(e.getMessage());
    }
  }

  /**
   * Load the existing site image with the given URL from the user uploads S3 bucket.
   *
   * @param imageUrl the URL of the image file in S3 to delete.
   * @throws SdkClientException if the load from S3 failed.
   */
  public static S3Object loadS3Image(String imageUrl) {
    String imagePath = imageUrl.split(externs.getBucketPublicUrl() + '/')[1];

    if (!pathExists(imagePath)) {
      throw new InvalidURLException();
    }

    // Create the request to get the HTML
    GetObjectRequest awsRequest = new GetObjectRequest(externs.getBucketPublic(), imagePath);

    S3Object image = externs.getS3Client().getObject(awsRequest);
    return image;
  }

  /**
   * Removes special characters, replaces spaces, and appends a suffix.
   *
   * @param baseTitle the title of the file.
   * @param suffix the suffix to be appended
   * @return the String for the image file name (without the file extension).
   */
  public static String getFileNameWithoutExtension(String baseTitle, String suffix) {
    String title =
        baseTitle.replaceAll("[!@#$%^&*()=+./\\\\|<>`~\\[\\]{}?]", ""); // Remove special characters
    return title.replace(" ", "_").toLowerCase() + suffix; // The desired name of the file in S3
  }

  /**
   * Validate the given string encoding of HTML and upload it to the user upload S3 bucket. HTML
   * will be overwritten in S3 if another file of the same name is uploaded.
   *
   * @param name the desired name of the new file in S3 (without a file extension).
   * @param adminID the desired ID of the user uploading the HTML to S3.
   * @param htmlContent the string encoding of the HTML to upload.
   * @return HTML file URL if the upload was successful.
   * @throws BadRequestHTMLException if the string to HTML decoding failed.
   * @throws S3FailedUploadException if the upload to S3 failed.
   */
  public static String uploadHTML(String name, Integer adminID, String htmlContent) {
    // Save HTML to temp file
    String fullFileName = getFileNameWithoutExtension(name, TEMPLATE_FILE_EXTENSION);
    File tempFile;

    // try to create a clean parse
    Parser parser = Parser.htmlParser().setTrackErrors(10);
    Jsoup.parse(htmlContent, parser);
    ParseErrorList errors = parser.getErrors();
    String errorLog = "";
    if (errors.size() > 0) {
      for (ParseError e : errors) {
        errorLog += e.getErrorMessage() + "\n";
      }
      throw new BadRequestHTMLException(errorLog);
    }

    try {
      // Temporarily writes the string to HTML file on disk
      tempFile = File.createTempFile(fullFileName, null, null);
      FileOutputStream fos = new FileOutputStream(tempFile);
      byte[] bytesArray = htmlContent.getBytes();
      fos.write(bytesArray);
      fos.flush();
      fos.close();
    } catch (IllegalArgumentException | IOException e) {
      // The string failed to decode to HTML
      throw new BadRequestHTMLException("HTML could not be written to disk.");
    }

    // Create the request to upload the HTML
    String objectName = String.join("/", TEMPLATE_S3_DIR, externs.getDirPublic(), fullFileName);
    PutObjectRequest awsRequest =
        new PutObjectRequest(externs.getBucketPublic(), objectName, tempFile);

    // Set the HTML file metadata to have the userID of the uploader
    ObjectMetadata awsObjectMetadata = new ObjectMetadata();
    Map<String, String> userMetadata = new HashMap<>();
    userMetadata.put("userID", Integer.toString(adminID));
    awsObjectMetadata.setUserMetadata(userMetadata);
    awsRequest.setMetadata(awsObjectMetadata);

    try {
      // Perform the upload to S3
      externs.getS3Client().putObject(awsRequest);
    } catch (SdkClientException e) {
      // The AWS S3 upload failed
      throw new S3FailedUploadException(e.getMessage());
    }

    // Delete the temporary file that was written to disk
    tempFile.delete();

    return String.join(
        "/", externs.getBucketPublicUrl(), TEMPLATE_S3_DIR, externs.getDirPublic(), name);
  }

  // helper to check whether the given path exists
  public static boolean pathExists(String path) {
    return externs.getS3Client().doesObjectExist(externs.getBucketPublic(), path);
  }

  /**
   * Load the existing HTML file with the given name from the user uploads S3 bucket.
   *
   * @param name the name of the HTML file in S3 to be loaded (without "_template.html extension).
   * @return LoadTemplateResponse with the html file name, content, and author.
   * @throws InvalidURLException if the file does not exist.
   * @throws BadRequestHTMLException if the HTML file cannot be decoded to a string.
   * @throws SdkClientException if the loading from S3 failed.
   */
  public static LoadTemplateResponse loadHTML(String name) {
    String htmlPath =
        String.join("/", TEMPLATE_S3_DIR, externs.getDirPublic(), name + TEMPLATE_FILE_EXTENSION);

    if (!pathExists(htmlPath)) {
      throw new InvalidURLException();
    }

    // Create the request to get the HTML
    GetObjectRequest awsRequest = new GetObjectRequest(externs.getBucketPublic(), htmlPath);

    S3Object HTMLFile = externs.getS3Client().getObject(awsRequest);
    StringBuilder content = new StringBuilder();
    try {
      int c = 0;
      while ((c = HTMLFile.getObjectContent().read()) != -1) {
        content.append((char) c);
      }
    } catch (IOException e) {
      throw new BadRequestHTMLException("HTML file could not be decoded to string");
    }

    String HTMLContent = content.toString();

    String htmlAuthor = HTMLFile.getObjectMetadata().getUserMetaDataOf("userID");

    return new LoadTemplateResponse(HTMLContent, HTMLFile.getKey(), htmlAuthor);
  }

  /**
   * Delete the existing HTML file with the given name from the user uploads S3 bucket.
   *
   * @param name the name of the HTML file in S3 to be deleted.
   * @throws InvalidURLException if the file does not exist.
   * @throws SdkClientException if the deletion from S3 failed.
   */
  public static void deleteHTML(String name) {
    String htmlPath =
        String.join("/", TEMPLATE_S3_DIR, externs.getDirPublic(), name + TEMPLATE_FILE_EXTENSION);

    if (!pathExists(htmlPath)) {
      throw new InvalidURLException();
    }

    // Create the request to delete the HTML
    DeleteObjectRequest awsRequest = new DeleteObjectRequest(externs.getBucketPublic(), htmlPath);

    externs.getS3Client().deleteObject(awsRequest);
  }

  /**
   * @param bucketName
   * @return
   */
  public static List<String> getAllNamesinBucket(String bucketName) {
    ListObjectsV2Request req = new ListObjectsV2Request();
    req.setBucketName(bucketName);
    req.setDelimiter("/");
    String path = String.join("/", TEMPLATE_S3_DIR, externs.getDirPublic() + "/");
    req.setPrefix(path);
    req.setStartAfter(path);

    ListObjectsV2Result res;
    try {

      res = externs.getS3Client().listObjectsV2(req);
      String prefix = req.getPrefix();
      List<String> result;
      result =
          res.getObjectSummaries().stream()
              .map(s -> s.getKey().replace(prefix, "").replace("_template.html", ""))
              .collect(Collectors.toList());
      return result;
    } catch (SdkClientException a) {
      throw new InvalidURLException();
    }
  }
}
