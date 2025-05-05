CREATE TABLE category
(
    id        BIGSERIAL PRIMARY KEY,
    name      VARCHAR(255) NOT NULL,
    parent_id BIGINT,
    CONSTRAINT fk_category_parent FOREIGN KEY (parent_id) REFERENCES category (id) ON DELETE SET NULL
);

CREATE TABLE app_user
(
    id       BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255)        NOT NULL,
    role     VARCHAR(50)         NOT NULL
);

CREATE TABLE product
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(255)   NOT NULL,
    description VARCHAR(1000),
    price       NUMERIC(10, 2) NOT NULL,
    currency    VARCHAR(10)    NOT NULL,
    deleted     BOOLEAN        NOT NULL DEFAULT FALSE,
    category_id BIGINT         NOT NULL,
    CONSTRAINT fk_product_category FOREIGN KEY (category_id) REFERENCES category (id) ON DELETE CASCADE
);