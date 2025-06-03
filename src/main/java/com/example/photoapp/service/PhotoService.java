package com.example.photoapp.service;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.HashMap;
import java.util.UUID;

@Service
public class PhotoService {
    private final S3Client s3Client;
    private final DynamoDbClient dynamoDbClient;
    private final SqsClient sqsClient;
    private final SnsClient snsClient;
    private final String bucketName = "photo-app-uploads-<your-name>";
    private final String tableName = "PhotoMetadata";
    private final String queueUrl = "<your-sqs-queue-url>";
    private final String topicArn = "<your-sns-topic-arn>";

    public PhotoService(S3Client s3Client, DynamoDbClient dynamoDbClient,
                        SqsClient sqsClient, SnsClient snsClient) {
        this.s3Client = s3Client;
        this.dynamoDbClient = dynamoDbClient;
        this.sqsClient = sqsClient;
        this.snsClient = snsClient;
    }

    public String uploadPhoto(String userId, String fileName, byte[] fileContent) {
        String photoId = UUID.randomUUID().toString();

        // Upload to S3
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(photoId + "-" + fileName)
                .build();
        s3Client.putObject(putObjectRequest,
                software.amazon.awssdk.core.sync.RequestBody.fromBytes(fileContent));

        // Save metadata to DynamoDB
        HashMap<String, AttributeValue> item = new HashMap<>();
        item.put("photoId", AttributeValue.builder().s(photoId).build());
        item.put("userId", AttributeValue.builder().s(userId).build());
        item.put("uploadTime", AttributeValue.builder().s(Instant.now().toString()).build());
        item.put("fileName", AttributeValue.builder().s(fileName).build());
        dynamoDbClient.putItem(builder -> builder.tableName(tableName).item(item));

        // Send message to SQS
        String messageBody = String.format("Photo uploaded: %s by user: %s", photoId, userId);
        SendMessageRequest sqsRequest = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(messageBody)
                .build();
        sqsClient.sendMessage(sqsRequest);

        // Publish to SNS
        PublishRequest snsRequest = PublishRequest.builder()
                .topicArn(topicArn)
                .message("New photo uploaded: " + fileName)
                .build();
        snsClient.publish(snsRequest);

        return photoId;
    }
}