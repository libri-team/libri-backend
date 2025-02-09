use mysql;
create user 'libri'@'%' identified by '1234';
grant all privileges on libri.* to 'libri'@'%';
