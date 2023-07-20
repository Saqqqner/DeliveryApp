-- Создание таблицы customer
create table customer
(
    id      bigserial primary key,
    name    varchar  not null,
    email   varchar   not null unique,
    address varchar  not null,
    password varchar  not null,
    role varchar  not null
);

-- Создание таблицы products
create table products
(
    id         bigserial primary key,
    name       varchar   not null unique,
    price      numeric(10, 2) not null,
    created_at timestamp default CURRENT_TIMESTAMP not null,
    updated_at timestamp default CURRENT_TIMESTAMP not null,
    stock      bigint not null
);

-- Создание таблицы orders
create table orders
(
    id          serial primary key,
    customer_id integer not null references customer,
    order_date  timestamp default CURRENT_TIMESTAMP not null
);

-- Создание таблицы order_items
create table order_items
(
    id         serial primary key,
    order_id   integer not null references orders,
    product_id integer not null references products,
    quantity   integer not null,
    price      numeric(10, 2) not null
);
