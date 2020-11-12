    alter table if exists LineItems;
    drop constraint if exists FKbd50qos3gul5b1t3h6yito6es;
    drop table if exists LineItems cascade;
    drop table if exists Orders cascade;
    drop table if exists OutboxEvent cascade;
    drop sequence if exists hibernate_sequence;
    create sequence hibernate_sequence start 1 increment 1;
    create table LineItems (
       id int8 not null,
        item varchar(255),
        name varchar(255),
        orderId varchar(255) not null,
        primary key (id)
    );
    create table Orders (
       orderId varchar(255) not null,
        loyaltyMemberId varchar(255),
        orderSource varchar(255),
        timestamp timestamp,
        primary key (orderId)
    );
    create table OutboxEvent (
       id uuid not null,
        aggregatetype varchar(255) not null,
        aggregateid varchar(255) not null,
        type varchar(255) not null,
        timestamp timestamp not null,
        payload varchar(8000),
        primary key (id)
    );
    alter table if exists LineItems
       add constraint FKbd50qos3gul5b1t3h6yito6es
       foreign key (orderId)
       references Orders;
