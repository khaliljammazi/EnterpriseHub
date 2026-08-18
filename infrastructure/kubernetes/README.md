# Kubernetes infrastructure

Future Kubernetes manifests or Helm charts belong here.

Planned structure:

```text
kubernetes/
|-- base/          Shared Deployments, Services, and configuration
|-- overlays/
|   |-- dev/       Development-specific values
|   `-- prod/      Production-specific values
`-- README.md
```

No manifests are added yet because container images, ports, health checks, resource requirements, ingress hostnames, and secret-management strategy must be finalized first.
