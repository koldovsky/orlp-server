create table deck_price (
    deck_price_id bigint not null auto_increment,
    deck_id bigint,
    deck_price integer not null,
    primary key (deck_price_id)
)