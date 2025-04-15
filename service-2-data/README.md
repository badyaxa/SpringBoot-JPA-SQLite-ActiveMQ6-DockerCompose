# Service 2 - Data
Microservice receives a message from the ActiveMQ queue with the client id.
Then it simulates calling third-party service (uses mocks) to obtain client full name by the specified id.
Then it sends the information to the ActiveMQ queue.

## Tech Specs 🔖

- Java `17`
- Spring Boot `3.4.4`
- ActiveMQ `6+`
- Docker

## Build
#### run ```mvn clean package``` to build the service.

## Improvement
- Кешування даних з зовнішніх сервісів які відносно рідко змінюються (Kafka, Redis, Map). Це зменшить навантаження на мікросервіси, які роблять виклики до зовнішніх систем, та прискорить відповідь для повторних запитів з однаковими ID клієнтів. Потрібно налаштувати TTL (time-to-live) для кешу залежно від типу даних
- Обробка невдалих повідомлень (Dead Letter Queue) і повторна спроба їх обробки. Це дозволить уникнути втрати даних у разі помилок при обробці повідомлень.
- Обробка ID клієнтів, для яких не вдалося отримати дані з зовнішніх сервісів. Можна реалізувати механізм повторних спроб або записувати такі ID в окрему таблицю для подальшого аналізу.
- Валідувати дані отримані з зовнішніх сервісів якщо вони мають відповідати певному формату.

## Author
- Github: [@badyaxa](https://github.com/badyaxa/SpringBoot-JPA-SQLite-ActiveMQ6-DockerCompose)
