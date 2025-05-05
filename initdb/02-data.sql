-- === CATEGORIES ===
INSERT INTO category (id, name, parent_id)
VALUES (1, 'Electronics', NULL),
       (2, 'Smartphones', 1),
       (3, 'Laptops', 1),
       (4, 'Appliances', NULL),
       (5, 'Kitchen', 4),
       (6, 'Android', 2),
       (7, 'Budget Phones', 6),
       (8, 'Blenders', 5),
       (9, 'Ovens', 5),
       (10, 'Climate Control', 4),
       (11, 'Air Conditioning', 10)
ON CONFLICT (id) DO NOTHING;

-- === USERS ===
-- adminpass: $2a$10$6OCw3h1HnmRz6K8pSXLceOmhL2IaA2RLr7gvb/kZcK7C9FuIfrbtm
-- userpass: $2a$10$fLJ1P6U/Y5xiY8s5ddjdYeOiwVLEgNj1vKs7tgjW5odfZ5f/zc7Da

INSERT INTO app_user (id, username, password, role)
VALUES (1, 'admin', '$2a$10$Vvw4yNvyH.m5kQL06smJ9.Uh5M3ScImCNX64srvyo9Fh50.U9C3Cq', 'ROLE_ADMIN'),
       (2, 'user', '$2a$10$eGZtCv.7Xlz4A29sHtDRnOTUeJ0CZ7evzxfZQavtmgS/urIKPiNRa', 'ROLE_USER')
ON CONFLICT (id) DO NOTHING;

-- === PRODUCTS ===
INSERT INTO product (id, name, description, price, currency, category_id, deleted)
VALUES (1, 'iPhone 14', 'Latest Apple smartphone', 999.99, 'EUR', 2, false),
       (2, 'MacBook Air', 'Lightweight laptop with M2 chip', 1199.00, 'EUR', 3, false),
       (3, 'Samsung Galaxy S23', 'Android flagship phone', 849.50, 'EUR', 6, false),
       (4, 'Blender X100', 'High-power kitchen blender', 130.00, 'EUR', 8, false),
       (5, 'Air Conditioner A++', 'Energy efficient cooling', 780.25, 'EUR', 11, false),
       (6, 'Budget Droid Mini', 'Affordable Android phone', 199.99, 'EUR', 7, false),
       (7, 'Smart Oven Pro', 'App-controlled oven', 499.00, 'EUR', 9, true),
       (8, 'Smartphone Stand', 'Universal smartphone holder', 25.00, 'EUR', 2, false),
       (9, 'Laptop Stand', 'Ergonomic laptop stand', 45.00, 'EUR', 3, false),
       (10, 'Air Purifier', 'Cleans the air in your home', 299.99, 'EUR', 10, false)
ON CONFLICT (id) DO NOTHING;