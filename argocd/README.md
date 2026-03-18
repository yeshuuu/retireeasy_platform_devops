# Argo CD Bootstrap

This folder contains an app-of-apps setup for deploying the current RetireEasy services with Argo CD.

## What gets created

- an Argo CD `AppProject`
- a `retireeasy-dev` namespace
- one Argo CD `Application` per service
- a root `Application` that bootstraps everything

## Folder layout

```text
argocd/
  bootstrap/
    kustomization.yaml
    namespace-retireeasy-dev.yaml
    retireeasy-project.yaml
    user-service-dev.yaml
    account-service-dev.yaml
    transaction-service-dev.yaml
  root-application.yaml
```

## Bootstrap steps

1. Install Argo CD into the `argocd` namespace.
2. Update `spec.source.repoURL` in `root-application.yaml` and the child application files to your Git repo URL.
3. Apply the root app:

```powershell
kubectl apply -f argocd/root-application.yaml
```

Argo CD will then create the project, namespace, and service applications automatically.

## Notes

- The child applications point to the current raw manifests under `kubernetes/`.
- Auto-sync, prune, and self-heal are enabled.
- `transaction-service` is set to sync after `account-service` because it depends on it.
