create table points_transaction (
  transaction_id bigint not null auto_increment,
  reference_id bigint not null,
  date_creation datetime not null,
  user_from bigint not null,
  user_to bigint not null,
  points int not null,
  primary key (transaction_id)
);

  alter table points_transaction add constraint POINTS_TRANSACTION_USER_FROM_FK foreign key (user_from) references user (user_id);
  alter table points_transaction add constraint POINTS_TRANSACTION_USER_TO_FK foreign key (user_to) references user (user_id);
  alter table course_ownership drop foreign key Course_Ownership_Transaction_FK;
  alter table deck_ownership drop foreign key Deck_Ownership_Transaction_FK;

  insert into points_transaction (reference_id, date_creation, user_from, user_to, points) values (1, '2018-11-11 17:25:46', 1, 3, 1452);
  insert into points_transaction (reference_id, date_creation, user_from, user_to, points) values (2, '2018-10-19 12:58:15', 2, 1, 2215);
  insert into points_transaction (reference_id, date_creation, user_from, user_to, points) values (3, '2018-09-13 15:27:54', 4, 2, 1897);
  insert into points_transaction (reference_id, date_creation, user_from, user_to, points) values (4, '2018-10-22 20:25:09', 2, 3, 1254);
