package com.github.mlk.simples.migrations.dynamodb.example;

import com.github.mlk.simples.migrations.dynamodb.EnableSimplesDynamoMigration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableSimplesDynamoMigration
@SpringBootApplication
@EnableConfigurationProperties(SimplesConfig.class)
public class SimplesApp {

}
