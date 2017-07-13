package com.github.mlk.simples.migrations.dynamodb.example;

import static org.junit.Assert.fail;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.DescribeTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableResult;
import com.github.mlk.junit.rules.HttpDynamoDbRule;
import org.assertj.core.api.Assertions;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class SimpleAppWebEnvironmentNoneTest {

  // The Spring context gets created once for all the tests, so we need to have just a single
  // fake DynamoDB for all the tests
  @ClassRule
  public static HttpDynamoDbRule localDynamoDbRole = new HttpDynamoDbRule() {
    @Override
    protected void before() throws Throwable {
      super.before();
      System.setProperty("application.endpoint", getEndpoint());
    }
  };

  @Autowired
  AmazonDynamoDBClient client;

  @Test
  public void shouldHaveCreatedThePeopleTable() {
    // NOTE: The magic happens as part of Spring startup

    try {
      DescribeTableResult migrationsTable = client
          .describeTable(new DescribeTableRequest().withTableName("people"));

      Assertions.assertThat(migrationsTable.getTable().getTableName()).isEqualTo("people");
    } catch(AmazonServiceException e) {
      fail("table does not exist");
    }
  }
}
