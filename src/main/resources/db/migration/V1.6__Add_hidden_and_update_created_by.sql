ALTER TABLE deck ADD hidden bit DEFAULT FALSE;

UPDATE deck SET created_by = user_id WHERE deck_id > 4;
