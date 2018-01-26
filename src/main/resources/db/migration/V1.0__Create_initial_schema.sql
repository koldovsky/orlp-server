create table account (
  account_id bigint not null auto_increment,
  authenticationtype varchar(8) not null,
  cards_number int default 10 not null,
  deactivated bit NOT null,
  email varchar(255) not null,
  lastpasswordresetdate datetime not null,
  learning_regime varchar(45) default 'CARDS_POSTPONING_USING_SPACED_REPETITION' not null,
  password varchar(255),
  status varchar(255) not null,
  primary key (account_id)
);

create table account_authority (
  account_id bigint not null,
  authority_id bigint not null,
  primary key (account_id, authority_id)
);

create table audit (
  audit_id bigint not null auto_increment,
  account_email varchar(255) not null,
  action varchar(255) not null,
  ip_address varchar(255) not null,
  role varchar(255) not null,
  time datetime not null, primary key (audit_id)
);

create table authority (
  authority_id bigint not null auto_increment,
  name varchar(50) not null,
  primary key (authority_id)
);

create table card (
  card_id bigint not null auto_increment,
  answer LONGTEXT not null,
  question LONGTEXT not null,
  rating double precision,
  title varchar(255),
  deck_id bigint, primary key (card_id)
);

create table card_rating (
  rating_id bigint not null auto_increment,
  account_email varchar(255) not null,
  rating integer not null,
  card_id bigint,
  primary key (rating_id)
);

create table category (
  category_id bigint not null auto_increment,
  description varchar(255) not null,
  name varchar(255) not null,
  image bigint,
  primary key (category_id)
);

create table course (
  course_id bigint not null auto_increment,
  description varchar(255) not null,
  name varchar(255) not null,
  published bit,
  rating double precision,
  category_id bigint,
  image bigint,
  user_id bigint,
  primary key (course_id)
);

create table course_decks (
  course_id bigint not null,
  deck_id bigint not NULL
);

create table course_rating (
  rating_id bigint not null auto_increment,
  account_email varchar(255) not null,
  rating integer not null,
  course_id bigint,
  primary key (rating_id)
);

create table course_comment (
  comment_id bigint not null,
  comment_date datetime not null,
  comment_text LONGTEXT not null,
  parent_comment_id bigint,
  person_id bigint,
  course_id bigint,
  primary key (comment_id)
);

create table deck (
  deck_id bigint not null auto_increment,
  description varchar(255) not null,
  name varchar(255) not null,
  rating double precision,
  synthax varchar(255),
  category_id bigint,
  user_id bigint,
  primary key (deck_id)
);

create table deck_rating (
  rating_id bigint not null auto_increment,
  account_email varchar(255) not null,
  rating integer not null,
  deck_id bigint,
  primary key (rating_id)
);

create table deck_comment (
  comment_id bigint not null,
  comment_date datetime not null,
  comment_text LONGTEXT not null,
  parent_comment_id bigint,
  person_id bigint,
  deck_id bigint,
  primary key (comment_id)
);

create table folder (
  folder_id bigint not null auto_increment,
  primary key (folder_id)
);

create table folder_decks (
  deck_id bigint not null,
  folder_id bigint not null,
  primary key (folder_id, deck_id)
);

create table hibernate_sequences (
  sequence_name varchar(255) not null,
  sequence_next_hi_value bigint,
  primary key (sequence_name)
);

create table image (
  image_id bigint not null auto_increment,
  imagebase64 LONGTEXT,
  is_used bit,
  size bigint not null,
  type varchar(255) not null,
  user_id bigint,
  primary key (image_id)
);

create table person (
  person_id bigint not null auto_increment,
  first_name varchar(255),
  image varchar(255),
  imagebase64 LONGTEXT,
  last_name varchar(255),
  imagetype varchar(6) not null,
  primary key (person_id)
);

create table remembering_level (
  id bigint not null auto_increment, name varchar(255),
  number_of_postponed_days integer not null,
  order_number integer not null,
  account_id bigint not null,
  primary key (id)
);

create table user (
  user_id bigint not null auto_increment,
  account_id bigint,
  folder_id bigint,
  person_id bigint,
  primary key (user_id)
);

create table user_card_queue (
  user_card_queue_id bigint not null auto_increment,
  card_date datetime not null,
  card_id bigint not null,
  date_to_repeat datetime,
  deck_id bigint not null,
  status varchar(255),
  user_id bigint not null,
  remembering_level_id bigint,
  primary key (user_card_queue_id)
);

create table user_courses (
  user_id bigint not null,
  course_id bigint not null,
  primary key (user_id, course_id)
);

alter table account add constraint UK_q0uja26qgu1atulenwup9rxyr unique (email);
alter table account_authority add constraint FKr21r28dhxvddh22qps7xc6q2u foreign key (authority_id) references authority (authority_id);
alter table account_authority add constraint FKsglqde2oirunehlexcjkt68eb foreign key (account_id) references account (account_id);
alter table card add constraint FK6k0or7dj9m5qhnshnk9fpg8r1 foreign key (deck_id) references deck (deck_id);
alter table card_rating add constraint FKhb5ur3trr26ue0x9qj5gt4ree foreign key (card_id) references card (card_id);
alter table category add constraint FKhw2fmfnv5dkws6jk2xhhnd2je foreign key (image) references image (image_id);
alter table course add constraint FKkyes7515s3ypoovxrput029bh foreign key (category_id) references category (category_id);
alter table course add constraint FK7gq3ss1clgd03rfywy9m7ftxj foreign key (image) references image (image_id);
alter table course add constraint FKo3767wbj6ow5axv38qej0gxo9 foreign key (user_id) references user (user_id);
alter table course_decks add constraint FKfr3nphovh3g228li7g444vht7 foreign key (deck_id) references deck (deck_id);
alter table course_decks add constraint FKgft32qnh9podvm1kfje89jfsp foreign key (course_id) references course (course_id);
alter table course_rating add constraint FKgaeoah8dmtu0mtofaxogd6kls foreign key (course_id) references course (course_id);
alter table course_comment add constraint FKi2x4qwjfqowv1gxbt2ybxo9il foreign key (course_id) references course (course_id);
alter table course_comment add constraint FK_g4jcv3d0jbr03qaop0uj639db foreign key (person_id) references person (person_id);
alter table deck add constraint FKp9mag3654d10bc1m49su5cwst foreign key (category_id) references category (category_id);
alter table deck add constraint FKceph6k63k3j52jdb1qdc8imkc foreign key (user_id) references user (user_id);
alter table deck_rating add constraint FKr6qt61nmk9kvq02nm68a3mw6m foreign key (deck_id) references deck (deck_id);
alter table deck_comment add constraint FKnt8wb6py85pmvl9tlynp1yesf foreign key (deck_id) references deck (deck_id);
alter table deck_comment add constraint FK_2jg4jxrbo9x2ec8asc96x3o5y foreign key (person_id) references person (person_id);
alter table folder_decks add constraint FK1ekad1u1f0n2t3kebjd1qq09m foreign key (folder_id) references folder (folder_id);
alter table folder_decks add constraint FKpk4vtok5r6bqxukujom4eux3b foreign key (deck_id) references deck (deck_id);
alter table image add constraint FKlxnnh0ir05khn8iu9tgwh1yyk foreign key (user_id) references user (user_id);
alter table remembering_level add constraint FKmoqxuovmwp60cmsqc9xefrrup foreign key (account_id) references account (account_id);
alter table user add constraint FKc3b4xfbq6rbkkrddsdum8t5f0 foreign key (account_id) references account (account_id);
alter table user add constraint FK4vm5ne5vlite2lekpiqvxgra1 foreign key (folder_id) references folder (folder_id);
alter table user add constraint FKir5g7yucydevmmc84i788jp79 foreign key (person_id) references person (person_id);
alter table user_card_queue add constraint FK11d0tw07pk7yvxw0ofcns8tws foreign key (remembering_level_id) references remembering_level (id);
alter table user_courses add constraint FKlqgnqmcg0mn2ci98xag6vxpdq foreign key (course_id) references course (course_id);
alter table user_courses add constraint FK4leaja1jtelxjs2iqqk4yuxna foreign key (user_id) references user (user_id);
