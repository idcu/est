#!/bin/bash
# EST Framework Docker Build Script for Linux/Mac
# Usage: ./build-docker.sh [version]

set -e

PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
DOCKERFILE_PATH="$(cd "$(dirname "${BASH_SOURCE[0]}")/../docker" && pwd)/Dockerfile"
IMAGE_NAME="est-demo"
DEFAULT_VERSION="2.1.0"
VERSION=${1:-$DEFAULT_VERSION}

echo "========================================"
echo "Building EST Demo Docker Image"
echo "========================================"
echo "Image Name: $IMAGE_NAME"
echo "Version: $VERSION"
echo "Project Root: $PROJECT_ROOT"
echo ""

# Build the Docker image
docker build -f "$DOCKERFILE_PATH" -t "$IMAGE_NAME:$VERSION" "$PROJECT_ROOT"

echo ""
echo "========================================"
echo "Build successful!"
echo "========================================"
echo "Image: $IMAGE_NAME:$VERSION"
echo ""
echo "To run the container:"
echo "docker run -d -p 8080:8080 --name est-demo $IMAGE_NAME:$VERSION"
echo ""
