create table ownership (
  transaction_id BIGINT not null auto_increment,
  reference_id BIGINT not null,
  date_creation DATE not null,
  user_id BIGINT not null,
  discriminator VARCHAR(20) not null,
  deck_id BIGINT null,
  course_id BIGINT null,
  primary key (transaction_id)
);

alter table ownership add constraint Ownership_Transaction_FK foreign key (reference_id) references ownership (transaction_id);
alter table ownership add constraint Ownership_User_FK foreign key (user_id) references user (user_id);
alter table ownership add constraint Ownership_Course_FK foreign key (course_id) references course (course_id);
alter table ownership add constraint Ownership_Deck_FK foreign key (deck_id) references deck (deck_id);
