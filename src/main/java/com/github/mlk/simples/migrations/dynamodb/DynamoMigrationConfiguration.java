package com.github.mlk.simples.migrations.dynamodb;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.github.mlk.simples.migrations.CheckMigration;
import com.github.mlk.simples.migrations.MigrationOMatic;
import com.github.mlk.simples.migrations.MigrationsFinder;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;

@Configuration
public class DynamoMigrationConfiguration {

  @Bean(name = "com.github.mlk.simples.migrations.MigrationOMatic")
  @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
  public MigrationOMatic migrationOMatic(MigrationsFinder migrationsFinder, CheckMigration checkMigration) {
    return new MigrationOMatic(migrationsFinder, checkMigration);
  }

  @Bean(name = "com.github.mlk.simples.migrations.MigrationsFinder")
  @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
  public MigrationsFinder migrationsFinder(ApplicationContext context) {
    return new MigrationsFinder(context);
  }

  @Bean(name = "com.github.mlk.simples.migrations.dynamodb.DynamoCheckMigration")
  @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
  public CheckMigration checkMigration(AmazonDynamoDB client) {
    return new DynamoCheckMigration(client);
  }
}
