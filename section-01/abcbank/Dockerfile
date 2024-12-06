# syntax=docker/dockerfile:1

################################################################################
# Stage 1: Resolve and download dependencies
FROM eclipse-temurin:23-jdk AS deps

WORKDIR /build

# Copy Maven wrapper and necessary files
COPY --chmod=0755 mvnw mvnw
COPY .mvn/ .mvn/
COPY pom.xml ./

# Download dependencies
RUN ./mvnw dependency:go-offline -DskipTests

################################################################################
# Stage 2: Build the application
FROM deps AS package

WORKDIR /build

# Copy source code
COPY src/ src/

# Package the application
RUN ./mvnw package -DskipTests

################################################################################
# Stage 3: Extract layers (optional, useful for Spring Boot applications)
FROM package AS extract

WORKDIR /build

RUN java -Djarmode=layertools -jar target/*.jar extract --destination target/extracted

################################################################################
# Stage 4: Final runtime image
FROM eclipse-temurin:23-jre AS final

# Create a non-privileged user
ARG UID=10001
RUN adduser \
    --disabled-password \
    --gecos "" \
    --home "/nonexistent" \
    --shell "/sbin/nologin" \
    --no-create-home \
    --uid "${UID}" \
    appuser

USER appuser

# Copy extracted layers
COPY --from=extract /build/target/extracted/dependencies/ ./
COPY --from=extract /build/target/extracted/spring-boot-loader/ ./
COPY --from=extract /build/target/extracted/snapshot-dependencies/ ./
COPY --from=extract /build/target/extracted/application/ ./

EXPOSE 8080

ENTRYPOINT [ "java", "org.springframework.boot.loader.launch.JarLauncher" ]

################################################################################
# Stage 5: Development stage with live reload capabilities
FROM deps AS dev

WORKDIR /build

# Copy source code before running Maven commands
COPY src/ src/
COPY pom.xml ./

# Install Spring Boot DevTools for live reloading
RUN ./mvnw install -DskipTests

# Expose port 8080
EXPOSE 8080

# Use Spring Boot's dev mode with DevTools
CMD ["./mvnw", "spring-boot:run"]
