CREATE TABLE course_price (
price_id bigint not null auto_increment,
price integer,
course_id bigint,
primary key (price_id)
);
alter table course_price add constraint FK4leaja1jtefsdfaiqqk4yuxna foreign key (course_id) references course (course_id);