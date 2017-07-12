package com.github.mlk.simples.migrations.dynamodb.example.migrations;

import static java.util.Collections.singleton;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.github.mlk.simples.migrations.Migration;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class V1CreatePeopleTable implements Migration {

  private final AmazonDynamoDB client;

  public V1CreatePeopleTable(AmazonDynamoDB client) {
    this.client = client;
  }

  @Override
  public void performMigration() {
    AttributeDefinition id = new AttributeDefinition("id", ScalarAttributeType.N);
    List<KeySchemaElement> keySchema = new ArrayList<>();

    keySchema.add(
        new KeySchemaElement()
            .withAttributeName(id.getAttributeName())
            .withKeyType(KeyType.HASH)
    );

    ProvisionedThroughput provisionedThroughput = new ProvisionedThroughput();
    provisionedThroughput.setReadCapacityUnits(10L);
    provisionedThroughput.setWriteCapacityUnits(10L);

    CreateTableRequest request = new CreateTableRequest()
        .withTableName("people")
        .withKeySchema(keySchema)
        .withAttributeDefinitions(singleton(id))
        .withProvisionedThroughput(provisionedThroughput);

    client.createTable(request);
  }
}
