package com.github.mlk.simples.migrations.dynamodb.example;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "application")
public class SimplesConfig {

  // This should create either a real one or a test one via @Profile/@ActiveProfile/Configuration magic.
  // But we are only using this in test, so I only worry about the test implementation
  @Bean
  public AmazonDynamoDB create() {

    return AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(
        // we can use any region here
        new AwsClientBuilder.EndpointConfiguration("http://localhost:" + System.getProperty("aws.dynamodb.port"), "us-west-2"))
        .build();

  }
}
