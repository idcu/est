#!/bin/bash
# EST Framework Kubernetes Undeployment Script for Linux/Mac
# Usage: ./undeploy-k8s.sh [namespace]

set -e

K8S_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/../k8s" && pwd)"
DEFAULT_NAMESPACE="est"
NAMESPACE=${1:-$DEFAULT_NAMESPACE}

echo "========================================"
echo "Undeploying EST Demo from Kubernetes"
echo "========================================"
echo "Namespace: $NAMESPACE"
echo ""

# Confirmation
read -p "WARNING: This will delete all EST Demo resources from namespace $NAMESPACE. Are you sure? (y/N): " -n 1 -r
echo
if [[ ! $REPLY =~ ^[Yy]$ ]]; then
    echo "Operation cancelled."
    exit 0
fi

# Undeploy using Kustomize
echo "Undeploying with Kustomize..."
kubectl delete -k "$K8S_DIR" || true

echo ""
echo "========================================"
echo "Undeployment completed!"
echo "========================================"
