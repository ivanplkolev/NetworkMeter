package aws;


import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import utils.FileKeeper;
import utils.FileSize;
import utils.Logger;


public class BucketManager {

    public static void createBucket() {
        AWSCredentials credentials = new BasicAWSCredentials( "", "");

        AmazonS3 s3client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.US_EAST_1)
                .build();

        String bucketName = "ivankolevdronsamolethelikopter";

        if (s3client.doesBucketExistV2(bucketName)) {
            Logger.log("Bucket name is not available.");
            System.exit(0);
        }

        s3client.createBucket(bucketName);


        List<Bucket> buckets = s3client.listBuckets();
        for (Bucket bucket : buckets) {
            Logger.log(bucket.getName());
        }


//        try {
//            s3client.deleteBucket("baeldung-bucket-test2");
//        } catch (AmazonServiceException e) {
//            System.err.println(e.getErrorMessage());
//            return;
//        }

    }


    public static void main(String[] args) throws IOException {
        String bucketName = "cf-templates-87meullpyfhw-us-east-1";
        uploadFileToBucket(bucketName, FileKeeper.getFile(FileSize.MB_10));
    }


    public static long uploadFileToBucket(String bucketName, File file) {
        String clientRegion = "us-east-1";
        String fileObjKeyName = file.getName()+ new Random().nextInt(1000);

        long startTime = System.currentTimeMillis();

        try {
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withRegion(clientRegion)
                    .withCredentials(new ProfileCredentialsProvider())
                    .build();

            PutObjectRequest request = new PutObjectRequest(bucketName,
                    fileObjKeyName,
                    file);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("plain/text");
            metadata.addUserMetadata("x-amz-meta-title", "someTitle");
            request.setMetadata(metadata);
            s3Client.putObject(request);
        } catch (AmazonServiceException e) {
            e.printStackTrace();
        } catch (SdkClientException e) {
            e.printStackTrace();
        }

        return System.currentTimeMillis() - startTime;
    }

}
