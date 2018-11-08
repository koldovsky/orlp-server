CREATE TABLE course_price (
price_id bigint not null auto_increment,
price bigint DEFAULT 0,
course_id bigint,
primary key (price_id)
);

alter table course add course_price_id bigint;
alter table course_price add constraint COURSE_PRICE_COURSE_FK foreign key (course_id) references course (course_id);
alter table course add constraint COURSE_COURSE_PRICE_FK foreign key (course_price_id) references course_price(price_id);