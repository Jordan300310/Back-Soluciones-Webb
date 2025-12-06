INSERT INTO proveedor (razon_social, ruc, cel, email, estado) VALUES
('Seguridad Industrial Andina S.A.C.', '20604587231', '987654321', 'contacto@segandina.com', TRUE),
('Protek Perú E.I.R.L.', '20587456321', '956123789', 'ventas@protekperu.com', TRUE),
('Distribuidora EPP Global S.R.L.', '20457896542', '999888777', 'info@eppglobal.com', TRUE),
('Industrias Segurimax S.A.', '20607896510', '912345678', 'comercial@segurimax.com', TRUE),
('Importadora SafeWork S.A.C.', '20369874125', '987321654', 'soporte@safework.com', TRUE);

INSERT INTO producto (nombre_producto, descripcion, precio, stock, marca, categoria, id_proveedor, imagen_url, estado) VALUES
('Casco de seguridad MSA V-Gard', 'Casco dieléctrico tipo I con suspensión Fas-Trac III', 89.90, 60, 'MSA', 'Protección craneal', 1, 'https://s7d9.scene7.com/is/image/minesafetyappliances/V-GardHardHatCapStyle_000060001300001000?$Home%20Market%20Card$', TRUE),
('Guantes de nitrilo reforzado', 'Guantes resistentes a químicos, abrasión y cortes leves', 12.50, 200, '3M', 'Protección de manos', 2, 'https://www.segutecnica.com/images/000000000001598730025steelpro-guantes-multiflex-nitrilo-espumado-respirable-30100078064-segutecnica.png', TRUE),
('Mascarilla respirador 3M 6200', 'Respirador reutilizable con cartuchos intercambiables para vapores y gases', 149.00, 80, '3M', 'Protección respiratoria', 2, 'https://alaska.pe/wp-content/uploads/2023/07/Respirador-media-cara-6200-3M.jpg', TRUE),
('Lentes de seguridad UVEX Astrospec', 'Lentes de policarbonato antiempañantes y protección UV', 45.00, 120, 'UVEX', 'Protección ocular', 3, 'https://b2btreckpe.vtexassets.com/arquivos/ids/174468-800-auto?v=638893239725070000&width=800&height=auto&aspect=true', TRUE),
('Botas dieléctricas PVC', 'Botas impermeables con puntera de acero, resistentes a descargas eléctricas', 179.00, 40, 'Segurimax', 'Protección podal', 4, 'https://promart.vteximg.com.br/arquivos/ids/7879561-700-700/11308.jpg?v=638455440556870000', TRUE),
('Arnés de seguridad de cuerpo completo', 'Arnés con 5 puntos de anclaje y hebillas de acero galvanizado', 299.00, 25, 'SafeWork', 'Protección anticaídas', 5, 'https://m.media-amazon.com/images/I/81QgUXgRLtL._AC_SL1500_.jpg', TRUE),
('Orejeras 3M Peltor Optime 105', 'Orejeras con aislamiento acústico de alta atenuación', 99.00, 70, '3M', 'Protección auditiva', 2, 'https://media.falabella.com/falabellaPE/127680107_01/w=1500,h=1500,fit=pad', TRUE),
('Chaleco reflectivo clase 2', 'Chaleco de seguridad color verde fosforescente con cintas reflectivas', 35.00, 150, 'Protek', 'Ropa de seguridad', 2, 'https://seguridadglobal.com.ar/wp-content/uploads/2022/01/Chaleco-reflectivo-Clase-2-Prisma.jpg', TRUE),
('Pantalla facial completa', 'Visor facial de policarbonato con banda ajustable para protección contra salpicaduras', 59.00, 90, 'Andina Safety', 'Protección facial', 1, 'https://siasuministros.com/1834748-big_default/pantalla-facial-superface-transparente-safetop-79300.jpg', TRUE),
('Cono de seguridad 75 cm', 'Cono naranja de PVC con base negra y cinta reflectiva', 69.00, 100, 'SafeWork', 'Señalización y control', 5, 'https://www.segutecnica.com/images/000000000000000091041dps-cono-pe-base-de-goma-economico-750-rigido-2-bandas-reflectivas-segutecnica.png', TRUE);

INSERT INTO usuario (username, password, estado)
VALUES ('admin', '$2a$12$U.MOTHtpqePlMgkff70tW.V0/UKjdXhggehh.3e2o5hmlbSFi9L9u', true); -- password: 123

SELECT id_usuario FROM usuario WHERE username = 'admin';

INSERT INTO empleado (
  id_usuario,
  nom, apat, amat,
  dni, cel, email, fen,
  cargo,
  sueldo,
  estado
) VALUES (
  1,
  'Admin', 'Admin', 'Admin',
  '00000000',
  '999999999',
  'admin@tuapp.com',
  '1990-01-01',
  'ADMIN',
  0,
  true
);
