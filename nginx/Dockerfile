# Nginx의 alpine 이미지를 베이스로 사용
FROM nginx:alpine

# default.conf 파일을 Nginx 설정 디렉터리로 복사
COPY default.conf /etc/nginx/conf.d/default.conf

# 컨테이너가 사용하는 포트 80 노출
EXPOSE 80