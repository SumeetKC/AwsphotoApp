package com.example.photoapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sns.SnsClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PhotoappApplication {
	public static void main(String[] args) {
		SpringApplication.run(PhotoappApplication.class, args);
	}

	@Bean
	public S3Client s3Client() {
		return S3Client.builder()
				.region(Region.US_EAST_1)
				.credentialsProvider(DefaultCredentialsProvider.create())
				.build();
	}

	@Bean
	public DynamoDbClient dynamoDbClient() {
		return DynamoDbClient.builder()
				.region(Region.US_EAST_1)
				.credentialsProvider(DefaultCredentialsProvider.create())
				.build();
	}

	@Bean
	public SqsClient sqsClient() {
		return SqsClient.builder()
				.region(Region.US_EAST_1)
				.credentialsProvider(DefaultCredentialsProvider.create())
				.build();
	}

	@Bean
	public SnsClient snsClient() {
		return SnsClient.builder()
				.region(Region.US_EAST_1)
				.credentialsProvider(DefaultCredentialsProvider.create())
				.build();
	}
}