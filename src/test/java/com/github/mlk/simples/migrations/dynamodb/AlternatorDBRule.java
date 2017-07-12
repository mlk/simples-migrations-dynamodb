package com.github.mlk.simples.migrations.dynamodb;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.michelboudreau.alternator.AlternatorDB;
import com.michelboudreau.alternatorv2.AlternatorDBClientV2;
import org.junit.rules.ExternalResource;

public class AlternatorDBRule extends ExternalResource {

  private AlternatorDB db;
  private AmazonDynamoDB client;

  public AmazonDynamoDB getClient() {
    return client;
  }

  @Override
  protected void before() throws Throwable {
    db = new AlternatorDB().start();
    client = new AlternatorDBClientV2();
  }

  @Override
  protected void after() {
    try {
      db.stop();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
