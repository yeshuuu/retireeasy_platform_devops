# RetireEasy Platform

A **Java Spring Boot microservices** platform for retirement account management, deployed on **AWS EKS** via GitOps with **Argo CD**.

---

## Architecture

```
GitLab CI  ──►  Amazon ECR (images)
                     │
GitLab Repo ◄── CI updates deployment image tags
     │
     └──► Argo CD watches repo ──► EKS Cluster (retireeasy-dev namespace)
                                        ├── user-service        :8081
                                        ├── account-service     :8082
                                        └── transaction-service :8083
                                                  │
                                         OpenFeign call to account-service
```

---

## Services

| Service | Port | Description |
|---|---|---|
| [`user-service`](./user-service/README.md) | 8081 | Manages user profiles |
| [`account-service`](./account-service/README.md) | 8082 | Manages retirement accounts |
| [`transaction-service`](./transaction-service/README.md) | 8083 | Records contributions & transactions |

All services use **PostgreSQL** as their database and are built with **Spring Boot 3/4**, **Spring Data JPA**, and **Lombok**.

---

## Repository Layout

```
retireeasy-platform/
├── account-service/          # Spring Boot microservice
├── transaction-service/      # Spring Boot microservice (calls account-service)
├── user-service/             # Spring Boot microservice
├── argocd/                   # Argo CD App-of-Apps manifests
│   ├── bootstrap/            # Child Application, AppProject, Namespace
│   └── root-application.yaml # Single entry-point — bootstrap everything
├── kubernetes/               # Raw K8s Deployment + Service + Secret manifests
│   ├── account-service/
│   ├── transaction-service/
│   └── user-service/
├── .gitlab-ci.yml            # CI/CD pipeline (build → docker → deploy)
└── deploy.md                 # Detailed step-by-step deployment guide
```

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.2.5 / 4.0.3 |
| Persistence | Spring Data JPA + PostgreSQL |
| Inter-service | OpenFeign (transaction → account) |
| Container | Docker (eclipse-temurin:17-jre) |
| Orchestration | Kubernetes (AWS EKS) |
| Image Registry | Amazon ECR |
| GitOps / CD | Argo CD (App-of-Apps) |
| CI | GitLab CI |

---

## CI/CD Flow

```
push to main
     │
     ├─ [build]            mvn clean package -DskipTests  (all 3 services)
     ├─ [docker]           docker build + push to ECR  (tag = git SHA)
     └─ [update-manifests] sed image tags in kubernetes/ + git commit [skip ci]
                                │
                          Argo CD auto-syncs → EKS rolls out new pods
```

---

## GitOps Bootstrap

> **Prerequisites**: Argo CD installed in the `argocd` namespace, `ecr-secret` K8s secret present, AWS credentials set as GitLab CI/CD variables.

1. Update `spec.source.repoURL` in all files under `argocd/` to your GitLab repo URL.
2. Apply the Kubernetes Secrets in each service folder:
   ```bash
   kubectl apply -f kubernetes/account-service/secret.yaml
   kubectl apply -f kubernetes/user-service/secret.yaml
   kubectl apply -f kubernetes/transaction-service/secret.yaml
   ```
3. Bootstrap Argo CD:
   ```bash
   kubectl apply -f argocd/root-application.yaml
   ```

Argo CD will create the `retireeasy-dev` namespace, `AppProject`, and all service `Application` resources automatically.

---

## Local Development

Each service can be run locally with a PostgreSQL instance on port `5433`:

```bash
# Start a local PostgreSQL container
docker run -d --name pg -e POSTGRES_PASSWORD=postgres -p 5433:5432 postgres:15

# Run any service (example: user-service)
cd user-service
./mvnw spring-boot:run
```

See each service's README for specific endpoints and environment variables.

---

## Required GitLab CI/CD Variables

| Variable | Description |
|---|---|
| `AWS_ACCESS_KEY_ID` | AWS access key (ECR push permissions) |
| `AWS_SECRET_ACCESS_KEY` | AWS secret key |
| `GITLAB_TOKEN` | GitLab personal access token (for manifest update push) |
