# === TILTFILE FOR ZAPLINK MICROSERVICES ===
# Compatible with Windows (no Python imports)

# Load all YAMLs using kustomize
k8s_yaml(kustomize('k8s'))

# Define Docker builds for all services
docker_build("api-gateway-service", "services/api-gateway-service", dockerfile="services/api-gateway-service/Dockerfile")
docker_build("zaplink-shortner-service", "services/zaplink-shortner-service", dockerfile="services/zaplink-shortner-service/Dockerfile")
docker_build("zaplink-manager-service", "services/zaplink-manager-service", dockerfile="services/zaplink-manager-service/Dockerfile")
docker_build("zaplink-processor-service", "services/zaplink-processor-service", dockerfile="services/zaplink-processor-service/Dockerfile")
docker_build("zaplink-auth-service", "services/zaplink-auth-service", dockerfile="services/zaplink-auth-service/Dockerfile")
docker_build("zaplink-notification-service", "services/zaplink-notification-service", dockerfile="services/zaplink-notification-service/Dockerfile")

# Map each service to its Kubernetes deployment
k8s_resource("api-gateway-service", port_forwards=8090)
k8s_resource("zaplink-shortner-service", port_forwards=8081)
k8s_resource("zaplink-processor-service", port_forwards=8082)
k8s_resource("zaplink-manager-service", port_forwards=8083)
k8s_resource("zaplink-auth-service", port_forwards=8084)
k8s_resource("zaplink-notification-service", port_forwards=8085)
k8s_resource("redis", port_forwards=30093, trigger_mode=TRIGGER_MODE_MANUAL)
k8s_resource("kafka", port_forwards=30192, trigger_mode=TRIGGER_MODE_MANUAL)
k8s_resource("kafka-ui", port_forwards=8080, trigger_mode=TRIGGER_MODE_MANUAL)

print("✅ Tiltfile loaded successfully! Run `tilt up` to start all services 🚀")
