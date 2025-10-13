FROM maven:3.9-eclipse-temurin-24 AS build

WORKDIR /app

# 1) Copy & install external JAR(s) to local Maven repo in the image
COPY lib/annotations.jar /tmp/annotations-1.0.0.jar
COPY lib/dboperation.jar /tmp/dboperation-1.0.0.jar
COPY lib/model.jar /tmp/model-1.0.0.jar
COPY lib/hashutility.jar /tmp/hashutility-1.0.0.jar

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
