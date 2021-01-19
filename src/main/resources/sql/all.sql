DROP TABLE IF EXISTS `student`;
CREATE TABLE `student`(
    `id` VARCHAR(15) NOT NULL DEFAULT '' UNIQUE,
    `email` VARCHAR(35) NOT NULL DEFAULT '',
    `name` VARCHAR(15) NOT NULL DEFAULT '',
    `password` VARCHAR(50) NOT NULL DEFAULT '',
    PRIMARY KEY (`email`)
)ENGINE=InnoDB, default charset = UTF8;

DROP TABLE IF EXISTS `merchant`;
CREATE TABLE `merchant`(
    `merchant_id` int NOT NULL auto_increment,
    `name` VARCHAR(10) NOT NULL DEFAULT '',
    `password` VARCHAR(50) NOT NULL DEFAULT '',
    `email` VARCHAR(35) NOT NULL DEFAULT '' UNIQUE,
    PRIMARY KEY (`merchant_id`)
)ENGINE=InnoDB, default charset = UTF8;

DROP TABLE IF EXISTS `wicket`;
CREATE TABLE `wicket`(
    `wicket_id` INT NOT NULL AUTO_INCREMENT,
    `wicket_number` INT NOT NULL DEFAULT 0,
    `floor` INT NOT NULL DEFAULT 0,
    `merchant_id` int NOT NULL DEFAULT 0,
    PRIMARY KEY (`wicket_id`),
    FOREIGN KEY (`merchant_id`) REFERENCES merchant (`merchant_id`),
    UNIQUE (`wicket_number`, `floor`)
)ENGINE=InnoDB, default charset = UTF8;

DROP TABLE IF EXISTS `food`;
CREATE TABLE `food`(
    `food_id` INT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(10) NOT NULL DEFAULT '',
    `price` DOUBLE NOT NULL DEFAULT 0,
    `taste` VARCHAR(5) NOT NULL DEFAULT '',
    `wicket_id` INT NOT NULL DEFAULT 0,
    PRIMARY KEY (`food_id`),
    FOREIGN KEY (`wicket_id`) REFERENCES wicket (`wicket_id`)
)ENGINE=InnoDB, default charset = UTF8;

alter table `food` add column (`image` varchar(50) default '');

insert into `food` (name, price, taste, wicket_id) values ('test1', 10, 'test1', 1);
insert into `food` (name, price, taste, wicket_id) values ('test2', 10, 'test2', 1);
insert into `food` (name, price, taste, wicket_id) values ('test3', 10, 'test3', 1);
insert into `food` (name, price, taste, wicket_id) values ('test4', 10, 'test4', 1);
insert into `food` (name, price, taste, wicket_id) values ('test5', 10, 'test5', 1);
insert into `food` (name, price, taste, wicket_id) values ('test6', 10, 'test6', 1);
insert into `food` (name, price, taste, wicket_id) values ('test7', 10, 'test7', 1);
insert into `food` (name, price, taste, wicket_id) values ('辣1', 10, '辣', 1);
insert into `food` (name, price, taste, wicket_id) values ('辣2', 10, '辣', 1);
insert into `food` (name, price, taste, wicket_id) values ('辣3', 10, '辣', 1);
insert into `food` (name, price, taste, wicket_id) values ('辣4', 10, '辣', 1);
insert into `food` (name, price, taste, wicket_id) values ('辣5', 10, '辣', 1);
insert into `food` (name, price, taste, wicket_id) values ('辣6', 10, '辣', 1);
insert into `food` (name, price, taste, wicket_id) values ('辣7', 10, '辣', 1);
insert into `food` (name, price, taste, wicket_id) values ('辣8', 10, '辣', 1);
insert into `food` (name, price, taste, wicket_id) values ('辣9', 10, '辣', 1);
insert into `food` (name, price, taste, wicket_id) values ('辣10', 10, '辣', 1);

DROP TABLE IF EXISTS `order_info`;
CREATE TABLE `order_info`(
    `order_id` INT NOT NULL AUTO_INCREMENT,
    `food_id` INT NOT NULL,
    `user_email` VARCHAR(35) NOT NULL DEFAULT '',
    `take_time` DATETIME NOT NULL,
    `is_package` TINYINT NOT NULL DEFAULT 0,
    `is_complete` TINYINT NOT NULL DEFAULT 0,
    `order_time` DATETIME NOT NULL ,
    `number` INT NOT NULL DEFAULT 0,
    `total_price` DOUBLE NOT NULL DEFAULT 0,
    `grade` TINYINT NOT NULL DEFAULT -1,
    CHECK ( `number` > 0 AND `number` < 20 ),
    PRIMARY KEY (`order_id`),
    FOREIGN KEY (`user_email`) REFERENCES student (`email`),
    FOREIGN KEY (`food_id`) REFERENCES food (`food_id`)
)ENGINE=InnoDB, default charset = UTF8;

DROP TABLE IF EXISTS `administrator`;
CREATE TABLE `administrator`(
    `administrator_id` INT NOT NULL AUTO_INCREMENT,
    `username` VARCHAR(10) NOT NULL DEFAULT '' UNIQUE,
    `password` VARCHAR(50) NOT NULL DEFAULT '',
    PRIMARY KEY (`administrator_id`)
)ENGINE=InnoDB, default charset = UTF8;

DROP TABLE IF EXISTS `wicket_open`;
CREATE TABLE `wicket_open`(
    `administrator_id` INT NOT NULL,
    `wicket_id` INT NOT NULL,
    PRIMARY KEY (`administrator_id`, `wicket_id`),
    FOREIGN KEY (`administrator_id`) REFERENCES `administrator`(`administrator_id`),
    FOREIGN KEY (`wicket_id`) REFERENCES `wicket`(`wicket_id`)
)ENGINE=InnoDB, default charset = UTF8;
