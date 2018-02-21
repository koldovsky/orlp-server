ALTER TABLE card MODIFY question NVARCHAR(255);
create index card_index on card (question, title);
create index category_index on category (name, description);
create index course_index on course (name, description);
create index deck_index on deck (name, description);
