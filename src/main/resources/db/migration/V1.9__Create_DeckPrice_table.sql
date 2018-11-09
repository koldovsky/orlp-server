create table deck_price (
    deck_price_id bigint not null auto_increment,
    deck_id bigint,
    deck_price integer default null,
    primary key (deck_price_id)
);

alter table deck add deck_price_id bigint;
alter table deck_price add constraint DECK_PRICE_DECK_FK foreign key (deck_id) references deck (deck_id);
alter table deck add constraint DECK_DECK_PRICE_FK foreign key (deck_price_id) references deck_price (deck_price_id);
