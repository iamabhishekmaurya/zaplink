# === TILTFILE FOR ZAPLINK MICROSERVICES ===
# Compatible with Windows (no Python imports)

# Load all YAMLs from the "k8s" folder manually
k8s_yaml([
    "k8s/api-gateway-deployment.yaml",
    "k8s/zaplink-service-registry-deployment.yaml",
    "k8s/zaplink-producer-deployment.yaml",
    "k8s/zaplink-manager-deployment.yaml",
    "k8s/zaplink-consumer-deployment.yaml"
])

# Define Docker builds for all services
docker_build("api-gateway-service", "services/api-gateway-service", dockerfile="services/api-gateway-service/Dockerfile")
docker_build("zaplink-service-registry", "services/zaplink-service-registry", dockerfile="services/zaplink-service-registry/Dockerfile")
docker_build("zaplink-producer-service", "services/zaplink-producer-service", dockerfile="services/zaplink-producer-service/Dockerfile")
docker_build("zaplink-manager-service", "services/zaplink-manager-service", dockerfile="services/zaplink-manager-service/Dockerfile")
docker_build("zaplink-consumer-service", "services/zaplink-consumer-service", dockerfile="services/zaplink-consumer-service/Dockerfile")

# Map each service to its Kubernetes deployment
k8s_resource("api-gateway-service", port_forwards=[8000])
k8s_resource("zaplink-service-registry", port_forwards=[8761])
k8s_resource("zaplink-producer-service", port_forwards=[9001])
k8s_resource("zaplink-consumer-service", port_forwards=[9002])
k8s_resource("zaplink-manager-service", port_forwards=[9003])

print("✅ Tiltfile loaded successfully! Run `tilt up` to start all services 🚀")
