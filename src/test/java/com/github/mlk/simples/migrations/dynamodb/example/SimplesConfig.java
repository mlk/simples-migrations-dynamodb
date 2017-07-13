package com.github.mlk.simples.migrations.dynamodb.example;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "application")
public class SimplesConfig {

  private String endpoint;

  public void setEndpoint(String endpoint) {
    this.endpoint = endpoint;
  }

  public String getEndpoint() {
    return endpoint;
  }

  @Bean
  public AmazonDynamoDB create() {

    return AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(
        // we can use any region here
        new AwsClientBuilder.EndpointConfiguration(getEndpoint(), "us-west-2"))
        .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials("x", "x")))
        .build();

  }
}
