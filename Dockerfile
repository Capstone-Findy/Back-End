###
# 빌드 스테이지
###

# 빌드는 외부에서 진행후 결과를 가져옵니다.
# Dockerfile에서 빌드를 진행하는 경우 Multi-Platform Build시 Platform 갯수마다 빌드하는 문제가 있습니다.

###
# JRE 추출 스테이지
###
FROM amazoncorretto:17-alpine3.20 AS jre-extracter

COPY build/libs/*-SNAPSHOT.jar application.jar

# 스프링 부트 레이어 추출
RUN java -Djarmode=layertools -jar application.jar extract

# Extract JRE
RUN apk add --no-cache binutils
RUN $JAVA_HOME/bin/jlink \
         --module-path "$JAVA_HOME/jmods" \
         --verbose \
         --add-modules ALL-MODULE-PATH \
         --strip-debug \
         --no-man-pages \
         --no-header-files \
         --compress=2 \
         --output /jre

###
# 실행 스테이지
###
FROM alpine:3.20 AS deployer
LABEL maintainer="Seongha Moon <azqazq195@wedatalab.com>"

# 환경 변수 설정
ENV JAVA_HOME=/jre
ENV PATH="$JAVA_HOME/bin:$PATH"

WORKDIR /application

# 레이어드 JAR 복사
COPY --from=jre-extracter /jre $JAVA_HOME
COPY --from=jre-extracter dependencies/ ./
COPY --from=jre-extracter spring-boot-loader/ ./
COPY --from=jre-extracter snapshot-dependencies/ ./
COPY --from=jre-extracter application/ ./

# 보안을 위한 비루트 사용자 생성
RUN addgroup --system javauser && adduser -S -s /bin/false -G javauser javauser
RUN chown -R javauser:javauser /application
USER javauser

# 실행 명령
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]