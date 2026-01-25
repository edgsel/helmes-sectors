-- liquibase formatted sql logicalFilePath:classpath:/db/changelog/2026/1769356615109-hlms-001-insert-sectors.sql
-- changeset edgsel:hlms-001-insert-sectors

INSERT INTO sectors (id, parent_id, name) VALUES

-- level 0
(1, NULL, 'Manufacturing'),
(2, NULL, 'Other'),
(3, NULL, 'Services'),

-- level 1 (Manufacturing)
(4, 1, 'Construction materials'),
(5, 1, 'Electronics and Optics'),
(6, 1, 'Food and Beverage'),
(7, 1, 'Furniture'),
(8, 1, 'Machinery'),
(9, 1, 'Metalworking'),
(10, 1, 'Plastic and Rubber'),
(11, 1, 'Printing'),
(12, 1, 'Textile and Clothing'),
(13, 1, 'Wood'),

-- level 1 (Other)
(53, 2, 'Creative industries'),
(54, 2, 'Energy technology'),
(55, 2, 'Environment'),

-- level 1 (Service)
(56, 3, 'Business services'),
(57, 3, 'Engineering'),
(58, 3, 'Information Technology and Telecommunications'),
(59, 3, 'Tourism'),
(60, 3, 'Translation services'),
(61, 3, 'Transport and Logistics'),

-- level 2 (Manufacturing -> Food and Beverage)
(14, 6, 'Bakery & confectionery products'),
(15, 6, 'Beverages'),
(16, 6, 'Fish & fish products'),
(17, 6, 'Meat & meat products'),
(18, 6, 'Milk & dairy products'),
(19, 6, 'Other (Food and Beverage)'),
(20, 6, 'Sweets & snack food'),

-- level 2 (Manufacturing -> Furniture)
(21, 7, 'Bathroom/Sauna'),
(22, 7, 'Bedroom'),
(23, 7, 'Children''s room'),
(24, 7, 'Kitchen'),
(25, 7, 'Living room'),
(26, 7, 'Office'),
(27, 7, 'Other (Furniture)'),
(28, 7, 'Outdoor'),
(29, 7, 'Project furniture'),

-- level 2 (Manufacturing -> Machinery)
(30, 8, 'Machinery components'),
(31, 8, 'Machinery equipment/tools'),
(32, 8, 'Manufacture of machinery'),
(33, 8, 'Maritime'),
(34, 8, 'Metal structures'),
(35, 8, 'Other (Machinery)'),
(36, 8, 'Repair and maintenance service'),

-- level 2 (Manufacturing -> Metalworking)
(37, 9, 'Construction of metal structures'),
(38, 9, 'Houses and buildings'),
(39, 9, 'Metal products'),
(40, 9, 'Metal works'),

-- level 2 (Manufacturing -> Plastic and Rubber)
(41, 10, 'Packaging'),
(42, 10, 'Plastic goods'),
(43, 10, 'Plastic processing technology'),
(44, 10, 'Plastic profiles'),

-- level 2 (Manufacturing -> Printing)
(45, 11, 'Advertising'),
(46, 11, 'Book/Periodicals printing'),
(47, 11, 'Labelling and packaging printing'),

-- level 2 (Manufacturing -> Textile and Clothing)
(48, 12, 'Clothing'),
(49, 12, 'Textile'),

-- level 2 (Manufacturing -> Wood)
(50, 13, 'Other (Wood)'),
(51, 13, 'Wooden building materials'),
(52, 13, 'Wooden houses'),

-- level 2 (Service -> Information Technology and Telecommunications)
(62, 58, 'Data processing, Web portals, E-marketing'),
(63, 58, 'Programming, Consultancy'),
(64, 58, 'Software, Hardware'),
(65, 58, 'Telecommunications'),

-- level 2 (Service -> Transport and Logistics)
(66, 61, 'Air'),
(67, 61, 'Rail'),
(68, 61, 'Road'),
(69, 61, 'Water'),

-- level 3 (Manufacturing -> Machinery -> Maritime)
(70, 33, 'Aluminium and steel workboats'),
(71, 33, 'Boat/Yacht building'),
(72, 33, 'Ship repair and conversion'),

-- level 3 (Manufacturing -> Metalworking -> Metal works)
(73, 40, 'CNC-machining'),
(74, 40, 'Forgings, Fasteners'),
(75, 40, 'Gas, Plasma, Laser cutting'),
(76, 40, 'MIG, TIG, Aluminium welding'),

-- level 3 (Manufacturing -> Plastic and Rubber -> Plastic processing technology)
(77, 43, 'Blowing'),
(78, 43, 'Moulding'),
(79, 43, 'Plastics welding and processing');