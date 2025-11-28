# === TILTFILE FOR ZAPLINK MICROSERVICES ===
# Compatible with Windows (no Python imports)

# Load all YAMLs using kustomize
k8s_yaml(kustomize('k8s'))

# Define Docker builds for all services
docker_build("api-gateway-service", "services/api-gateway-service", dockerfile="services/api-gateway-service/Dockerfile")
docker_build("zaplink-service-registry", "services/zaplink-service-registry", dockerfile="services/zaplink-service-registry/Dockerfile")
docker_build("zaplink-producer-service", "services/zaplink-producer-service", dockerfile="services/zaplink-producer-service/Dockerfile")
docker_build("zaplink-manager-service", "services/zaplink-manager-service", dockerfile="services/zaplink-manager-service/Dockerfile")
docker_build("zaplink-consumer-service", "services/zaplink-consumer-service", dockerfile="services/zaplink-consumer-service/Dockerfile")

# Map each service to its Kubernetes deployment
k8s_resource("api-gateway-service", port_forwards=8090)
k8s_resource("zaplink-service-registry", port_forwards=8761)
k8s_resource("zaplink-producer-service", port_forwards=8081)
k8s_resource("zaplink-consumer-service", port_forwards=8082)
k8s_resource("zaplink-manager-service", port_forwards=8083)
k8s_resource("redis", port_forwards=30093)
k8s_resource("kafka", port_forwards=30192)
k8s_resource("kafka-ui", port_forwards=8080)

print("✅ Tiltfile loaded successfully! Run `tilt up` to start all services 🚀")
