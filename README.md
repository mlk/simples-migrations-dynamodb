simples-migrations-dynamodb
=================

A poor-mans [Flyway](https://flywaydb.org/) for [dynamodb](https://aws.amazon.com/dynamodb).

To use first add the `@EnableSimplesDynamoMigration` annotation to your
[Spring Boot](https://projects.spring.io/spring-boot/) app. Then create a bunch
of beans that implement `Migration`.
See the `SimplesApp` for an example.

Note: When building this in an IDE you may need to first do a ./gradlew test first.