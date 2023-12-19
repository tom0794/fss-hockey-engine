FROM ubuntu:noble-20231214

COPY ./container/entrypoint.sh ./entrypoint.sh
COPY ./target/fss-hockey-spring-${project.version}.jar ./fss-hockey-spring.jar

RUN echo `ls -la`

# RUN ./entrypoint.sh
ENTRYPOINT ["/bin/bash", "-c", "echo", "hello"]