## Running
Requires docker. 
In the project directory, you can run:

**Build**

### `docker build -t graph-service .`

**Build for m1 Mac**

#### `docker buildx build --platform=linux/amd64 -t graph-service:latest .`

**Run**

### `docker run -p 8080:8080 -name graph-service graph-service`

### test it is working

`curl localhost:8080/health`

## Deploying to GKE cluster

1. Reserve static ipadress <br> `gcloud compute addresses create graph-service-2-ip --region us-west1 --network-tier=STANDARD`
1. Retrieve Ip Address <br> `gcloud compute addresses list`
1. Use this ip address in service.yaml as the load balancer ip
1. Replace your GCR.io project address in cloudbuild.yaml
1. Replace cluster name and region in cloudbuild.yaml

- [deploy](./deploy)
  - [configMap.yaml](./deploy/configMap.yaml) - config
  - [service.yaml](./deploy/service.yaml) - Kubernetes Service Load Balancer
  - [deployment.yaml](./deploy/deployment.yaml) - Kubernetes Deployment Manifests
  - [clouddeploy.yaml](./deploy/clouddeploy.yaml) - _GCP Build Script_
  - [deploy.sh](./deploy/deploy.sh) - kubectl apply manifests, run cloud build
