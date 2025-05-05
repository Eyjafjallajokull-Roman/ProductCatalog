-- Align category ID sequence
SELECT setval('category_id_seq', (SELECT COALESCE(MAX(id), 0) FROM category) + 1, false);

-- Align user ID sequence
SELECT setval('app_user_id_seq', (SELECT COALESCE(MAX(id), 0) FROM app_user) + 1, false);

-- Align product ID sequence
SELECT setval('product_id_seq', (SELECT COALESCE(MAX(id), 0) FROM product) + 1, false);