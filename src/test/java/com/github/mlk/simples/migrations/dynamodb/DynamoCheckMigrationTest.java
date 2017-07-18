package com.github.mlk.simples.migrations.dynamodb;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.DescribeTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableResult;
import com.github.mlk.junit.rules.HttpDynamoDbRule;
import java.util.UUID;
import org.junit.Rule;
import org.junit.Test;



public class DynamoCheckMigrationTest {
  @Rule
  public HttpDynamoDbRule localDynamoDbRole = new HttpDynamoDbRule();

  public AmazonDynamoDB getClient() {
    return AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(
        // we can use any region here
        new AwsClientBuilder.EndpointConfiguration(localDynamoDbRole.getEndpoint(), "us-west-2"))
        .build();
  }

  @Test
  public void whenTableDoesNotExistThenCreateIt() {
    DynamoCheckMigration underTest = new DynamoCheckMigration(getClient());

    underTest.setup();

    try {
      DescribeTableResult migrationsTable = getClient()
          .describeTable(new DescribeTableRequest().withTableName("migrations"));

      assertThat(migrationsTable.getTable().getTableName()).isEqualTo("migrations");
    } catch(AmazonServiceException e) {
      fail("table does not exist");
    }
  }

  @Test
  public void whenTablesDoesExistDontBlowUp() {
    DynamoCheckMigration underTest = new DynamoCheckMigration(getClient());

    underTest.setup();

    underTest.setup();

    try {
      DescribeTableResult migrationsTable = getClient()
          .describeTable(new DescribeTableRequest().withTableName("migrations"));

      assertThat(migrationsTable.getTable().getTableName()).isEqualTo("migrations");
    } catch(AmazonServiceException e) {
      fail("table does not exist");
    }
  }

  @Test
  public void whenMigrationHasNotRunThenReturnFalse() {
    DynamoCheckMigration underTest = new DynamoCheckMigration(getClient());

    underTest.setup();

    assertThat(underTest.hasRunMigration(UUID.randomUUID().toString())).isFalse();
  }


  @Test
  public void whenMigrationHasRunThenReturnTrue() {
    DynamoCheckMigration underTest = new DynamoCheckMigration(getClient());

    underTest.setup();

    underTest.migrated("V1");

    assertThat(underTest.hasRunMigration("V1")).isTrue();
  }
}