# book-challenge
매월 다른 사용자와 함께 경쟁하는 독서 챌린지 서비스

## 🎋 브랜치 관리 전략

프로젝트의 효율적인 개발과 배포를 위해 다음과 같은 브랜치 전략을 사용합니다.

![image](https://github.com/user-attachments/assets/2fba0d64-c38c-405b-892a-bce116b5e84e)


### feature branch

- **목적**: 실제 기능 개발을 위한 브랜치
- **특징**:
  - develop 브랜치에서 분기
  - 모든 개발 작업은 이 브랜치에서 진행
  - 일시적으로 존재하며, 로컬 환경에서만 유지
- **명명 규칙**: `feature/이슈번호/기능개발명`
- **사용 예**:
  ```bash
  $ git switch develop
  $ git switch -c feature/12345/기능개발명
  ```

### develop branch

- **목적**: 테스트 서버 배포용 브랜치
- **특징**:
  - main 브랜치에서 생성
  - 자유롭게 테스트 서버에 배포 가능
  - 필요 시 직접 버그 수정 또는 충돌 해결 가능
  - 테스트 완료 후 main 브랜치로 PR 요청
- **사용 예**:
  ```bash
  $ git switch develop
  $ git merge feature/12345/기능개발명
  ```

### main branch

- **목적**: 운영 서버 배포용 안정 브랜치
- **특징**:
  - PR 접수 시 코드 리뷰와 CI 테스트 진행
  - 승인 시 자동으로 운영 서버에 배포
  - 승인된 PR은 Git Squash Commit으로 처리
    - 여러 커밋을 하나로 통합하여 안정적인 롤백 지원
   


