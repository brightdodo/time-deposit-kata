# Time Deposit Kata

A Spring Boot application that manages time deposit accounts, calculates monthly interest according to plan rules, tracks withdrawals, and exposes REST APIs. Uses Flyway for database migrations and an in-memory H2 database by default.

---

## Features

- **Interest Calculation**: Monthly interest based on plan type:
    - **Basic**: 1% p.a. (applied monthly)
    - **Student**: 3% p.a. up to one year
    - **Premium**: 5% p.a. starting after 45 days
    - No interest for the first 30 days on all plans
- **Withdrawals**: Track withdrawal history per deposit
- **REST APIs**:
    - `GET /api/time-deposits` — list all deposits with withdrawals
    - `PUT /api/time-deposits/update-balances` — recalculate and persist balances
- **Persistence**:
    - JPA entities for `TimeDeposit` and `Withdrawal`
    - Flyway migrations for schema (`V1__create_time_deposits_and_withdrawals_tables.sql`) and seed data (`V2__insert_test_data.sql`)
- **Open/Closed Design**:
    - Interest rules implemented via injectable `InterestStrategy` components
    - New plans can be added by creating a new `InterestStrategy` bean
- **Testing**:
    - Unit tests for business logic and service layer
    - Integration tests against H2 in-memory database

---

## Getting Started

### Prerequisites

- Java 17
- Maven 3.6+

### Build & Run

1. **Clone the repository**
   ```bash
   git clone https://github.com/brightdodo/time-deposit-kata.git
   cd time-deposit-kata
   ```
   
2. **Build the project**
    ```
   mvn clean install
   ```
3. **Run the application**
    ```
   mvn spring-boot:run
   ```
4. **Accessing the APIs**
   - Swagger UI: http://localhost:8080/swagger-ui.html
   - OpenAPI JSON: http://localhost:8080/v3/api-docs

Example requests:
```bash
# List deposits
curl http://localhost:8080/api/time-deposits

# Update balances
curl -X PUT http://localhost:8080/api/time-deposits/update-balances -v
```

### Database Migrations

Flyway migrations are located under src/main/resources/db/migration:
•	V1__create_time_deposits_and_withdrawals_tables.sql — schema creation
•	V2__insert_test_data.sql — sample seed data

On application startup, Flyway will run these migrations automatically against the configured datasource (H2 by default).

To switch to another database (PostgreSQL, MySQL, etc.), configure the following properties in application.yml or application.properties:
```yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb
    username: sa
    password:
  flyway:
    enabled: true
```

### Extending Interest Rules

To add a new plan type:
1. Create a new class implementing InterestStrategy, annotate with @Component, and return a unique getPlanCode().
2. Spring will auto-register it; no changes to existing code are needed.