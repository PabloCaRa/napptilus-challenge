CREATE TABLE IF NOT EXISTS product_price
(
    id         INTEGER,
    brand_id   INTEGER,
    start_date TIMESTAMP,
    end_date   TIMESTAMP,
    price_list INTEGER,
    product_id INTEGER,
    priority   INTEGER,
    price      NUMERIC(19, 2),
    currency   VARCHAR(3)
);