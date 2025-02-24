INSERT INTO categories (name, description) VALUES ('N/A', 'Sin categoría')
INSERT INTO categories (name, description) VALUES ('Bebidas', null)
INSERT INTO categories (name, description) VALUES ('Botanas', null)
INSERT INTO categories (name, description) VALUES ('Dulces', null)
INSERT INTO categories (name, description) VALUES ('Helados', null)
INSERT INTO categories (name, description) VALUES ('Lácteos', null)
INSERT INTO categories (name, description) VALUES ('Panes', null)
INSERT INTO categories (name, description) VALUES ('Vinos', null)


INSERT INTO subcategories (name, description, category_id) VALUES ('N/A', 'Sin subcategoría', 1)
INSERT INTO subcategories (name, description, category_id) VALUES ('Agua natural', null, 2)
INSERT INTO subcategories (name, description, category_id) VALUES ('Agua mineral', null, 2)
INSERT INTO subcategories (name, description, category_id) VALUES ('Jugos', null, 2)
INSERT INTO subcategories (name, description, category_id) VALUES ('Refresco', null, 2)
INSERT INTO subcategories (name, description, category_id) VALUES ('Cacahuates', null, 3)
INSERT INTO subcategories (name, description, category_id) VALUES ('Nueces', null, 3)
INSERT INTO subcategories (name, description, category_id) VALUES ('Papas fritas', null, 3)
INSERT INTO subcategories (name, description, category_id) VALUES ('Semillas', null, 3)
INSERT INTO subcategories (name, description, category_id) VALUES ('Chicles', null, 4)
INSERT INTO subcategories (name, description, category_id) VALUES ('Chocolate', null, 4)
INSERT INTO subcategories (name, description, category_id) VALUES ('Gomitas', null, 4)
INSERT INTO subcategories (name, description, category_id) VALUES ('Paletas', null, 4)
INSERT INTO subcategories (name, description, category_id) VALUES ('Helado base agua', null, 5)
INSERT INTO subcategories (name, description, category_id) VALUES ('Helado base leche', null, 5)
INSERT INTO subcategories (name, description, category_id) VALUES ('Leche', null, 6)
INSERT INTO subcategories (name, description, category_id) VALUES ('Queso', null, 6)
INSERT INTO subcategories (name, description, category_id) VALUES ('Yogurt', null, 6)
INSERT INTO subcategories (name, description, category_id) VALUES ('Pan dulce', null, 7)
INSERT INTO subcategories (name, description, category_id) VALUES ('Pan de caja', null, 7)
INSERT INTO subcategories (name, description, category_id) VALUES ('Vino blanco', null, 8)
INSERT INTO subcategories (name, description, category_id) VALUES ('Vino rosado', null, 8)

INSERT INTO products (name, description, size, price, barcode, subcategory_id) VALUES ('Estrella Marina', 'Agua embotellada', '500 ml', 8.00, '7501086801121', 2)
INSERT INTO products (name, description, size, price, barcode, subcategory_id) VALUES ('Estrella Marina', 'Agua embotellada', '1 L', 14.00, '7501086801046', 2)
INSERT INTO products (name, description, size, price, barcode, subcategory_id) VALUES ('Estrella Marina', 'Agua embotellada', '2 L', 18.00, '7501086801015', 2)
INSERT INTO products (name, description, size, price, barcode, subcategory_id) VALUES ('Rana Mandarina', 'Bebida carbonatada sabor mandarina', '600 ml', 18.00, '7501441606118', 5)
INSERT INTO products (name, description, size, price, barcode, subcategory_id) VALUES ('Rana Manzana', 'Bebida carbonatada sabor manzana', '600 ml', 18.00, '7501031360024', 5)
INSERT INTO products (name, description, size, price, barcode, subcategory_id) VALUES ('Rana de Nuez', 'Bebida carbonatada sabor cola', '600 ml', 20.00, '7503006897016', 5)
INSERT INTO products (name, description, size, price, barcode, subcategory_id) VALUES ('Rana Toronja', 'Bebida carbonatada sabor toronja', '600 ml', 18.00, '7501071120183', 5)
INSERT INTO products (name, description, size, price, barcode, subcategory_id) VALUES ('Ardillas Enchiladas', 'Cacahuates enchilados', '60 g', 24.00, '7500478036301', 6)
INSERT INTO products (name, description, size, price, barcode, subcategory_id) VALUES ('Ardillas Japonesas', 'Cacahuates japoneses', '60 g', 20.00, '7500478019014', 6)
INSERT INTO products (name, description, size, price, barcode, subcategory_id) VALUES ('Ardillas Saladas', 'Cacahuates salados', '60 g', 21.00, '7501030459941', 6)
INSERT INTO products (name, description, size, price, barcode, subcategory_id) VALUES ('Chihuahuas Adobados', 'Papas fritas adobadas', '50 g', 25.00, '7501011101463', 8)
INSERT INTO products (name, description, size, price, barcode, subcategory_id) VALUES ('Chihuahuas de Queso', 'Papas fritas sabor queso', '50 g', 25.00, '7501011104099', 8)
INSERT INTO products (name, description, size, price, barcode, subcategory_id) VALUES ('Chihuahuas Salados', 'Papas fritas con sal', '50 g', 22.00, '7501011101436', 8)
INSERT INTO products (name, description, size, price, barcode, subcategory_id) VALUES ('Oso Almendrado', 'Barra de chocolate con leche y almendras', '40 g', 36.00, '7501024544295', 11)
INSERT INTO products (name, description, size, price, barcode, subcategory_id) VALUES ('Oso Amargo', 'Barra de chocolate amargo', '40 g', 30.00, '7501024501007', 11)
INSERT INTO products (name, description, size, price, barcode, subcategory_id) VALUES ('Oso Blanco', 'Dulce sabor a chocolate blanco', '40 g', 34.00, '7501024513635', 11)
INSERT INTO products (name, description, size, price, barcode, subcategory_id) VALUES ('Oso de Leche', 'Barra de chocolate con leche', '40 g', 35.00, '7501024511310', 11)
INSERT INTO products (name, description, size, price, barcode, subcategory_id) VALUES ('Oso Semiamargo', 'Barra de chocolate semiamargo', '40 g', 32.00, '7501024524556', 11)
INSERT INTO products (name, description, size, price, barcode, subcategory_id) VALUES ('Tortugas de Mango', 'Golosina masticable dulce sabor mango con chile', ' 100 g', 28.00, '7507528042073', 12)
INSERT INTO products (name, description, size, price, barcode, subcategory_id) VALUES ('Tortugas de Sandía', 'Golosina masticable dulce sabor sandía con chile', ' 100 g', 28.00, '7502246442376', 12)
INSERT INTO products (name, description, size, price, barcode, subcategory_id) VALUES ('Vaca de Fresa', 'Helado sabor fresa', '1 L', 50.00, '7506306417779', 15)
INSERT INTO products (name, description, size, price, barcode, subcategory_id) VALUES ('Vaca de Galleta', 'Helado sabor galleta', '1 L', 48.00, '7501791610568', 15)
INSERT INTO products (name, description, size, price, barcode, subcategory_id) VALUES ('Vaca Napolitana', 'Helado sabor napolitano', '1 L', 45.00, '7501130902194', 15)

INSERT INTO inventory (quantity_available, minimum_stock, maximum_stock, product_id) VALUES (202, 120, 300, 1)
INSERT INTO inventory (quantity_available, minimum_stock, maximum_stock, product_id) VALUES (223, 150, 375, 2)
INSERT INTO inventory (quantity_available, minimum_stock, maximum_stock, product_id) VALUES (102, 80, 200, 3)
INSERT INTO inventory (quantity_available, minimum_stock, maximum_stock, product_id) VALUES (173, 150, 375, 4)
INSERT INTO inventory (quantity_available, minimum_stock, maximum_stock, product_id) VALUES (303, 250, 625, 5)
INSERT INTO inventory (quantity_available, minimum_stock, maximum_stock, product_id) VALUES (245, 180, 450, 6)
INSERT INTO inventory (quantity_available, minimum_stock, maximum_stock, product_id) VALUES (202, 120, 300, 7)
INSERT INTO inventory (quantity_available, minimum_stock, maximum_stock, product_id) VALUES (266, 260, 650, 8)
INSERT INTO inventory (quantity_available, minimum_stock, maximum_stock, product_id) VALUES (233, 220, 550, 9)
INSERT INTO inventory (quantity_available, minimum_stock, maximum_stock, product_id) VALUES (322, 250, 625, 10)
INSERT INTO inventory (quantity_available, minimum_stock, maximum_stock, product_id) VALUES (339, 260, 650, 11)
INSERT INTO inventory (quantity_available, minimum_stock, maximum_stock, product_id) VALUES (248, 220, 550, 12)
INSERT INTO inventory (quantity_available, minimum_stock, maximum_stock, product_id) VALUES (310, 280, 700, 13)
INSERT INTO inventory (quantity_available, minimum_stock, maximum_stock, product_id) VALUES (271, 230, 575, 14)
INSERT INTO inventory (quantity_available, minimum_stock, maximum_stock, product_id) VALUES (250, 160, 400, 15)
INSERT INTO inventory (quantity_available, minimum_stock, maximum_stock, product_id) VALUES (238, 210, 525, 16)
INSERT INTO inventory (quantity_available, minimum_stock, maximum_stock, product_id) VALUES (250, 220, 550, 17)
INSERT INTO inventory (quantity_available, minimum_stock, maximum_stock, product_id) VALUES (190, 180, 450, 18)
INSERT INTO inventory (quantity_available, minimum_stock, maximum_stock, product_id) VALUES (175, 170, 425, 19)
INSERT INTO inventory (quantity_available, minimum_stock, maximum_stock, product_id) VALUES (231, 190, 475, 20)
INSERT INTO inventory (quantity_available, minimum_stock, maximum_stock, product_id) VALUES (76, 60, 150, 21)
INSERT INTO inventory (quantity_available, minimum_stock, maximum_stock, product_id) VALUES (57, 40, 100, 22)
INSERT INTO inventory (quantity_available, minimum_stock, maximum_stock, product_id) VALUES (113, 70, 175, 23)
