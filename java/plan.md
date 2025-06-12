# Analysis of Time Deposit Kata

## Findings

1. **Requirements vs. Implementation Gaps**
    - **API Endpoints**
        - _Missing:_ No REST controllers or endpoints to update balances or to retrieve time deposits (including withdrawals).
    - **Persistence Layer**
        - _Missing:_ No database schema (entities, repositories) for `timeDeposits` or `withdrawals`. All data resides in in-memory objects.
    - **Model Definition**
        - _Missing:_ `TimeDeposit` lacks a `withdrawals` collection required by the GET API schema.
    - **Interest Logic**
        - _Present:_ `updateBalance` method implements business rules:
            - No interest for the first 30 days.
            - **Basic:** 1% per annum (applied monthly).
            - **Student:** 3% per annum up to 1 year.
            - **Premium:** 5% per annum starting after 45 days.
        - _Concerns:_ Logic is nested and hard-coded, mixing the “no-interest” guard with plan-specific branches, which hinders future extension and maintenance.
    - **Testing**
        - _Missing:_ No unit tests cover business logic or boundary conditions. The existing test is a placeholder with no assertions on interest.
        - _Coverage:_ 0% of logic branches.
    - **Code Quality**
        - A single method handles iteration, calculation, rounding, and state mutation, violating the Single Responsibility Principle.
        - Plan types compared via raw strings, increasing the risk of typos and inconsistency.
        - No clear separation between domain, service, and API layers.

## Action Plan

1. **Increase Test Coverage to 100%**
    - Write unit tests for each plan type and every boundary case:
        - Days ≤ 30 → expect zero interest (all plans).
        - **Basic** plan at day 31 and beyond.
        - **Student** plan at days 31, 365, and 366.
        - **Premium** plan at days 31, 45, and 46.
    - Include tests for rounding behavior (two decimal places, HALF_UP).
    - Use JUnit 5 parameterized tests with descriptive names and CSV sources.

2. **Refactor Interest Calculation with Strategy Pattern**
    - Introduce a `PlanType` enum encapsulating an `InterestStrategy` for each plan.
    - Move the “no-interest for first 30 days” guard into shared logic.
    - Implement each plan’s rule in its own strategy class.

3. **Extract Rounding and Balance Mutation**
    - Move rounding logic into a helper method, e.g., `roundToTwoDecimals(double amount)`.
    - Keep `updateBalance` focused on iteration and delegation only.

4. **Model and Persist Withdrawals**
    - Define a `Withdrawal` JPA entity (`id`, `timeDepositId`, `amount`, `date`).
    - Extend the `TimeDeposit` entity to include a `List<Withdrawal>`.
    - Create Spring Data repositories for both entities.

5. **Implement REST API Layer**
    - **GET** `/api/time-deposits` → return all deposits with withdrawals.
    - **PUT** `/api/time-deposits/update-balances` → trigger balance update and persist changes.
    - Use Spring Boot controllers, map entities to DTOs, and document with OpenAPI annotations.

6. **Integration Testing**
    - Use Testcontainers to spin up a real database.
    - Write integration tests that:
        1. Seed sample deposits and withdrawals.
        2. Call each REST endpoint.
        3. Assert database state and response correctness.
    - Ensure overall coverage ≥ 95% across unit and integration tests.

7. **Design for Extensibility**
    - Organize code into **Domain**, **Application** (services), and **Adapters** (API, persistence).
    - Ensure adding new plan types or business rules requires minimal changes.

---