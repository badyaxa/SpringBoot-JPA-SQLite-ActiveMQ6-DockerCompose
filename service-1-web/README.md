# Service 1 - Web
Microservice accept an HTTP JSON request with the client id as input.
Then send a message to the ActiveMQ queue with the client id.

## Tech Specs 🔖

- Java `17`
- Spring Boot `3.4.4`
- ActiveMQ `6+`
- Docker

## Build
#### run ```mvn clean package``` to build the service.

## API (postman)
[POST to http://localhost:8081/api/client/login?userId=someUserId](http://localhost:8081/api/client/login?userId=someUserId)
to receive a client Bearer token.

[POST to http://localhost:8081/api/client/id](http://localhost:8081/api/client/id)
with token and with body:
```json
{ "clientId": "00000000-0000-0000-0000-000000000002" }
```
## Improvement
- Додати OpenAPI (Swagger) для документації API. Це дозволить розробникам легше зрозуміти, як використовувати API.

## Author
- Github: [@badyaxa](https://github.com/badyaxa/SpringBoot-JPA-SQLite-ActiveMQ6-DockerCompose)
