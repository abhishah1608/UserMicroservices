# Stage 0: Base Dependencies Stage
# This uses your pre-built image containing the external JARs
FROM abhishah1608/externaljarmicroservices:1.0 AS external-dependencies

# Stage 1: Maven Builder Stage
# Use a full Maven image to build the application
#FROM maven:3.9.6-openjdk-24  

#FROM maven:3.9.6-eclipse-temurin-24 AS build

#FROM eclipse-temurin:25-jdk-alpine AS build

FROM maven:3.9-ibm-semeru-25-noble AS build

# Set the working directory
WORKDIR /app

# Copy the external JARs from the 'external-dependencies' stage into the builder's local Maven repository
# Default Maven local repo path is ~/.m2/repository
# Note: This step is a workaround to satisfy Maven's build process without an external Nexus/Artifactory server.
COPY --from=external-dependencies /app/lib/annotations.jar /tmp/annotations-1.0.0.jar
COPY --from=external-dependencies /app/lib/dboperation.jar /tmp/dboperation-1.0.0.jar
COPY --from=external-dependencies /app/lib/model.jar /tmp/model-1.0.0.jar
COPY --from=external-dependencies /app/lib/hashutility.jar /tmp/hashutility-1.0.0.jar

RUN mvn -q install:install-file \
    -Dfile=/tmp/annotations-1.0.0.jar \
    -DgroupId=com.demo \
    -DartifactId=annotations \
    -Dversion=1.0.0 \
    -Dpackaging=jar

RUN mvn -q install:install-file \
	    -Dfile=/tmp/hashutility-1.0.0.jar \
	    -DgroupId=com.demo \
	    -DartifactId=hashutility \
	    -Dversion=1.0.0 \
	    -Dpackaging=jar
		
RUN mvn -q install:install-file \
		    -Dfile=/tmp/dboperation-1.0.0.jar \
		    -DgroupId=com.demo \
		    -DartifactId=dboperation \
		    -Dversion=1.0.0 \
		    -Dpackaging=jar

RUN mvn -q install:install-file \
			    -Dfile=/tmp/model-1.0.0.jar \
			    -DgroupId=com.demo \
			    -DartifactId=model \
			    -Dversion=1.0.0 \
			    -Dpackaging=jar



# 2) Resolve all deps (now your custom jar is known)
COPY pom.xml .
RUN mvn -B -q -DskipTests dependency:go-offline

# 3) Build your app
COPY src ./src
RUN mvn -B -e -X -DskipTests package

# Runtime
FROM eclipse-temurin:24-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8090
ENTRYPOINT ["java","-jar","app.jar"]
