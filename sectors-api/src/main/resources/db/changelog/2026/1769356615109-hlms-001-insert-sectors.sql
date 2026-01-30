-- liquibase formatted sql logicalFilePath:classpath:/db/changelog/2026/1769356615109-hlms-001-insert-sectors.sql
-- changeset edgsel:hlms-001-insert-sectors

INSERT INTO sectors (id, parent_id, name, sector_level) VALUES

-- level 0
(1, NULL, 'Manufacturing', 0),
(2, NULL, 'Other', 0),
(3, NULL, 'Services', 0),

-- level 1 (Manufacturing)
(4, 1, 'Construction materials', 1),
(5, 1, 'Electronics and Optics', 1),
(6, 1, 'Food and Beverage', 1),
(7, 1, 'Furniture', 1),
(8, 1, 'Machinery', 1),
(9, 1, 'Metalworking', 1),
(10, 1, 'Plastic and Rubber', 1),
(11, 1, 'Printing', 1),
(12, 1, 'Textile and Clothing',1),
(13, 1, 'Wood', 1),

-- level 1 (Other)
(53, 2, 'Creative industries', 1),
(54, 2, 'Energy technology', 1),
(55, 2, 'Environment', 1),

-- level 1 (Service)
(56, 3, 'Business services', 1),
(57, 3, 'Engineering', 1),
(58, 3, 'Information Technology and Telecommunications', 1),
(59, 3, 'Tourism', 1),
(60, 3, 'Translation services', 1),
(61, 3, 'Transport and Logistics', 1),

-- level 2 (Manufacturing -> Food and Beverage)
(14, 6, 'Bakery & confectionery products', 2),
(15, 6, 'Beverages', 2),
(16, 6, 'Fish & fish products', 2),
(17, 6, 'Meat & meat products', 2),
(18, 6, 'Milk & dairy products', 2),
(19, 6, 'Other (Food and Beverage)', 2),
(20, 6, 'Sweets & snack food', 2),

-- level 2 (Manufacturing -> Furniture)
(21, 7, 'Bathroom/Sauna', 2),
(22, 7, 'Bedroom', 2),
(23, 7, 'Children''s room', 2),
(24, 7, 'Kitchen', 2),
(25, 7, 'Living room', 2),
(26, 7, 'Office', 2),
(27, 7, 'Other (Furniture)', 2),
(28, 7, 'Outdoor', 2),
(29, 7, 'Project furniture', 2),

-- level 2 (Manufacturing -> Machinery)
(30, 8, 'Machinery components', 2),
(31, 8, 'Machinery equipment/tools', 2),
(32, 8, 'Manufacture of machinery', 2),
(33, 8, 'Maritime', 2),
(34, 8, 'Metal structures', 2),
(35, 8, 'Other (Machinery)', 2),
(36, 8, 'Repair and maintenance service', 2),

-- level 2 (Manufacturing -> Metalworking)
(37, 9, 'Construction of metal structures', 2),
(38, 9, 'Houses and buildings', 2),
(39, 9, 'Metal products', 2),
(40, 9, 'Metal works', 2),

-- level 2 (Manufacturing -> Plastic and Rubber)
(41, 10, 'Packaging', 2),
(42, 10, 'Plastic goods', 2),
(43, 10, 'Plastic processing technology', 2),
(44, 10, 'Plastic profiles', 2),

-- level 2 (Manufacturing -> Printing)
(45, 11, 'Advertising', 2),
(46, 11, 'Book/Periodicals printing', 2),
(47, 11, 'Labelling and packaging printing', 2),

-- level 2 (Manufacturing -> Textile and Clothing)
(48, 12, 'Clothing', 2),
(49, 12, 'Textile', 2),

-- level 2 (Manufacturing -> Wood)
(50, 13, 'Other (Wood)', 2),
(51, 13, 'Wooden building materials', 2),
(52, 13, 'Wooden houses', 2),

-- level 2 (Service -> Information Technology and Telecommunications)
(62, 58, 'Data processing, Web portals, E-marketing', 2),
(63, 58, 'Programming, Consultancy', 2),
(64, 58, 'Software, Hardware', 2),
(65, 58, 'Telecommunications', 2),

-- level 2 (Service -> Transport and Logistics)
(66, 61, 'Air', 2),
(67, 61, 'Rail', 2),
(68, 61, 'Road', 2),
(69, 61, 'Water', 2),

-- level 3 (Manufacturing -> Machinery -> Maritime)
(70, 33, 'Aluminium and steel workboats', 3),
(71, 33, 'Boat/Yacht building', 3),
(72, 33, 'Ship repair and conversion', 3),

-- level 3 (Manufacturing -> Metalworking -> Metal works)
(73, 40, 'CNC-machining', 3),
(74, 40, 'Forgings, Fasteners', 3),
(75, 40, 'Gas, Plasma, Laser cutting', 3),
(76, 40, 'MIG, TIG, Aluminium welding', 3),

-- level 3 (Manufacturing -> Plastic and Rubber -> Plastic processing technology)
(77, 43, 'Blowing', 3),
(78, 43, 'Moulding', 3),
(79, 43, 'Plastics welding and processing', 3);
