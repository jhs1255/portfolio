# 데이터 베이스 관련 오류
- data to long song lyric
    --> 가사 데이터가 너무 큼 mysql workbench에 `MODIFY COLUMN lyric TEXT` 명렁어 입력
- table not fond 
    mysql workbench에서 `show variables like 'lower_case_table_names`를 입력한 후 값이 1인지 확인 값은 1이어야 함
서버 실행 전에 mysqlworkbench에서 
```
drop database if exists berrecommend;
CREATE DATABASE berrecommend;
DROP USER IF EXISTS 'admin'@'%';
CREATE USER 'admin'@'%' IDENTIFIED BY 'qwer1234!@#$';
GRANT ALL PRIVILEGES ON berrecommend.* TO 'admin'@'%';
FLUSH PRIVILEGES;
drop database if exists berrecommend_meta;
CREATE DATABASE berrecommend_meta;
GRANT ALL PRIVILEGES ON berrecommend_meta.* TO 'admin'@'%';
FLUSH PRIVILEGES;
```
를 입력한 후 실행하기.

- chart rank 관련 rank는 예약어이므로 관련 오류가 발생 할 수 있음
entity/ChartDetail.class에     `@Column(nullable = false, name = "`rank`")`로 되어 있는가 확인

# 추가적인 오류 있으시면 말씀해주세요.
