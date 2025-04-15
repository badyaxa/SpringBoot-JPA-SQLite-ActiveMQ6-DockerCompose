# Welcome to microservices-spring-boot 
The first microservice should accept an HTTP JSON request with the client id as input.

The next three should be sequential and simulate calling third-party services (use mocks) to obtain various client data by the specified id. For example, full name, residential address, contact list, card numbers, etc.

The last microservice should write data to a SQL database using JPA.

## Tech Specs 🔖

- Java `17`
- Spring Boot `3.4.4`
- Spring Data JPA
- SQLite
- ActiveMQ `6+`
- Docker Compose

## Build
#### On each service you should run ```mvn clean package``` to build the service.

## Up
Start and run all services:
```sh
  docker-compose up -d --build
```

## API (postman)
[POST to http://localhost:8081/api/client/login?userId=someUserId](http://localhost:8081/api/client/login?userId=someUserId)
to receive a client Bearer token.

[POST to http://localhost:8081/api/client/id](http://localhost:8081/api/client/id)
with token and with body:
```json
{ "clientId": "00000000-0000-0000-0000-000000000002" }
```
## Down
Stops containers and removes containers, networks, volumes, and images created by `up`:
```sh
  docker-compose down --rmi local -v
```

## Improvement
- Додати моніторинг (Actuator) для моніторингу стану сервісів.
- Додати централізоване логування для спрощення налагодження та відстеження проблем у розподіленій системі. Наприклад ELK (Elasticsearch, Logstash, Kibana) або Graylog.
- Для виявлення проблем з продуктивністю можна застосувати профілювання (наприклад telemetria) + Prometheus/Grafana для моніторингу.
- Застосувати інструмент статичного аналізу коду (наприклад Parasoft чи SonarQube) для виявлення проблем з якістю коду.

## Author
- Github: [@badyaxa](https://github.com/badyaxa/SpringBoot-JPA-SQLite-ActiveMQ6-DockerCompose)
