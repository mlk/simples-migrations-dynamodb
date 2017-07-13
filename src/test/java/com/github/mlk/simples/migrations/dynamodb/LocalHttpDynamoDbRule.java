package com.github.mlk.simples.migrations.dynamodb;

import com.amazonaws.services.dynamodbv2.local.main.ServerRunner;
import com.amazonaws.services.dynamodbv2.local.server.DynamoDBProxyServer;
import org.junit.rules.ExternalResource;

public class LocalHttpDynamoDbRule extends ExternalResource {
  private DynamoDBProxyServer server;

  @Override
  protected void before() throws Throwable {
    System.setProperty("sqlite4java.library.path", "build/libs");

    final String[] localArgs = { "-inMemory" };
    server = ServerRunner.createServerFromCommandLineArgs(localArgs);
    server.start();
  }

  @Override
  protected void after() {
    try {
      server.stop();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
