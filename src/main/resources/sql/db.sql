-- DB 생성
CREATE DATABASE ks_lecture default CHARACTER SET UTF8;

-- 유저 생성
create user 'ks_lecture_adm'@'localhost' identified by '5gY7PIianDDNUQ4';

-- 권한 부여 및 반영
grant all privileges on ks_lecture.* to 'ks_lecture_adm'@'localhost';
FLUSH PRIVILEGES;

-- 강연 테이블 생성
CREATE TABLE lecture (
  lecture_seq bigint NOT NULL AUTO_INCREMENT,
  lecturer VARCHAR(50) not null,
  lecture_hall_code VARCHAR(20) not null,
  lecture_date datetime not null,
  lecture_content varchar(1000),
  apply_cnt int not null default 0,
  max_cnt int not null,
  reg_date datetime not null,
  PRIMARY KEY(lecture_seq)
) ENGINE=InnoDB CHARSET=utf8;

-- 신청 테이블 생성
CREATE TABLE apply (
  apply_seq bigint NOT NULL AUTO_INCREMENT,
  lecture_seq bigint NOT NULL,
  employee_number VARCHAR(5),
  reg_date datetime not null,
  PRIMARY KEY(apply_seq),
  FOREIGN KEY(lecture_seq) REFERENCES lecture(lecture_seq)
) ENGINE=InnoDB CHARSET=utf8;