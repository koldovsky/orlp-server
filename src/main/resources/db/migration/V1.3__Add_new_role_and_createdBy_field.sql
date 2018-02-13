INSERT INTO authority (name) VALUES ('ROLE_ANONYMOUS');

ALTER TABLE course ADD created_by bigint DEFAULT 1;

ALTER TABLE course CHANGE created_by created_by bigint not null;
