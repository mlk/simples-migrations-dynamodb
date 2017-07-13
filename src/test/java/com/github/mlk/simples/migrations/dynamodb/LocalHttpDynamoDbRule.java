package com.github.mlk.simples.migrations.dynamodb;

import com.amazonaws.services.dynamodbv2.local.main.ServerRunner;
import com.amazonaws.services.dynamodbv2.local.server.DynamoDBProxyServer;
import java.io.IOException;
import java.net.ServerSocket;
import org.junit.rules.ExternalResource;

public class LocalHttpDynamoDbRule extends ExternalResource {
  private DynamoDBProxyServer server;
  private String port;

  @Override
  protected void before() throws Throwable {
    System.setProperty("sqlite4java.library.path", "build/libs");

    port = getAvailablePort();

    System.setProperty("aws.dynamodb.port", port);

    final String[] localArgs = { "-inMemory", "-port", port };
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

  public String getPort() {
    return port;
  }

  private String getAvailablePort() {
    try (final ServerSocket serverSocket = new ServerSocket(0)) {
      return String.valueOf(serverSocket.getLocalPort());
    } catch (IOException e) {
      throw new RuntimeException("Available port was not found", e);
    }
  }
}
