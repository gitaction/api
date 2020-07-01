FROM openjdk:11

ENV APP_JAR=app.jar

RUN apt-get update \
    && apt-get -y dist-upgrade \
    && apt-get -y remove python2.7 git curl mercurial bzip2 \
    && apt-get -y autoremove \
    && rm -rf /var/lib/apt/lists/*

COPY build/libs/crawler-0.0.1-SNAPSHOT.jar ./app.jar

CMD ["sh", "-c", "java -XX:MetaspaceSize=256M -Xms1024M -Dfile.encoding=UTF-8 -Djava.security.egd=file:/dev/./urandom -Duser.timezone=UTC -jar /$APP_JAR"]
