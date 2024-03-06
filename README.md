# 개발 언어, 프레임워크, RDB, ORM, 빌드도구
JAVA(17), SpringFramework(SpringBoot 3.2.3), MySQL, JPA, Gradle

# 데이터 설계
강연(Lecture) 1 : N 신청(Apply)
1:N 양방향 관계

[Lecture (강연)]  
lecture_seq (강연Seq. PrimaryKey)  
lecturer (강연자)  
lecture_hall_code (강연장)  
lecture_date (강연일시)  
lecture_content (강연내용)  
apply_cnt (신청한 인원)  
max_cnt (신청 가능 인원)  
reg_date (등록일시)  


[Apply (신청)]  
apply_seq (신청Seq. PrimaryKey)  
lecture_seq (강연Seq. ForeignKey)  
employee_number (사번)  
reg_date (등록일시)

# swagger
http://54.180.122.75:8081/swagger-ui/index.html{: target="_blank"}

# 기타 설명
'신청한 인원'에 대한 동시성 이슈는 배타적 락을 걸어서  
데이터의 일관성을 유지하도록 처리 하였습니다  
그리고 repository, service, controller별 단위 테스트를 작성하였고  
동시성 테스트는 별도로 구성하였습니다 (ApplyConcurrencyTest.java)  
DB, 유저, 권한부여, 테이블 생성 쿼리는 db.sql에 작성하였습니다.  
감사합니다.
