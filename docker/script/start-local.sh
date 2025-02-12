#!/bin/bash

# 스크립트의 디렉토리 경로를 얻습니다
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
# 프로젝트 루트 디렉토리 (스크립트 디렉토리의 상위 상위 디렉토리)
PROJECT_ROOT="$SCRIPT_DIR/../.."

# 프로젝트 루트 디렉토리로 이동
cd "$PROJECT_ROOT"

# 그래들 빌드
./gradlew clean bootJar

# 도커 컴포즈로 서비스 시작
docker-compose -f docker/compose/docker-compose.local.yml up --build -d

# 로그 출력
docker-compose -f docker/compose/docker-compose.local.yml logs -f
