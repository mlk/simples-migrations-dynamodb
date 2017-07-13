package com.github.mlk.simples.migrations.dynamodb;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.model.DescribeTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableResult;
import java.util.UUID;
import org.junit.Rule;
import org.junit.Test;



public class DynamoCheckMigrationTest {
  @Rule
  public LocalDynamoDbRole localDynamoDbRole = new LocalDynamoDbRole();

  @Test
  public void whenTableDoesNotExistThenCreateIt() {
    DynamoCheckMigration underTest = new DynamoCheckMigration(localDynamoDbRole.getClient());

    underTest.setup();

    try {
      DescribeTableResult migrationsTable = localDynamoDbRole.getClient()
          .describeTable(new DescribeTableRequest().withTableName("migrations"));

      assertThat(migrationsTable.getTable().getTableName()).isEqualTo("migrations");
    } catch(AmazonServiceException e) {
      fail("table does not exist");
    }
  }

  @Test
  public void whenTablesDoesExistDontBlowUp() {
    DynamoCheckMigration underTest = new DynamoCheckMigration(localDynamoDbRole.getClient());

    underTest.setup();

    underTest.setup();

    try {
      DescribeTableResult migrationsTable = localDynamoDbRole.getClient()
          .describeTable(new DescribeTableRequest().withTableName("migrations"));

      assertThat(migrationsTable.getTable().getTableName()).isEqualTo("migrations");
    } catch(AmazonServiceException e) {
      fail("table does not exist");
    }
  }

  @Test
  public void whenMigrationHasNotRunThenReturnFalse() {
    DynamoCheckMigration underTest = new DynamoCheckMigration(localDynamoDbRole.getClient());

    underTest.setup();

    assertThat(underTest.hasRunMigration(UUID.randomUUID().toString())).isFalse();
  }


  @Test
  public void whenMigrationHasRunThenReturnTrue() {
    DynamoCheckMigration underTest = new DynamoCheckMigration(localDynamoDbRole.getClient());

    underTest.setup();

    underTest.migrated("V1");

    assertThat(underTest.hasRunMigration("V1")).isTrue();
  }
}