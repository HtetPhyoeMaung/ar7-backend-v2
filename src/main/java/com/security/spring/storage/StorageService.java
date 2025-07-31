package com.security.spring.storage;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;

import com.security.spring.utils.UUIDGenerator;
import org.springframework.stereotype.Service;
import com.amazonaws.services.s3.AmazonS3;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;



import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;


@Service
public class StorageService {
    private final AmazonS3 space;

    private static final String BUCKET_NAME = "ar7imageserversit";

    public StorageService() {
        // Create the AWS credentials provider
        AWSCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider(
                new BasicAWSCredentials("DO00B3NREZ8TTDVT8Y77", "qnooagiCt6cgttIJVvjt919IJfmY67GIuAA2hsYXvvw")
        );

        // Initialize the Amazon S3 client
        space = AmazonS3ClientBuilder
                .standard()
                .withCredentials(awsCredentialsProvider)
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration("sgp1.digitaloceanspaces.com", "sgp1")
                )
                .build();
    }
    public BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(resultingImage, 0, 0, null);
        g2d.dispose();
        return outputImage;
    }
    public String uploadImage(MultipartFile file) throws IOException {
        // Generate unique file name
        String filename = UUIDGenerator.generateUUID() + file.getOriginalFilename();
        // Convert MultipartFile to BufferedImage
        BufferedImage inputImage = ImageIO.read(file.getInputStream());
        // Resize image (you can specify target width and height here)
        int targetWidth = 500;
        int targetHeight = 300;
        BufferedImage resizedImage = resizeImage(inputImage, targetWidth, targetHeight);
        // Convert resized BufferedImage back to InputStream
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(resizedImage, "jpg", baos);
        byte[] resizedBytes = baos.toByteArray();
        ByteArrayInputStream resizedInputStream = new ByteArrayInputStream(resizedBytes);
        // Create object metadata
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(resizedBytes.length);
        // Upload to DigitalOcean Spaces
        space.putObject(new PutObjectRequest(BUCKET_NAME, filename, resizedInputStream, objectMetadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));

        return filename;
    }

    //hello
    public String uploadFile(byte[] fileData, String fileName, String contentType) throws IOException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(contentType);
        objectMetadata.setContentLength(fileData.length);

        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(fileData)) {
            space.putObject(new PutObjectRequest(BUCKET_NAME, fileName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        }

        // Return the URL of the uploaded file
        return space.getUrl(BUCKET_NAME, fileName).toString();
    }


    public String  getImageByName(String imageName) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(BUCKET_NAME, imageName);

        return space.generatePresignedUrl(generatePresignedUrlRequest).toString();
    }

    public String  updateImage(MultipartFile file, String filename) throws IOException {

        // Check if the file exists in the bucket
        try {
            // Try to retrieve the existing object
            S3Object existS3Object = space.getObject("digizenger-image", filename);

            // If object exists, delete it
            if (existS3Object != null) {
                space.deleteObject(BUCKET_NAME, filename);
            }
        } catch (AmazonS3Exception e) {
            // Handle the case where the file does not exist
            if (e.getStatusCode() == 404) {
                System.out.println("File does not exist, proceeding with upload.");
            } else {
                throw new IOException("Failed to check existence of the file in S3: " + e.getMessage());
            }
        }
        String imageName = UUIDGenerator.generateUUID()+file.getOriginalFilename();
        // Upload the new file with the same name
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());
        space.putObject(new PutObjectRequest(BUCKET_NAME, imageName, file.getInputStream(), objectMetadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return imageName;

    }

    public void deleteImage(String imageName) {
        // Deletes the specified image from the "digizenger-image" bucket
        space.deleteObject(BUCKET_NAME, imageName);
    }
}