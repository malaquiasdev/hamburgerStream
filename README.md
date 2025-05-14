# 🍔 hamburgerStream

**From the queue to the table — a tasty backend API to process and serve real-time order data.**

## 📦 Overview

**hamburgerStream** is a backend application built as a technical exercise to practice key software engineering concepts such as microservices architecture, message queues (RabbitMQ), data persistence, and RESTful APIs.

The system simulates a real-world flow where customer orders are received through a message queue, processed to calculate total values, stored in a database, and made available through an API.

## 🎯 Goals

- Consume order messages from a RabbitMQ queue
- Calculate the total amount for each order
- Store order and customer data in a database
- Provide a REST API with endpoints for:
    - Total value per order
    - Number of orders per customer
    - List of orders by customer

## 🧾 Example Order Message

```json
{
  "orderCode": 2025,
  "customerId": 87,
  "items": [
    {
      "product": "Big Mac",
      "quantity": 2,
      "price": 5.99
    },
    {
      "product": "Large Fries",
      "quantity": 1,
      "price": 3.49
    },
    {
      "product": "Coca-Cola (Medium)",
      "quantity": 2,
      "price": 1.89
    }
  ]
}
```

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/hamburgerstream-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult <https://quarkus.io/guides/maven-tooling>.

## Related Guides

- REST ([guide](https://quarkus.io/guides/rest)): A Jakarta REST implementation utilizing build time processing and Vert.x. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on it.
- Camel Spring RabbitMQ ([guide](https://camel.apache.org/camel-quarkus/latest/reference/extensions/spring-rabbitmq.html)): Send and receive messages from RabbitMQ using Spring RabbitMQ client
- REST Jackson ([guide](https://quarkus.io/guides/rest#json-serialisation)): Jackson serialization support for Quarkus REST. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on it
- Hibernate ORM with Panache ([guide](https://quarkus.io/guides/hibernate-orm-panache)): Simplify your persistence code for Hibernate ORM via the active record or the repository pattern
- JDBC Driver - PostgreSQL ([guide](https://quarkus.io/guides/datasource)): Connect to the PostgreSQL database via JDBC

## Provided Code

### Hibernate ORM

Create your first JPA entity

[Related guide section...](https://quarkus.io/guides/hibernate-orm)

[Related Hibernate with Panache section...](https://quarkus.io/guides/hibernate-orm-panache)


### REST

Easily start your REST Web Services

[Related guide section...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)