# account-service

Manages **retirement accounts** for the RetireEasy platform.

- **Port**: `8082`
- **Spring Boot**: `4.0.3`
- **Database**: PostgreSQL — table `accounts`

---

## API Endpoints

| Method | Path | Description |
|---|---|---|
| `POST` | `/accounts` | Create a new account |
| `GET` | `/accounts/{id}` | Get account by ID |
| `PUT` | `/accounts/{id}/balance?amount=X` | Add/subtract from account balance |

### Example — Create Account

```http
POST /accounts
Content-Type: application/json

{
  "userId": 1,
  "accountType": "401K",
  "planName": "Growth Plan"
}
```

### Example — Update Balance

```http
PUT /accounts/1/balance?amount=500.00
```

---

## Entity

```java
Account {
  Long   accountId
  Long   userId
  String accountType   // e.g. "401K", "IRA"
  String planName
  Double balance       // default: 0.0
  String status        // default: "ACTIVE"
}
```

---

## Environment Variables

| Variable | Default (local) | Description |
|---|---|---|
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://localhost:5433/retireeasy` | PostgreSQL JDBC URL |
| `SPRING_DATASOURCE_USERNAME` | `postgres` | DB username |
| `SPRING_DATASOURCE_PASSWORD` | `postgres` | DB password |

---

## Running Locally

```bash
cd account-service
./mvnw spring-boot:run
```

Service starts on `http://localhost:8082`.

---

## Docker

```bash
# Build
./mvnw clean package -DskipTests
docker build -t account-service:local .

# Run
docker run -p 8082:8082 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5433/retireeasy \
  account-service:local
```

---

## Kubernetes

Manifests are in [`../../kubernetes/account-service/`](../../kubernetes/account-service/).

- `deployment.yaml` — Deployment pulling from ECR
- `service.yaml`    — ClusterIP Service on port 8082
- `secret.yaml`     — PostgreSQL credentials (fill in values before applying)
