# SpringTest
RestAPI에 맞춰 작성한 간단한 회원 생성, 수정, 삭제, 조회 함수를 구현했습니다. 

# 회원 API 명세서
[Index]                              [Method]                              [URL]                              [Description]    
1                                    POST                                  /app/users/sign-up                 회원가입
2                                    GET                                   /app/users                         전체 회원 조회
3                                    POST                                  /app/uses/log-in                   로그인
4                                    PATCH                                 /app/users/:userIdx                회원 수정
5                                    GET                                   /app/users/:userIdx                특정 회원 조회
6                                    DELETE                                /app/users/delete/:userIdx         회원 삭제
