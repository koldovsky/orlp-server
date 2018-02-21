INSERT INTO authority (name) VALUES ('ROLE_ANONYMOUS');

ALTER TABLE card ADD created_by BIGINT DEFAULT 1;
ALTER TABLE card CHANGE created_by created_by BIGINT NOT NULL;

ALTER TABLE course ADD created_by BIGINT DEFAULT 1;
ALTER TABLE course CHANGE created_by created_by BIGINT NOT NULL;

ALTER TABLE course_comment ADD created_by BIGINT DEFAULT 1;
ALTER TABLE course_comment CHANGE created_by created_by BIGINT NOT NULL;

ALTER TABLE deck_comment ADD created_by BIGINT DEFAULT 1;
ALTER TABLE deck_comment CHANGE created_by created_by BIGINT NOT NULL;

ALTER TABLE deck ADD created_by BIGINT DEFAULT 1;
ALTER TABLE deck CHANGE created_by created_by BIGINT NOT NULL;

ALTER TABLE folder ADD created_by BIGINT DEFAULT 1;
ALTER TABLE folder CHANGE created_by created_by BIGINT NOT NULL;

ALTER TABLE account ADD created_by BIGINT DEFAULT 1;
ALTER TABLE account CHANGE created_by created_by BIGINT NOT NULL;
