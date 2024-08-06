INSERT INTO authors (first_name, last_name, biography) VALUES ('Gabriel', 'García Márquez', 'Gabriel García Márquez fue un escritor, novelista, cuentista, guionista y periodista colombiano. Ganador del Premio Nobel de Literatura en 1982.');
INSERT INTO authors (first_name, last_name, biography) VALUES  ('J.K.', 'Rowling', 'J.K. Rowling es una escritora, productora de cine y guionista británica, conocida por ser la autora de la famosa serie de libros Harry Potter.');
INSERT INTO authors (first_name, last_name, biography) VALUES  ('Dan', 'Brown', 'Dan Brown es un escritor estadounidense conocido por sus novelas de misterio y suspenso, entre las que se encuentra El código Da Vinci.');
INSERT INTO authors (first_name, last_name, biography) VALUES  ('Haruki', 'Murakami', 'Haruki Murakami es un escritor y traductor japonés, autor de novelas y ensayos, considerado uno de los más importantes autores contemporáneos.');
INSERT INTO authors (first_name, last_name, biography) VALUES  ('Stephen', 'King', 'Stephen King es un escritor estadounidense conocido por sus novelas de terror, ciencia ficción y suspenso, muchas de las cuales han sido adaptadas al cine.');
INSERT INTO authors (first_name, last_name, biography) VALUES  ('George', 'Orwell', 'George Orwell fue un escritor y periodista británico, conocido por su novela "1984" y su ensayo "Rebelión en la granja".');
INSERT INTO authors (first_name, last_name, biography) VALUES  ('Eva', 'García Sáenz de Urturi', 'Eva García Sáenz de Urturi es una escritora española conocida por sus novelas de intriga y misterio, como "El silencio de la ciudad blanca".');
INSERT INTO authors (first_name, last_name, biography) VALUES  ('Sally', 'Rooney', 'Sally Rooney es una escritora irlandesa conocida por sus novelas contemporáneas, entre las que destaca "Normal People".');
INSERT INTO authors (first_name, last_name, biography) VALUES  ('Paula', 'Hawkins', 'Paula Hawkins es una escritora británica conocida por su novela "La chica del tren", que se convirtió en un éxito de ventas a nivel mundial.');
INSERT INTO authors (first_name, last_name, biography) VALUES  ('Dolores', 'Redondo', 'Dolores Redondo es una escritora española conocida por su Trilogía del Baztán, que incluye "El guardián invisible".');
INSERT INTO authors (first_name, last_name, biography) VALUES  ('Ernest', 'Cline', 'Ernest Cline es un escritor y guionista estadounidense, conocido por su novela "Ready Player One" y su secuela "Ready Player Two".');
INSERT INTO authors (first_name, last_name, biography) VALUES  ('Tara', 'Westover', 'Tara Westover es una escritora estadounidense conocida por su libro "Educated", que relata su experiencia de crecer en una familia survivalista en Idaho.');
INSERT INTO authors (first_name, last_name, biography) VALUES  ('George R.R.', 'Martin', 'George R.R. Martin es un escritor estadounidense conocido por su serie de novelas "Canción de hielo y fuego", adaptada a la famosa serie de televisión "Game of Thrones".');

    -- Gabriel Garcseller_id, ía Márquez
INSERT INTO books (seller_id, stock, isbn, title, author_id, editorial, edition, synopsis ,cover, price, status) VALUES ('d0a76817-7bc5-46d6-8264-a9e824500aa8',3, '9780307474727', 'Cien años de soledad', 1, 'Editorial Sudamericana', 'Primera edición', 'Cien años de soledad es una novela escrita por el escritor estadounidense Gabriel Garcia Marquez y publicada en 1967.' ,'https://www.mejoreslibros.top/wp-content/uploads/2020/09/Cien-anos-de-Soledad-50-Aniversario-1141x1536.jpg' ,25.99, 'PUBLISHED');
INSERT INTO books (seller_id, stock, isbn, title, author_id, editorial, edition, synopsis ,cover, price, status) VALUES ('a9777be2-5eb0-44f9-9fc5-d425bdeea152',3, '9780061122415', 'El amor en los tiempos del cólera', 1, 'HarperCollins', 'Edición especial', 'El amor en los tiempos del colera es una novela escrita por el escritor estadounidense Gabriel Garcia Marquez y publicada en 1865','https://www.agapea.com/DEBOLSILLO/El-amor-en-los-tiempos-del-colera-edicion-escolar--i6n17519646.jpg', 20.50, 'PUBLISHED');
-- J.K. Rowlingstocseller_id, k, 3, '88fbdef8-cad9-447a-a2e6-9dff1d1bb002',
INSERT INTO books (seller_id, stock, isbn, title, author_id, editorial, edition, synopsis ,cover, price, status) VALUES ('88fbdef8-cad9-447a-a2e6-9dff1d1bb002',3, '9788478888566', 'Harry Potter y la piedra filosofal', 2, 'Salamandra', '20th Anniversary Edition', 'Harry Potter y la piedra filosofal es una novela escrita por el escritor británico J. K. Rowling y publicada por primera vez en 1997','https://images-na.ssl-images-amazon.com/images/S/pv-target-images/6ff78b522f917f7ef331f466bd31b8d6156dec31740d84e98cb1846b9b049e28._RI_V_TTW_.jpg', 15.50, 'PUBLISHED');
INSERT INTO books (seller_id, stock, isbn, title, author_id, editorial, edition, synopsis ,cover, price, status) VALUES ('88fbdef8-cad9-447a-a2e6-9dff1d1bb002',3, '9788478884452', 'Harry Potter y el prisionero de Azkaban', 2, 'Salamandra', 'Edición ilustrada', 'Harry Potter y el prisionero de Azkaban es una novela escrita por el escritor británico J. K. Rowling y publicada por primera vez en 1999','https://th.bing.com/th/id/R.9ffb26bbcb0c73cd7b4cafe31869251e?rik=Zk1voqrbT50mQw&pid=ImgRaw&r=0', 18.75, 'PUBLISHED');
-- Dan Brownstock, seller_id, 3, '88fbdef8-cad9-447a-a2e6-9dff1d1bb002',
INSERT INTO books (seller_id, stock, isbn, title, author_id, editorial, edition, synopsis ,cover, price, status) VALUES ('88fbdef8-cad9-447a-a2e6-9dff1d1bb002',3, '9788401341741', 'El código Da Vinci', 3, 'Planeta', 'Edición especial ilustrada', 'El código Da Vinci es una novela escrita por el escritor estadounidense Dan Brown y publicada por primera vez en 2003' ,'https://imagessl9.casadellibro.com/a/l/t0/59/9788408013259.jpg',18.75, 'PUBLISHED');
INSERT INTO books (seller_id, stock, isbn, title, author_id, editorial, edition, synopsis ,cover, price, status) VALUES ('88fbdef8-cad9-447a-a2e6-9dff1d1bb002',3, '9788401341765', 'Ángeles y demonios', 3, 'Planeta', 'Edición de bolsillo', 'El código Da Vinci es una novela escrita por el escritor estadounidense Dan Brown y publicada por primera vez en 2003' ,'https://imagessl7.casadellibro.com/a/l/t0/47/9788408106647.jpg', 12.99, 'PUBLISHED');
-- Haruki Murakamisseller_id, tock, 3, '88fbdef8-cad9-447a-a2e6-9dff1d1bb002',
INSERT INTO books (seller_id, stock, isbn, title, author_id, editorial, edition, synopsis ,cover, price, status) VALUES ('88fbdef8-cad9-447a-a2e6-9dff1d1bb002',3, '9788483835141', 'Tokio blues (Norwegian Wood)', 4, 'Tusquets', 'Edición limitada', 'Tokio blues es una novela escrita por el escritor noruego Haruki Murakami y publicada por primera vez en 1987','https://cdn-images-1.medium.com/max/1200/1*XDfStxeyPzihoxzOTrfG8g.jpeg', 22.95, 'PUBLISHED');
-- Stephen Kingseller_id, '88fbdef8-cad9-447a-a2e6-9dff1d1bb002',
INSERT INTO books (seller_id, stock, isbn, title, author_id, editorial, edition, synopsis ,cover, price, status) VALUES ('d0a76817-7bc5-46d6-8264-a9e824500aa8',3, '9781501142970', 'It', 5, 'Scribner', 'Edición aniversario', 'It es una novela escrita por el escritor estadounidense Stephen King y publicada por primera vez en 1986' ,'https://d28hgpri8am2if.cloudfront.net/book_images/onix/cvr9781501142970/it-9781501142970_hr.jpg', 30.00, 'PUBLISHED');
INSERT INTO books (seller_id, stock, isbn, title, author_id, editorial, edition, synopsis ,cover, price, status) VALUES ('88fbdef8-cad9-447a-a2e6-9dff1d1bb002',3, '9781501143793', 'El resplandor', 5, 'Scribner', 'Edición conmemorativa', 'El resplandor es una novela escrita por el escritor estadounidense Stephen King y publicada por primera vez en 1977' ,'https://th.bing.com/th/id/OIP.eKU4UcPAA-Gf7WA64MlPHQHaLQ?rs=1&pid=ImgDetMain', 28.99, 'PUBLISHED');
INSERT INTO books (seller_id, stock, isbn, title, author_id, editorial, edition, synopsis ,cover, price, status) VALUES ('88fbdef8-cad9-447a-a2e6-9dff1d1bb002',3, '9781501198090', 'Carrie', 5, 'Scribner', 'Edición especial', 'Carrie es una novela escrita por el escritor estadounidense Stephen King y publicada por primera vez en 1974' ,'https://vignette.wikia.nocookie.net/carriemovies/images/f/f5/Carrie-L-1.jpeg/revision/latest?cb=20140420073751',19.95, 'PUBLISHED');
-- Libros adicionalseller_id, es'88fbdef8-cad9-447a-a2e6-9dff1d1bb002',
INSERT INTO books (seller_id, stock, isbn, title, author_id, editorial, edition, synopsis ,cover, price, status) VALUES ('d0a76817-7bc5-46d6-8264-a9e824500aa8',3, '9780345803504', '1984', 6, 'Vintage', 'Edición de bolsillo', '1984 es una novela escrita por el escritor británico George Orwell y publicada por primera vez en 1949','https://imagessl4.casadellibro.com/a/l/t0/44/9788499890944.jpg', 10.99, 'PUBLISHED');
INSERT INTO books (seller_id, stock, isbn, title, author_id, editorial, edition, synopsis ,cover, price, status) VALUES ('88fbdef8-cad9-447a-a2e6-9dff1d1bb002',3, '9788408181909', 'El silencio de la ciudad blanca', 7, 'Booket', 'Edición de bolsillo', 'El silencio de la ciudad blanca es una novela escrita por el escritor estadounidense Anthony Bourdain y publicada por primera vez en 2011','https://th.bing.com/th/id/OIP.S_scG4LscRn7o9-iJmLm8wAAAA?rs=1&pid=ImgDetMain', 9.95, 'PUBLISHED');
INSERT INTO books (seller_id, stock, isbn, title, author_id, editorial, edition, synopsis ,cover, price, status) VALUES ('88fbdef8-cad9-447a-a2e6-9dff1d1bb002',3, '9781984822185', 'Normal People', 8, 'Hogarth', 'Edición de tapa blanda', 'Normal People es una novela escrita por el escritor estadounidense Anthony Bourdain y publicada por primera vez en 2011' ,'https://th.bing.com/th/id/OIP.A2Jr4iobnk837CAoP9IN3QHaLZ?w=1600&h=2463&rs=1&pid=ImgDetMain', 14.50, 'PUBLISHED');
INSERT INTO books (seller_id, stock, isbn, title, author_id, editorial, edition, synopsis ,cover, price, status) VALUES ('88fbdef8-cad9-447a-a2e6-9dff1d1bb002',3, '9788466332285', 'La chica del tren', 9, 'Booket', 'Edición de bolsillo', 'La chica del tren es una novela escrita por el escritor estadounidense Anthony Bourdain y publicada por primera vez en 2011' ,'https://www.ecartelera.com/carteles/10500/10507/005_m.jpg',12.75, 'PUBLISHED');
INSERT INTO books (seller_id, stock, isbn, title, author_id, editorial, edition, synopsis ,cover, price, status) VALUES ('88fbdef8-cad9-447a-a2e6-9dff1d1bb002',3, '9788466341706', 'El guardián invisible', 10, 'Destino', 'Edición de tapa blanda', 'El guardián invisible es una novela escrita por el escritor estadounidense Anthony Bourdain y publicada por primera vez en 2011' ,'https://th.bing.com/th/id/R.017999b36c48fdc1a6f469fc5359d7e8?rik=J8RAspAlzvoGcA&pid=ImgRaw&r=0', 16.95, 'PUBLISHED');
INSERT INTO books (seller_id, stock, isbn, title, author_id, editorial, edition, synopsis ,cover, price, status) VALUES ('88fbdef8-cad9-447a-a2e6-9dff1d1bb002',3, '9788433960066', 'Los pilares de la Tierra', 11, 'Booket', 'Edición de bolsillo', 'Los pilares de la Tierra es una novela escrita por el escritor estadounidense Anthony Bourdain y publicada por primera vez en 2011','https://th.bing.com/th/id/R.28a77bbc60a450db35617c63650f0ad0?rik=Vc0YSlsQEvw6eQ&pid=ImgRaw&r=0', 25.00, 'PENDING');
INSERT INTO books (seller_id, stock, isbn, title, author_id, editorial, edition, synopsis ,cover, price, status) VALUES ('88fbdef8-cad9-447a-a2e6-9dff1d1bb002',3, '9780062315007', 'Ready Player One', 12, 'Broadway Books', 'Edición de tapa blanda', 'Ready Player One es una novela escrita por el escritor estadounidense Ernest Cline y publicada por primera vez en 2011','https://th.bing.com/th/id/OIP.JXcAea00-BQDHLqz9FFM1gHaLa?rs=1&pid=ImgDetMain', 14.99, 'PENDING');
INSERT INTO books (seller_id, stock, isbn, title, author_id, editorial, edition, synopsis ,cover, price, status) VALUES ('88fbdef8-cad9-447a-a2e6-9dff1d1bb002',3, '9781524796280', 'Educated', 12, 'Random House', 'Edición de tapa blanda', 'Educated es una novela escrita por el escritor estadounidense Ernest Cline y publicada por primera vez en 2011','https://papercutshop.se/wp-content/uploads/2018/01/1-85.jpg', 17.99, 'PENDING');

-- Insertar datos en la tabla public.addresses
INSERT INTO addresses (address_num, postal_code, address_city, address_country, address_road) VALUES (123, 12345, 'City1', 'Country1', 'Road1');
INSERT INTO addresses (address_num, postal_code, address_city, address_country, address_road) VALUES (456, 54321,'City2', 'Country2', 'Road2');

INSERT INTO user_address (user_id, address_id) VALUES ('9a97b5ee-6066-4160-a564-ea0ea0816ec8', 1);
INSERT INTO user_address (user_id, address_id) VALUES ('7cd78083-6dec-4bff-ac90-ef373bb1515e', 2);
-- Insertar datos en la tabla public.sales
-- INSERT INTO sales (sale_date, address_id, book_id, user_id) VALUES ('2024-05-01', 1, '9788478888566', 'user1');
-- INSERT INTO sales (sale_date, address_id, book_id, user_id) VALUES ('2024-05-02', 2,  '9781984822185', 'user2');

INSERT INTO genres (name) VALUES ('Realismo mágico');
    INSERT INTO genres (name) VALUES ('Fantasía');
    INSERT INTO genres (name) VALUES ('Misterio');
    INSERT INTO genres (name) VALUES ('Ciencia ficción');
    INSERT INTO genres (name) VALUES ('Drama');
    INSERT INTO genres (name) VALUES ('Aventura');
    INSERT INTO genres (name) VALUES ('Romance');
    INSERT INTO genres (name) VALUES ('Suspenso');
    INSERT INTO genres (name) VALUES ('Thriller');
    INSERT INTO genres (name) VALUES ('Histórico');

-- Insertar datos de géneros de libros (relación muchos a muchos)

    -- Gabriel García Márquez
INSERT INTO books_genres (book_id, genre_id) VALUES ('9780307474727', 1); -- Cien años de soledad (Realismo mágico)
INSERT INTO books_genres (book_id, genre_id) VALUES('9780061122415', 1); -- El amor en los tiempos del cólera (Realismo mágico)
    --J.K. Rowlin;
INSERT INTO books_genres (book_id, genre_id) VALUES ('9788478888566', 2); -- Harry Potter y la piedra filosofal (Fantasía)
INSERT INTO books_genres (book_id, genre_id) VALUES ('9788478884452', 2); -- Harry Potter y el prisionero de Azkaban (Fantasía)
    --Dan, Brow;
INSERT INTO books_genres (book_id, genre_id) VALUES ('9788401341741', 3); -- El código Da Vinci (Misterio)
INSERT INTO books_genres (book_id, genre_id) VALUES ('9788401341765', 3); -- Ángeles y demonios (Misterio)
    --Haruki Murakam;
INSERT INTO books_genres (book_id, genre_id) VALUES ('9788483835141', 4); -- Tokio blues (Norwegian Wood) (Drama)
    --Stephen Kin;
INSERT INTO books_genres (book_id, genre_id) VALUES ('9781501142970', 9); -- It (Thriller)
INSERT INTO books_genres (book_id, genre_id) VALUES ('9781501143793', 9); -- El resplandor (Thriller)
INSERT INTO books_genres (book_id, genre_id) VALUES ('9781501198090', 9); -- Carrie (Thriller)
    --Libros adicionale;
INSERT INTO books_genres (book_id, genre_id) VALUES ('9780345803504', 5); -- 1984 (Drama)
INSERT INTO books_genres (book_id, genre_id) VALUES ('9788408181909', 6); -- El silencio de la ciudad blanca (Aventura)
INSERT INTO books_genres (book_id, genre_id) VALUES ('9781984822185', 7); -- Normal People (Romance)
INSERT INTO books_genres (book_id, genre_id) VALUES ('9788466332285', 8); -- La chica del tren (Suspenso)
INSERT INTO books_genres (book_id, genre_id) VALUES ('9788466341706', 9); -- El guardián invisible (Thriller)
INSERT INTO books_genres (book_id, genre_id) VALUES ('9788433960066', 10); -- Los pilares de la Tierra (Histórico)
INSERT INTO books_genres (book_id, genre_id) VALUES ('9780062315007', 4); -- Ready Player One (Drama)
INSERT INTO books_genres (book_id, genre_id) VALUES ('9781524796280', 3); -- The Testaments (Misterio)


-- Insertar datos en la tabla cart para usuario con el id 1 para modelo de prueba
-- INSERT INTO carts (id, user_id) VALUES (1, '68a3fe04-82a7-4a2d-8b70-9f9e7a8f9455');
-- INSERT INTO carts_books ( cart_id, book_id, quantity) VALUES ( 1, '9788408181909',1)
-- INSERT INTO carts_books ( cart_id, book_id, quantity) VALUES ( 1, '9781524796280',2)

INSERT INTO favorite_books (book_id, user_id) VALUES ('9780062315007', '9a97b5ee-6066-4160-a564-ea0ea0816ec8')
INSERT INTO favorite_books (book_id, user_id) VALUES ('9781524796280', '9a97b5ee-6066-4160-a564-ea0ea0816ec8')
INSERT INTO favorite_books (book_id, user_id) VALUES ('9788466341706', '9a97b5ee-6066-4160-a564-ea0ea0816ec8')
INSERT INTO favorite_books (book_id, user_id) VALUES ('9781501143793', '7cd78083-6dec-4bff-ac90-ef373bb1515e')
INSERT INTO favorite_books (book_id, user_id) VALUES ('9788478884452', '7cd78083-6dec-4bff-ac90-ef373bb1515e')

INSERT INTO wallet (saler_id) VALUES ('d0a76817-7bc5-46d6-8264-a9e824500aa8')
INSERT INTO wallet (saler_id) VALUES ('a9777be2-5eb0-44f9-9fc5-d425bdeea152')
INSERT INTO wallet (saler_id) VALUES ('88fbdef8-cad9-447a-a2e6-9dff1d1bb002')