package aws;


import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import utils.FileKeeper;
import utils.FileSize;
import utils.Logger;


public class BucketManager {

    private static final String BUCKET_DEF_PREFIX = "fminetworkmeter";
    private static final Regions DEFAULT_REGION = Regions.US_EAST_1;

    public static String createBucket() {

        AmazonS3 s3client = AmazonS3ClientBuilder
                .standard()
                .withRegion(DEFAULT_REGION)
                .build();

        String bucketName = BUCKET_DEF_PREFIX + new Random().nextInt(1000);

        if (s3client.doesBucketExistV2(bucketName)) {
            Logger.log("Bucket name is not available.");
            System.exit(0);
        }

        s3client.createBucket(bucketName);
        Logger.log("Bucket " + bucketName + " successfully cretaed");

        List<Bucket> buckets = s3client.listBuckets();
//        for (Bucket bucket : buckets) {
//            Logger.log(bucket.getName());
//        }

        return bucketName;

    }

    public static void deleteBucket(String bucketName) {
        AmazonS3 s3Client = AmazonS3ClientBuilder
                .standard()
                .withRegion(DEFAULT_REGION)
                .build();

        try {
            Logger.log("Removing objects from bucket");
            ObjectListing object_listing = s3Client.listObjects(bucketName);
            while (true) {
                for (S3ObjectSummary summary : object_listing.getObjectSummaries()) {
                    s3Client.deleteObject(bucketName, summary.getKey());
                }

                if (object_listing.isTruncated()) {
                    object_listing = s3Client.listNextBatchOfObjects(object_listing);
                } else {
                    break;
                }
            }
            ;
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        }

        try {
            s3Client.deleteBucket(bucketName);

            Logger.log("Bucket " + bucketName + " successfully deleted");
        } catch (AmazonServiceException e) {
            Logger.log("Bucket " + bucketName + " NOT deleted");
            System.err.println(e.getErrorMessage());
        }
    }


    public static void main(String[] args) {
        String bucketName = createBucket();
        uploadFileToBucket(bucketName, FileKeeper.getFile(FileSize.MB_1));
        deleteBucket(bucketName);
    }


    public static long uploadFileToBucket(String bucketName, File file) {
        String fileObjKeyName = file.getName() + new Random().nextInt(1000);

        long startTime = System.currentTimeMillis();

        try {
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withRegion(DEFAULT_REGION)
                    .withCredentials(new ProfileCredentialsProvider())
                    .build();

            PutObjectRequest request = new PutObjectRequest(bucketName,
                    fileObjKeyName,
                    file);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("plain/text");
            request.setMetadata(metadata);
            s3Client.putObject(request);
        } catch (SdkClientException e) {
            Logger.log("File not uploaded");
            e.printStackTrace();
        }

        return System.currentTimeMillis() - startTime;
    }

}
