package com.github.mlk.simples.migrations.dynamodb;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.github.mlk.simples.migrations.CheckMigration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;

@Log4j
@Component
public class DynamoCheckMigration implements CheckMigration {

  public static final String MIGRATION_TABLE_NAME = "migrations";
  private final AmazonDynamoDB client;

  public DynamoCheckMigration(AmazonDynamoDB client) {
    this.client = client;
  }

  @Override
  public void setup() {
    List<KeySchemaElement> keySchema = new ArrayList<>();

    AttributeDefinition attributeDefinition = new AttributeDefinition("script", ScalarAttributeType.S);

    keySchema.add(
        new KeySchemaElement()
            .withAttributeName(attributeDefinition.getAttributeName())
            .withKeyType(KeyType.HASH)
    );

    ProvisionedThroughput provisionedThroughput = new ProvisionedThroughput();
    provisionedThroughput.setReadCapacityUnits(10L);
    provisionedThroughput.setWriteCapacityUnits(10L);

    CreateTableRequest request = new CreateTableRequest()
        .withTableName(MIGRATION_TABLE_NAME)
        .withKeySchema(keySchema)
        .withAttributeDefinitions(Collections.singleton(attributeDefinition))
        .withProvisionedThroughput(provisionedThroughput);

    try {
      client.createTable(request);
    } catch (AmazonClientException e) {
      log.debug("yummy exception", e);
    }
  }

  @Override
  public boolean hasRunMigration(String migrationName) {
    GetItemRequest getItemRequest = new GetItemRequest()
        .withKey(Collections.singletonMap("script", new AttributeValue(migrationName)))
        .withTableName("migrations");

    GetItemResult result = client.getItem(getItemRequest);
    return result.getItem() != null;
  }

  @Override
  public void migrated(String migrationName) {
    PutItemRequest putItemRequest = new PutItemRequest()
        .withItem(Collections.singletonMap("script", new AttributeValue(migrationName)))
        .withTableName("migrations");

    client.putItem(putItemRequest);
  }
}
