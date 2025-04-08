FROM amazoncorretto:17.0.9-al2023

COPY ./target/fss-hockey-spring.jar ./fss-hockey-spring.jar

ENV DOCKER_HOST="host.docker.internal"

EXPOSE 8080

CMD ["sh", "-c", "java -version"]
CMD ["sh", "-c", "java -jar fss-hockey-spring.jar"]

LABEL org.opencontainers.image.source=https://github.com/tom0794/fss-hockey-engine
LABEL org.opencontainers.image.description="fss-hockey-engine"
