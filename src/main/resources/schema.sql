CREATE TABLE brands (
    brand_id SMALLINT PRIMARY KEY,
    name CHARACTER VARYING
);

CREATE TABLE prices (
    price_id INTEGER PRIMARY KEY,
    brand_id SMALLINT,
    start_date TIMESTAMP,
    end_date TIMESTAMP,
    price_list SMALLINT,
    product_id INTEGER,
    priority SMALLINT,
    price NUMERIC(10, 2),
    curr CHARACTER VARYING,
    FOREIGN KEY (brand_id) REFERENCES brands(brand_id)
);