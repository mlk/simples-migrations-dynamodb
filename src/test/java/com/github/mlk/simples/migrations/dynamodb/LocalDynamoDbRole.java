package com.github.mlk.simples.migrations.dynamodb;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.local.embedded.DynamoDBEmbedded;
import org.junit.rules.ExternalResource;

public class LocalDynamoDbRole extends ExternalResource {

  private AmazonDynamoDB client;

  public AmazonDynamoDB getClient() {
    return client;
  }

  @Override
  protected void before() throws Throwable {
    System.setProperty("sqlite4java.library.path", "build/libs");
    client = DynamoDBEmbedded.create().amazonDynamoDB();
  }

  @Override
  protected void after() {
    try {
      client.shutdown();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
