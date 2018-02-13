create table card_image (
  card_image_id bigint not null auto_increment,
  image_base64 LONGTEXT not null,
  card_id bigint not null,
  primary key (card_image_id)
);

alter table card_image add constraint FK41h5m0do62my9najykypfb1ym foreign key (card_id) references card (card_id)
