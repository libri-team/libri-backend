USE mysql;

-- 전체 권한을 가진 사용자 생성
CREATE USER 'libri_user'@'%' IDENTIFIED BY 'libri_local_pass';
GRANT ALL PRIVILEGES ON libri_db.* TO 'libri_user'@'%';

-- READ 전용 사용자 생성
CREATE USER 'libri_reader'@'%' IDENTIFIED BY 'reader_local_pass';
GRANT SELECT ON libri_db.* TO 'libri_reader'@'%';

FLUSH PRIVILEGES;