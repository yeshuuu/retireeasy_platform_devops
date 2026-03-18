# user-service

Manages **user profiles** for the RetireEasy platform.

- **Port**: `8081`
- **Spring Boot**: `4.0.3`
- **Database**: PostgreSQL — table `users`

---

## API Endpoints

| Method | Path | Description |
|---|---|---|
| `POST` | `/users` | Create a new user |
| `GET` | `/users/{id}` | Get user by ID |

### Example — Create User

```http
POST /users
Content-Type: application/json

{
  "name": "Jane Doe",
  "email": "jane@example.com",
  "phone": "555-0100"
}
```

### Example — Get User

```http
GET /users/1
```

---

## Entity

```java
User {
  Long   id
  String name
  String email
  String phone
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
cd user-service
./mvnw spring-boot:run
```

Service starts on `http://localhost:8081`.

---

## Docker

```bash
# Build
./mvnw clean package -DskipTests
docker build -t user-service:local .

# Run
docker run -p 8081:8081 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5433/retireeasy \
  user-service:local
```

---

## Kubernetes

Manifests are in [`../../kubernetes/user-service/`](../../kubernetes/user-service/).

- `deployment.yaml` — Deployment pulling from ECR
- `service.yaml`    — ClusterIP Service on port 8081
- `secret.yaml`     — PostgreSQL credentials (fill in values before applying)
