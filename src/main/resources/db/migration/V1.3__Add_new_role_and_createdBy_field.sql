INSERT INTO authority (name) VALUES ('ROLE_ANONYMOUS');

ALTER TABLE course ADD created_by bigint not null;