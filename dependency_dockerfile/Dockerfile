# Use a lightweight base image with a Java Runtime Environment (JRE) only
# Choosing a specific, stable version is a best practice.
FROM openjdk:26-ea-trixie as dependencies

# Create a directory in the image where the JARs will live
WORKDIR /app/lib

# Copy the dependency JAR files from your local 'external_jars' directory into the image
# This creates a layer that is cached unless the contents of external_jars change
COPY jar_files/*.jar ./

# This image is now ready to be tagged and pushed to a registry.
# It can be used as a 'FROM' image in other Dockerfiles.
