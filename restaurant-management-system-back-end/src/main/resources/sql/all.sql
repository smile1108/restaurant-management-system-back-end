DROP TABLE IF EXISTS `student`;
CREATE TABLE `student`(
    `id` VARCHAR(15) NOT NULL DEFAULT '',
    `email` VARCHAR(35) NOT NULL DEFAULT '',
    `name` VARCHAR(10) NOT NULL DEFAULT '',
    `password` VARCHAR(20) NOT NULL DEFAULT '',
    PRIMARY KEY (`id`)
)ENGINE=InnoDB, default charset = UTF8;

DROP TABLE IF EXISTS `merchant`;
CREATE TABLE `merchant`(
    `merchant_id` int NOT NULL auto_increment,
    `name` VARCHAR(10) NOT NULL DEFAULT '',
    `password` VARCHAR(20) NOT NULL DEFAULT '',
    `email` VARCHAR(35) NOT NULL DEFAULT '',
    PRIMARY KEY (`merchant_id`)
)ENGINE=InnoDB, default charset = UTF8;

DROP TABLE IF EXISTS `wicket`;
CREATE TABLE `wicket`(
    `wicket_id` INT NOT NULL AUTO_INCREMENT,
    `wicket_number` INT NOT NULL DEFAULT 0,
    `floor` INT NOT NULL DEFAULT 0,
    `merchant_id` int NOT NULL DEFAULT 0,
    PRIMARY KEY (`wicket_id`),
    FOREIGN KEY (`merchant_id`) REFERENCES merchant (`merchant_id`)
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

DROP TABLE IF EXISTS `order_info`;
CREATE TABLE `order_info`(
    `order_id` INT NOT NULL AUTO_INCREMENT,
    `food_id` INT NOT NULL,
    `take_time` TIME NOT NULL,
    `package` TINYINT NOT NULL DEFAULT 0,
    `is_complete` TINYINT NOT NULL DEFAULT 0,
    `order_time` TIME NOT NULL ,
    `number` INT NOT NULL DEFAULT 0,
    `total_price` DOUBLE NOT NULL DEFAULT 0,
    `grade` TINYINT NOT NULL DEFAULT 0,
    CHECK ( `number` > 0 AND `number` < 20 ),
    PRIMARY KEY (`order_id`),
    FOREIGN KEY (`food_id`) REFERENCES `food` (`food_id`)
)ENGINE=InnoDB, default charset = UTF8;

DROP TABLE IF EXISTS `administrator`;
CREATE TABLE `administrator`(
    `administrator_id` INT NOT NULL AUTO_INCREMENT,
    `username` VARCHAR(10) NOT NULL DEFAULT '',
    `password` VARCHAR(20) NOT NULL DEFAULT '',
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
