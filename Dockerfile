# Dockerfile 예시 - Spring Boot 애플리케이션을 위한 Dockerfile

# OpenJDK 17 slim 버전을 베이스 이미지로 사용
FROM openjdk:17-jdk-slim

# 컨테이너 내부에서 /tmp 디렉터리를 VOLUME으로 설정 (로그 등 임시 파일 저장)
VOLUME /tmp


# 빌드 시 전달할 JAR 파일 경로를 target 폴더로 변경
ARG JAR_FILE=/build/libs/Michigan-Korean-Community-1.0.jar

# JAR 파일을 컨테이너 내부의 app.jar로 복사
COPY ${JAR_FILE} app.jar

# 애플리케이션이 사용하는 포트(예: 10000번)를 노출
EXPOSE 8080

# 컨테이너 시작 시 JAR 파일을 실행하도록 ENTRYPOINT 설정
ENTRYPOINT ["java", "-jar", "/app.jar"]