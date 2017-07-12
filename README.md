simples-migrations-dynamodb
=================

A poor-mans [Flyway](https://flywaydb.org/) for [dynamodb](https://aws.amazon.com/dynamodb).

To use first add the `@EnableSimplesDynamoMigration` annotation to your
[Spring Boot](https://projects.spring.io/spring-boot/) app. Then create a bunch
of beans that implement `Migration`.
See the `SimplesApp` for an example.