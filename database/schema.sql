-- ============================================================
-- DATABASE SCHEMA
-- ============================================================

-- 1. Tables without dependencies
-- --------------------------------------------

CREATE TABLE admin_users (
    id         VARCHAR(255) PRIMARY KEY,
    email      VARCHAR(255) UNIQUE NOT NULL,
    username   VARCHAR(255) UNIQUE NOT NULL,
    password   VARCHAR(255) NOT NULL,
    fullname   VARCHAR(255) NOT NULL
);

CREATE TABLE customers (
    id         VARCHAR(255) PRIMARY KEY,
    fullname   VARCHAR(255) NOT NULL,
    phone      VARCHAR(255)
);

CREATE TABLE meal_type (
    id          VARCHAR(255) PRIMARY KEY,
    name        VARCHAR(255) UNIQUE NOT NULL,
    description TEXT
);

CREATE TABLE role (
    id          VARCHAR(255) PRIMARY KEY,
    name        VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE menus (
    id          VARCHAR(255) PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    active      BOOLEAN NOT NULL DEFAULT FALSE,
    date        DATE NOT NULL,
    max_meals   INTEGER NOT NULL CHECK (max_meals > 0),
    created_at  TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP
);

-- 2. Tables with dependencies
-- --------------------------------------------

CREATE TABLE meals (
    id          VARCHAR(255) PRIMARY KEY,
    name        VARCHAR(255) NOT NULL UNIQUE,
    price       FLOAT(53) NOT NULL CHECK (price > 0),
    description TEXT,
    image_url   TEXT,
    type_id     VARCHAR(255)   -- FK to meal_type
);

CREATE TABLE menu_meals (
    meal_id     VARCHAR(255) NOT NULL,
    menu_id     VARCHAR(255) NOT NULL,
    PRIMARY KEY (meal_id, menu_id)
);

CREATE TABLE order_items (
    id          VARCHAR(255) PRIMARY KEY,
    quantity    INTEGER NOT NULL CHECK (quantity > 0),
    customer_id VARCHAR(255) NOT NULL,
    meal_id     VARCHAR(255) NOT NULL,
    menu_id     VARCHAR(255) NOT NULL,
    CONSTRAINT uk_order_items_unique UNIQUE (customer_id, menu_id, meal_id)
);

-- 3. Referential integrity constraints (foreign keys)
-- --------------------------------------------

ALTER TABLE meals
    ADD CONSTRAINT fk_meals_type
    FOREIGN KEY (type_id) REFERENCES meal_type(id) ON DELETE SET NULL;

ALTER TABLE menu_meals
    ADD CONSTRAINT fk_menu_meals_meal
    FOREIGN KEY (meal_id) REFERENCES meals(id) ON DELETE CASCADE,
    ADD CONSTRAINT fk_menu_meals_menu
    FOREIGN KEY (menu_id) REFERENCES menus(id) ON DELETE CASCADE;

ALTER TABLE order_items
    ADD CONSTRAINT fk_order_items_customer
    FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE,
    ADD CONSTRAINT fk_order_items_meal
    FOREIGN KEY (meal_id) REFERENCES meals(id) ON DELETE CASCADE,
    ADD CONSTRAINT fk_order_items_menu
    FOREIGN KEY (menu_id) REFERENCES menus(id) ON DELETE CASCADE;

-- 4. Performance indexes
-- --------------------------------------------

CREATE INDEX idx_meals_name      ON meals(name);
CREATE INDEX idx_meals_price     ON meals(price);
CREATE INDEX idx_meals_type_id   ON meals(type_id);
CREATE INDEX idx_menus_date      ON menus(date);
CREATE INDEX idx_menus_active    ON menus(active);
CREATE INDEX idx_menu_meals_meal ON menu_meals(meal_id);
CREATE INDEX idx_order_items_customer ON order_items(customer_id);
CREATE INDEX idx_order_items_meal     ON order_items(meal_id);
CREATE INDEX idx_order_items_menu     ON order_items(menu_id);