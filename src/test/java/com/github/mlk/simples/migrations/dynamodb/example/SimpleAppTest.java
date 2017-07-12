package com.github.mlk.simples.migrations.dynamodb.example;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.Assert.fail;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.model.DescribeTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableResult;
import com.github.mlk.simples.migrations.dynamodb.AlternatorDBRule;
import org.assertj.core.api.Assertions;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.ManagementServerProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "management.port=0")
public class SimpleAppTest {
  private RestOperations restOperations = new RestTemplate();

  @Autowired
  ManagementServerProperties managementServerProperties;

  @Autowired
  Environment environment;

  // The Spring context gets created once for all the tests, so we need to have just a single
  // fake DynamoDB for all the tests
  @ClassRule
  public static AlternatorDBRule alternatorDBRule = new AlternatorDBRule();

  public String getManagementPath() {
    final int managementPort = environment.getProperty("local.management.port", Integer.class);
    final String managementPath = managementServerProperties.getContextPath();

    return "http://localhost:" + managementPort + managementPath;
  }

  @Test
  public void healthCheck() throws Exception {
    assertThat(restOperations
        .getForEntity(UriComponentsBuilder
                .fromHttpUrl(getManagementPath() + "/health")
                .build()
                .toUri(),
            String.class).getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  public void shouldHaveCreatedThePeopleTable() {
    // NOTE: The magic happens as part of Spring startup

    try {
      DescribeTableResult migrationsTable = alternatorDBRule.getClient()
          .describeTable(new DescribeTableRequest().withTableName("people"));

      Assertions.assertThat(migrationsTable.getTable().getTableName()).isEqualTo("people");
    } catch(AmazonServiceException e) {
      fail("table does not exist");
    }
  }
}
