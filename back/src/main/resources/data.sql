DROP DATABASE IF EXISTS tripdb;

CREATE DATABASE tripdb;
use tripdb;

CREATE TABLE `member`
(
    `member_id`         varchar(12)                                          NOT NULL,
    `password`          varchar(200)                                         NOT NULL,
    `nickname`          varchar(10)                                          NOT NULL unique,
    `email`             varchar(50)                                          NOT NULL unique,
    `image_url`         varchar(200)                                         NULL,
    `role`              char(1),
    `oauth_server_type` ENUM ('GENERAL','GOOGLE', 'KAKAO') default 'GENERAL' NOT NULL,
    PRIMARY KEY (`member_id`)
);

CREATE TABLE `oauth_member`
(
    `oauth_id`          int                               NOT NULL AUTO_INCREMENT,
    `email`             varchar(50)                       NOT NULL,
    `oauth_server_type` ENUM ('GENERAL','GOOGLE','KAKAO') NOT NULL,
    PRIMARY KEY (`oauth_id`)
);

CREATE TABLE `sido`
(
    `sido_id` int          NOT NULL AUTO_INCREMENT,
    `name`    varchar(100) NOT NULL,
    `imageUrl` varchar(200)   NULL,
    PRIMARY KEY (`sido_id`)
);

CREATE TABLE `attraction_info`
(
    `attraction_info_id` int             NOT NULL AUTO_INCREMENT,
    `sido_id`            int             NOT NULL,
    `name`               varchar(100)    NULL,
    `addr`               varchar(100)    NULL,
    `url`                varchar(200)    NULL,
    `latitude`           decimal(20, 17) NOT NULL,
    `longitude`          decimal(20, 17) NOT NULL,
    PRIMARY KEY (`attraction_info_id`),
    FOREIGN KEY (`sido_id`) REFERENCES `sido` (`sido_id`)
);

CREATE TABLE `attraction_description`
(
    `attraction_description_id` int not null,
    `overview` varchar(10000) not null,
    FOREIGN KEY (`attraction_description_id`) REFERENCES `attraction_info` (`attraction_info_id`)
);

CREATE TABLE `review`
(
    `review_id`          int           NOT NULL AUTO_INCREMENT,
    `attraction_info_id` int           NOT NULL,
    `member_id`          varchar(12)   NOT NULL,
    `title`              varchar(150)  NOT NULL,
    `content`            varchar(1500) NOT NULL,
    `hits`               int           not null default 0,
    `image_url`          varchar(200)  NULL,
    `create_date`        timestamp              DEFAULT CURRENT_TIMESTAMP,
    `update_date`        timestamp              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`review_id`),
    FOREIGN KEY (`attraction_info_id`) REFERENCES `attraction_info` (`attraction_info_id`),
    FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`) ON DELETE CASCADE
);

CREATE TABLE `comment`
(
    `comment_id`  int          NOT NULL AUTO_INCREMENT,
    `member_id`   varchar(12)  NOT NULL,
    `review_id`   int          NOT NULL,
    `content`     varchar(300) NOT NULL,
    `create_date` timestamp DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`comment_id`),
    FOREIGN KEY (`review_id`) REFERENCES `review` (`review_id`) ON DELETE CASCADE,
    FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`) ON DELETE CASCADE
);

CREATE TABLE `member_board_log`
(
    `member_board_log_id` int         NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `member_id`           varchar(12) NOT NULL,
    `attraction_info_id`  int         NOT NULL,
    `create_date`         timestamp DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`attraction_info_id`) REFERENCES `attraction_info` (`attraction_info_id`)
);

CREATE TABLE `member_board_map`
(
    `member_board_map_id` int         NOT NULL AUTO_INCREMENT,
    `member_id`           varchar(12) NOT NULL,
    `sido_id`             int         NOT NULL,
    `now_location`        int     DEFAULT 0,
    `island_cnt`          int     DEFAULT 0,
    PRIMARY KEY (`member_board_map_id`),
    FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`),
    FOREIGN KEY (`sido_id`) REFERENCES `sido` (`sido_id`)
);

CREATE TABLE `board`
(
    `board_id`            int NOT NULL AUTO_INCREMENT,
    `member_board_map_id` int NOT NULL,
    `attraction_info_id`  int NOT NULL,
    `location`            int NOT NULL,
    PRIMARY KEY (`board_id`),
    FOREIGN KEY (`member_board_map_id`) REFERENCES `member_board_map` (`member_board_map_id`),
    FOREIGN KEY (`attraction_info_id`) REFERENCES `attraction_info` (`attraction_info_id`)
);

CREATE TABLE `member_board_like`
(
    `member_board_like_id` int         NOT NULL AUTO_INCREMENT,
    `review_id`            int         NOT NULL,
    `member_id`            varchar(12) NOT NULL,
    PRIMARY KEY (`member_board_like_id`),
    FOREIGN KEY (`review_id`) REFERENCES `review` (`review_id`) ON DELETE CASCADE,
    FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`) ON DELETE CASCADE
);

CREATE TABLE `score`
(
    `score_id`  int         NOT NULL AUTO_INCREMENT,
    `sido_id`   int         NOT NULL,
    `member_id` varchar(12) NOT NULL,
    `score`     int         NULL,
    PRIMARY KEY (`score_id`),
    FOREIGN KEY (`sido_id`) REFERENCES `sido` (`sido_id`),
    FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`) ON DELETE CASCADE
);

CREATE TABLE `restaurant`
(
    `restaurant_id` int             NOT NULL AUTO_INCREMENT,
    `name`          varchar(100)    NULL,
    `content`       varchar(1000)   NULL,
    `tel`           varchar(30)     NULL,
    `latitude`      decimal(20, 17) NULL,
    `longitude`     decimal(20, 17) NULL,
    PRIMARY KEY (`restaurant_id`)
);

CREATE TABLE `mail`
(
    `email`           varchar(50) NOT NULL,
    `key`             varchar(10) NOT NULL,
    `expiration_date` timestamp DEFAULT (CURRENT_TIMESTAMP + INTERVAL 5 minute ),
    PRIMARY KEY (`email`)
);

# LOAD DATA INFILE 'C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Uploads\\sido_data.csv'
#     INTO TABLE `tripdb`.`sido`
#     FIELDS TERMINATED BY ','
#     ENCLOSED BY '"'
#     LINES TERMINATED BY '\n'
#     (`sido_id`, `name`);

INSERT INTO tripdb.sido (sido_id, name, imageUrl) VALUES (1, '서울', '|4ae73ecb-3327-412c-aed9-49d00fb95aea.jpg');
INSERT INTO tripdb.sido (sido_id, name, imageUrl) VALUES (2, '인천', '|f8480efb-0204-40b0-ab22-8fa14d8d0bfd.jpg');
INSERT INTO tripdb.sido (sido_id, name, imageUrl) VALUES (3, '대전', '|dfd2f712-662c-49ea-886b-e2411d3af36b.jpg');
INSERT INTO tripdb.sido (sido_id, name, imageUrl) VALUES (4, '대구', '|086baaf8-57cd-4707-9303-e058f45e3a4b.jpg');
INSERT INTO tripdb.sido (sido_id, name, imageUrl) VALUES (5, '광주', '|117cef51-d58b-42ba-af2a-3a23e496c5f3.jpg');
INSERT INTO tripdb.sido (sido_id, name, imageUrl) VALUES (6, '부산', '|4fd41b9c-7f14-4677-b56f-0a945e2ddbab.jpg');
INSERT INTO tripdb.sido (sido_id, name, imageUrl) VALUES (7, '울산', '|e595e6d4-be0b-4944-8a45-bdbad45d5659.jpg');
INSERT INTO tripdb.sido (sido_id, name, imageUrl) VALUES (8, '세종특별자치시', '|c97ab343-f1b0-48da-8cfd-d460cda7835d.jpg');
INSERT INTO tripdb.sido (sido_id, name, imageUrl) VALUES (31, '경기도', '|ffa4f777-6b42-4e1b-a131-d95634cbfbca.jpg');
INSERT INTO tripdb.sido (sido_id, name, imageUrl) VALUES (32, '강원도', '|00bf1897-53fd-4df3-b122-f11755973db4.jpg');
INSERT INTO tripdb.sido (sido_id, name, imageUrl) VALUES (33, '충청북도', '|1abe4eca-5f90-49f7-a9d4-3dba9d1186a7.jpg');
INSERT INTO tripdb.sido (sido_id, name, imageUrl) VALUES (34, '충청남도', '|f9b7f039-638f-4ed4-9a18-22acca100f4e.jpg');
INSERT INTO tripdb.sido (sido_id, name, imageUrl) VALUES (35, '경상북도', '|edb7097e-c660-4cb6-832f-b11a80815a97.jpg');
INSERT INTO tripdb.sido (sido_id, name, imageUrl) VALUES (36, '경상남도', '|f486b862-377b-443a-b87c-e84ee1f6820f.jpg');
INSERT INTO tripdb.sido (sido_id, name, imageUrl) VALUES (37, '전라북도', '|6db5a2a2-c8d4-4f71-8c0c-8b428262f1b0.jpg');
INSERT INTO tripdb.sido (sido_id, name, imageUrl) VALUES (38, '전라남도', '|26031d61-67df-40e0-823d-937971004066.jpg');
INSERT INTO tripdb.sido (sido_id, name, imageUrl) VALUES (39, '제주도', '|8aed7341-bd36-4152-a015-2fad7e403539.jpg');

LOAD DATA INFILE 'C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Uploads\\attraction_info_data.csv'
    INTO TABLE `tripdb`.`attraction_info`
    FIELDS TERMINATED BY ','
    ENCLOSED BY '"'
    LINES TERMINATED BY '\n'
    (`attraction_info_id`, `sido_id`, `name`, `addr`, `url`, `latitude`, `longitude`);

LOAD DATA INFILE 'C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Uploads\\attraction_description_data.csv'
    INTO TABLE `tripdb`.`attraction_description`
    FIELDS TERMINATED BY ','
    ENCLOSED BY '"'
    LINES TERMINATED BY '\n'
    (`attraction_description_id`, `overview`);

INSERT INTO `member` (`member_id`, `password`, `nickname`, `email`, `image_url`, `role`, `oauth_server_type`)
VALUES
    ('user01', 'password1', 'nick1', 'nick1@example.com', null, 'U', 'GENERAL'),
    ('user02', 'password2', 'nick2', 'nick2@example.com', null, 'U', 'GENERAL'),
    ('user03', 'password3', 'nick3', 'nick3@example.com', null, 'U', 'GENERAL'),
    ('user04', 'password4', 'nick4', 'nick4@example.com', null, 'A', 'GENERAL'),
    ('user05', 'password5', 'nick5', 'nick5@example.com', null, 'U', 'GENERAL'),
    ('user06', 'password6', 'nick6', 'nick6@example.com', null, 'U', 'GENERAL'),
    ('user07', 'password7', 'nick7', 'nick7@example.com', null, 'U', 'GENERAL'),
    ('user08', 'password8', 'nick8', 'nick8@example.com', null, 'U', 'GENERAL'),
    ('user09', 'password9', 'nick9', 'nick9@example.com', null, 'U', 'GENERAL'),
    ('user10', 'password10', 'nick10', 'nick10@example.com', null, 'U', 'GENERAL');

INSERT INTO `review` (`attraction_info_id`, `member_id`, `title`, `content`, `hits`, `image_url`, `create_date`, `update_date`)
VALUES
    (125405, 'user01', 'Amazing Place', 'This is a wonderful place to visit.', 100, NULL, '2023-01-01 10:00:00', '2023-01-01 10:00:00'),
    (125406, 'user02', 'Beautiful Scenery', 'The scenery here is absolutely stunning.', 150, NULL, '2023-01-02 11:00:00', '2023-01-02 11:00:00'),
    (125407, 'user03', 'Great Experience', 'I had a fantastic experience here.', 200, NULL, '2023-01-03 12:00:00', '2023-01-03 12:00:00'),
    (125408, 'user04', 'Worth Visiting', 'Definitely worth visiting this place.', 50, NULL, '2023-01-04 13:00:00', '2023-01-04 13:00:00'),
    (125409, 'user05', 'Historical Place', 'A lot of history in this place.', 75, NULL, '2023-01-05 14:00:00', '2023-01-05 14:00:00'),
    (125410, 'user06', 'Perfect Getaway', 'Perfect place for a weekend getaway.', 120, NULL, '2023-01-06 15:00:00', '2023-01-06 15:00:00'),
    (125411, 'user07', 'Lovely Atmosphere', 'The atmosphere here is so lovely.', 110, NULL, '2023-01-07 16:00:00', '2023-01-07 16:00:00'),
    (125412, 'user08', 'Must See', 'This is a must-see attraction.', 80, NULL, '2023-01-08 17:00:00', '2023-01-08 17:00:00'),
    (125413, 'user09', 'Breathtaking Views', 'The views here are breathtaking.', 130, NULL, '2023-01-09 18:00:00', '2023-01-09 18:00:00'),
    (125414, 'user10', 'Amazing Architecture', 'The architecture here is amazing.', 140, NULL, '2023-01-10 19:00:00', '2023-01-10 19:00:00'),
    (125415, 'user01', 'Peaceful Place', 'Such a peaceful and relaxing place.', 95, NULL, '2023-01-11 20:00:00', '2023-01-11 20:00:00'),
    (125416, 'user02', 'Family Friendly', 'Great place to visit with family.', 105, NULL, '2023-01-12 21:00:00', '2023-01-12 21:00:00'),
    (125417, 'user03', 'Adventurous', 'Perfect place for an adventure.', 90, NULL, '2023-01-13 22:00:00', '2023-01-13 22:00:00'),
    (125418, 'user04', 'Cultural Richness', 'Rich in culture and history.', 85, NULL, '2023-01-14 23:00:00', '2023-01-14 23:00:00'),
    (125419, 'user05', 'Hidden Gem', 'A hidden gem worth exploring.', 70, NULL, '2023-01-15 09:00:00', '2023-01-15 09:00:00'),
    (125420, 'user06', 'Nature\'s Beauty', 'Nature at its finest.', 65, NULL, '2023-01-16 08:00:00', '2023-01-16 08:00:00'),
    (125421, 'user07', 'Modern Marvel', 'A marvel of modern architecture.', 54, NULL, '2023-01-17 07:00:00', '2023-01-17 07:00:00'),
    (125425, 'user08', 'Serene Environment', 'The environment is so serene.', 60, NULL, '2023-01-18 06:00:00', '2023-01-18 06:00:00'),
    (125423, 'user09', 'Fantastic Tour', 'The tour was fantastic.', 135, NULL, '2023-01-19 05:00:00', '2023-01-19 05:00:00'),
    (125424, 'user10', 'Educational Visit', 'Very educational and informative.', 145, NULL, '2023-01-20 04:00:00', '2023-01-20 04:00:00');

INSERT INTO `comment` (`member_id`, `review_id`, `content`)
VALUES
    ('user02', 1, 'I totally agree! This place is amazing.'),
    ('user03', 1, 'Absolutely stunning view!'),
    ('user04', 2, 'Beautiful scenery indeed!'),
    ('user05', 2, 'I visited last year, it was great.'),
    ('user06', 3, 'Great experience, highly recommend!'),
    ('user07', 3, 'I had a similar experience.'),
    ('user08', 4, 'Worth visiting for sure.'),
    ('user09', 4, 'I want to visit this place.'),
    ('user10', 5, 'A lot of history indeed.'),
    ('user01', 5, 'This place has so much history.'),
    ('user02', 6, 'Perfect getaway spot!'),
    ('user03', 6, 'Had a great time here.'),
    ('user04', 7, 'Lovely atmosphere!'),
    ('user05', 7, 'Such a lovely place.'),
    ('user06', 8, 'A must-see for sure.'),
    ('user07', 8, 'Absolutely must-see!'),
    ('user08', 9, 'Breathtaking views indeed.'),
    ('user09', 9, 'The views are truly breathtaking.'),
    ('user10', 10, 'Amazing architecture!'),
    ('user01', 10, 'The architecture is stunning.'),
    ('user02', 11, 'Such a peaceful place.'),
    ('user03', 11, 'Very relaxing and peaceful.'),
    ('user04', 12, 'Great place for family.'),
    ('user05', 12, 'Perfect for a family trip.'),
    ('user06', 13, 'Had an adventurous time!'),
    ('user07', 13, 'Adventure awaits!'),
    ('user08', 14, 'Rich in culture indeed.'),
    ('user09', 14, 'A cultural treasure.'),
    ('user10', 15, 'A hidden gem for sure.'),
    ('user01', 15, 'Definitely a hidden gem.'),
    ('user02', 16, 'Nature at its best.'),
    ('user03', 16, 'Beautiful nature spot.'),
    ('user04', 17, 'Modern marvel indeed.'),
    ('user05', 17, 'A modern architectural wonder.'),
    ('user06', 18, 'Serene environment.'),
    ('user07', 18, 'Very serene and calm.'),
    ('user08', 19, 'Fantastic tour!'),
    ('user09', 19, 'The tour was indeed fantastic.'),
    ('user10', 20, 'Very educational visit.'),
    ('user01', 20, 'Learned a lot from this visit.');

INSERT INTO `score` (`sido_id`, `member_id`, `score`)
VALUES
    (1, 'user01', 85), (1, 'user02', 90), (1, 'user03', 88), (1, 'user04', 92), (1, 'user05', 87), (1, 'user06', 89),
    (2, 'user07', 78), (2, 'user08', 84), (2, 'user09', 82), (2, 'user10', 80), (2, 'user01', 85), (2, 'user02', 88),
    (3, 'user03', 91), (3, 'user04', 90), (3, 'user05', 86), (3, 'user06', 88), (3, 'user07', 83), (3, 'user08', 87),
    (4, 'user09', 77), (4, 'user10', 79), (4, 'user01', 88), (4, 'user02', 86), (4, 'user03', 90), (4, 'user04', 89),
    (5, 'user05', 84), (5, 'user06', 83), (5, 'user07', 85), (5, 'user08', 87), (5, 'user09', 86), (5, 'user10', 88),
    (6, 'user01', 89), (6, 'user02', 90), (6, 'user03', 88), (6, 'user04', 87), (6, 'user05', 86), (6, 'user06', 85),
    (7, 'user07', 83), (7, 'user08', 82), (7, 'user09', 84), (7, 'user10', 81), (7, 'user01', 85), (7, 'user02', 87),
    (8, 'user03', 90), (8, 'user04', 89), (8, 'user05', 88), (8, 'user06', 87), (8, 'user07', 85), (8, 'user08', 86),
    (31, 'user09', 84), (31, 'user10', 83), (31, 'user01', 85), (31, 'user02', 86), (31, 'user03', 87), (31, 'user04', 89),
    (32, 'user05', 88), (32, 'user06', 87), (32, 'user07', 86), (32, 'user08', 85), (32, 'user09', 84), (32, 'user10', 83),
    (33, 'user01', 82), (33, 'user02', 81), (33, 'user03', 85), (33, 'user04', 87), (33, 'user05', 88), (33, 'user06', 86),
    (34, 'user07', 89), (34, 'user08', 90), (34, 'user09', 88), (34, 'user10', 87), (34, 'user01', 86), (34, 'user02', 85),
    (35, 'user03', 84), (35, 'user04', 83), (35, 'user05', 85), (35, 'user06', 86), (35, 'user07', 87), (35, 'user08', 88),
    (36, 'user09', 89), (36, 'user10', 90), (36, 'user01', 88), (36, 'user02', 87), (36, 'user03', 86), (36, 'user04', 85),
    (37, 'user05', 84), (37, 'user06', 83), (37, 'user07', 85), (37, 'user08', 87), (37, 'user09', 88), (37, 'user10', 89),
    (38, 'user01', 90), (38, 'user02', 89), (38, 'user03', 88), (38, 'user04', 87), (38, 'user05', 86), (38, 'user06', 85),
    (39, 'user07', 84), (39, 'user08', 83), (39, 'user09', 85), (39, 'user10', 87), (39, 'user01', 86), (39, 'user02', 88);

INSERT INTO `member_board_like` (`review_id`, `member_id`)
VALUES
    (1, 'user01'), (1, 'user02'), (1, 'user03'), (1, 'user04'),
    (2, 'user01'), (2, 'user02'),
    (3, 'user06'), (3, 'user07'), (3, 'user08'),
    (4, 'user06'), (4, 'user07'), (4, 'user08'),
    (5, 'user01'), (5, 'user02'), (5, 'user03'), (5, 'user04'),
    (6, 'user01'),
    (7, 'user06'), (7, 'user07'),
    (8, 'user06'), (8, 'user07'),
    (9, 'user01'), (9, 'user02'), (9, 'user03'), (9, 'user04'),
    (10, 'user01'), (10, 'user02'), (10, 'user03'),
    (11, 'user06'),
    (12, 'user06'), (12, 'user07'), (12, 'user08'),
    (13, 'user01'), (13, 'user02'), (13, 'user03'), (13, 'user04'), (13, 'user05'),
    (16, 'user06'), (16, 'user07'), (16, 'user08'), (16, 'user09'),
    (17, 'user01'), (17, 'user02'), (17, 'user03'),
    (18, 'user01'), (18, 'user02'),
    (19, 'user06'), (19, 'user07'),
    (20, 'user06'), (20, 'user07'), (20, 'user08'), (20, 'user09');