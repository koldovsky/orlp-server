create table deck_ownership (
  transaction_id BIGINT not null auto_increment,
  reference_id BIGINT not null,
  date_creation DATE not null,
  user_id BIGINT not null,
  deck_id BIGINT not null,
  primary key (transaction_id)
);

create table course_ownership (
  transaction_id BIGINT not null auto_increment,
  reference_id BIGINT not null,
  date_creation DATE not null,
  user_id BIGINT not null,
  course_id BIGINT not null,
  primary key (transaction_id)
);

alter table deck_ownership add constraint Deck_Ownership_Transaction_FK foreign key (reference_id) references deck_ownership (transaction_id);
alter table deck_ownership add constraint Deck_Ownership_User_FK foreign key (user_id) references user (user_id);
alter table deck_ownership add constraint Deck_Ownership_Deck_FK foreign key (deck_id) references deck (deck_id);

alter table course_ownership add constraint Course_Ownership_Transaction_FK foreign key (reference_id) references course_ownership (transaction_id);
alter table course_ownership add constraint Course_Ownership_User_FK foreign key (user_id) references user (user_id);
alter table course_ownership add constraint Course_Ownership_Deck_FK foreign key (course_id) references course (course_id);
