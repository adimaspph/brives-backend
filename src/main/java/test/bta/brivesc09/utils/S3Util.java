package test.bta.brivesc09.utils;

import java.io.IOException;
import java.io.InputStream;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import org.springframework.web.multipart.MultipartFile;

public class S3Util {
    private static final String BUCKET = "brives";
    private static final String REGION = "sgp1";
    private static final String ENDPOINT = "sgp1.digitaloceanspaces.com";
    private static final String ACCESSKEYID = "BU34NWA6ML2A4YL5QNZL";
    private static final String SECRETACCESSKEY = "UKeNuGQ/4nqgSU49C70R2iXZ03TfnwpdylzrzcMYbD0";

    public static String uploadFile(String fileName, MultipartFile file)
            throws IOException {
        BasicAWSCredentials creds = new BasicAWSCredentials(ACCESSKEYID, SECRETACCESSKEY);
        AmazonS3 client = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new EndpointConfiguration(ENDPOINT, REGION))
                .withCredentials(new AWSStaticCredentialsProvider(creds)).build();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getInputStream().available());
        if (file.getContentType() != null && !"".equals(file.getContentType())) {
            metadata.setContentType(file.getContentType());
        }

        client.putObject(new PutObjectRequest(BUCKET, fileName, file.getInputStream(), metadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return client.getUrl(BUCKET, fileName).toString();
    }
}
