# Eagle Bank API

A Java Spring Boot REST API for Eagle Bank, supporting user management, JWT authentication, bank accounts, and transactions. Uses SQLite for main DB and H2 for tests.

## Prerequisites
- Java 17+
- Gradle 7+
- [SQLite](https://www.sqlite.org/download.html) (CLI tools)

## Database Setup

### 1. SQLite (Development)
- The app uses `db/eagle_bank.db` as the main database.
- On first run, the database and tables are auto-created if missing.
- To manually initialize or reset the schema:
  1. Ensure `db/` directory exists in the project root.
  2. Run:
     ```sh
     sqlite3 db/eagle_bank.db < eaglebank-api/src/main/resources/init.sql
     ```
  3. (Optional) Inspect DB:
     ```sh
     sqlite3 db/eagle_bank.db
     # Then use .tables and SQL commands
     ```

### 2. H2 (Testing)
- Tests use an in-memory H2 database, auto-initialized from schema.

## Running the Application

1. **From the project root:**
   ```sh
   cd eaglebank-api
   ./gradlew bootRun
   ```
   The API will be available at [http://localhost:8080](http://localhost:8080)

2. **To build a JAR:**
   ```sh
   ./gradlew build
   java -jar build/libs/eaglebank-api-*.jar
   ```

## Running Tests

From `eaglebank-api`:
```sh
./gradlew test
```

## Useful Endpoints
- `POST /v1/users` — Create user
- `POST /v1/auth` — Authenticate (get JWT)
- `POST /v1/accounts` — Create bank account
- `POST /v1/accounts/{accountNumber}/transactions` — Create transaction
- `GET /v1/accounts/{accountNumber}/transactions` — List transactions
- `GET /v1/accounts/{accountNumber}/transactions/{transactionId}` — Get transaction by ID

See OpenAPI spec for full details.

## Troubleshooting
- **DB errors:** Ensure `db/eagle_bank.db` exists and is initialized. Run the schema as above if needed.
- **Manual ID assignment:** Both `User` and `BankAccount` require manually set IDs (e.g., `usr-...`, `acc-...`).
- **Port in use:** Change `server.port` in `application.properties` if 8080 is busy.
- **JWT errors:** Ensure you use the JWT returned from `/v1/auth` for protected endpoints.

---
For more, see the code and comments in each module. If you have issues, check logs or open an issue.