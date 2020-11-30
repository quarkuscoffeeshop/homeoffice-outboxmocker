    DROP SCHEMA IF EXISTS coffeeshop;
    CREATE SCHEMA coffeeshop;
    alter table if exists coffeeshop.LineItems
    drop constraint if exists FKbd50qos3gul5b1t3h6yito6es;
    drop table if exists coffeeshop.LineItems cascade;
    drop table if exists coffeeshop.Orders cascade;
    drop table if exists coffeeshop.OutboxEvent cascade;
    drop sequence if exists coffeeshop.hibernate_sequence;
    create sequence coffeeshop.hibernate_sequence start 1 increment 1;
    create table coffeeshop.LineItems (
       id int8 not null,
        item varchar(255),
        name varchar(255),
        orderId varchar(255) not null,
        primary key (id)
    );
    create table coffeeshop.Orders (
       orderId varchar(255) not null,
        loyaltyMemberId varchar(255),
        orderSource varchar(255),
        timestamp timestamp,
        primary key (orderId)
    );
    create table coffeeshop.OutboxEvent (
       id uuid not null,
        aggregatetype varchar(255) not null,
        aggregateid varchar(255) not null,
        type varchar(255) not null,
        timestamp timestamp not null,
        payload varchar(8000),
        primary key (id)
    );
    alter table if exists coffeeshop.LineItems
       add constraint FKbd50qos3gul5b1t3h6yito6es
       foreign key (orderId)
       references coffeeshop.Orders;
