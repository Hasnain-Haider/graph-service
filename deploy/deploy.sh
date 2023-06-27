#! /bin/sh
kubectl apply -f deployment.yaml 
kubectl apply -f service.yaml
gcloud builds submit --config cloudbuild.yaml .