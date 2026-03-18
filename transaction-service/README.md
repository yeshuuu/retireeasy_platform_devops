# transaction-service

Records **contributions and transactions** for retirement accounts. On a `CONTRIBUTION` transaction, it automatically updates the linked account balance via an **OpenFeign** call to `account-service`.

- **Port**: `8083`
- **Spring Boot**: `3.2.5`
- **Spring Cloud**: `2023.0.1` (Feign client)
- **Database**: PostgreSQL — table `transactions`
- **Depends on**: `account-service` (for balance updates)

---

## API Endpoints

| Method | Path | Description |
|---|---|---|
| `POST` | `/transactions` | Create a transaction |
| `GET` | `/transactions/account/{accountId}` | Get all transactions for an account |

### Example — Create Contribution

When `txnType` is `CONTRIBUTION`, the account balance is automatically updated.

```http
POST /transactions
Content-Type: application/json

{
  "accountId": 1,
  "txnType": "CONTRIBUTION",
  "amount": 500.00
}
```

### Example — Get Transactions

```http
GET /transactions/account/1
```

---

## Entity

```java
Txn {
  Long          txnId
  Long          accountId
  String        txnType    // e.g. "CONTRIBUTION", "WITHDRAWAL"
  Double        amount
  LocalDateTime txnDate    // auto-set on creation
}
```

---

## Inter-Service Communication

Uses **Spring Cloud OpenFeign** to call `account-service`:

```java
@FeignClient(name = "account-service", url = "${account.service.url}")
public interface AccountClient {
    @PutMapping("/accounts/{id}/balance")
    void updateBalance(@PathVariable Long id, @RequestParam Double amount);
}
```

---

## Environment Variables

| Variable | Default (local) | Description |
|---|---|---|
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://localhost:5433/retireeasy` | PostgreSQL JDBC URL |
| `SPRING_DATASOURCE_USERNAME` | `postgres` | DB username |
| `SPRING_DATASOURCE_PASSWORD` | `postgres` | DB password |
| `ACCOUNT_SERVICE_URL` | `http://localhost:8082` | Base URL for account-service |

---

## Running Locally

> Make sure `account-service` is also running on port `8082`.

```bash
cd transaction-service
./mvnw spring-boot:run
```

Service starts on `http://localhost:8083`.

---

## Docker

```bash
# Build
./mvnw clean package -DskipTests
docker build -t transaction-service:local .

# Run
docker run -p 8083:8083 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5433/retireeasy \
  -e ACCOUNT_SERVICE_URL=http://host.docker.internal:8082 \
  transaction-service:local
```

---

## Kubernetes

Manifests are in [`../../kubernetes/transaction-service/`](../../kubernetes/transaction-service/).

- `deployment.yaml` — Deployment pulling from ECR; `ACCOUNT_SERVICE_URL` points to `account-service` K8s ClusterIP
- `service.yaml`    — ClusterIP Service on port 8083
- `secret.yaml`     — PostgreSQL credentials (fill in values before applying)
