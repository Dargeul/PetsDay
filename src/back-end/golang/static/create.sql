CREATE TABLE IF NOT EXISTS `user` (
  `user_id` int(32) NOT NULL AUTO_INCREMENT,
  `username` varchar(150),
  `password` varchar(150),
  `user_nickname` varchar(30),
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `pet` (
  `pet_id` int(32) NOT NULL AUTO_INCREMENT,
  `pet_nickname` varchar(30),
  `pet_owner` int(32),
  `pet_type` varchar(30),
  `pet_weight` int(32),
  `pet_sex` varchar(30),
  `pet_birth` varchar(30),
  `pet_photo` varchar(30),
  CONSTRAINT FOREIGN KEY (`pet_owner`) REFERENCES `user`(`user_id`)
  ON DELETE  RESTRICT  ON UPDATE CASCADE,
  PRIMARY KEY (`pet_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `hotspot` (
  `hs_time` varchar(30),
  `hs_user` int(32),
  `hs_content` varchar(30),
  `hs_photo` varchar(30),
  `hs_id` int(32) NOT NULL AUTO_INCREMENT,
  CONSTRAINT FOREIGN KEY (`hs_user`) REFERENCES `user`(`user_id`)
  ON DELETE  RESTRICT  ON UPDATE CASCADE,
  PRIMARY KEY (`hs_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `comment` (
  `com_time` varchar(30),
  `com_user` int(32),
  `com_hs` int(32),
  `com_content` varchar(30),
  `com_id` int(32) NOT NULL AUTO_INCREMENT,
  CONSTRAINT FOREIGN KEY (`com_user`) REFERENCES `user`(`user_id`)
  ON DELETE  RESTRICT  ON UPDATE CASCADE,
  CONSTRAINT FOREIGN KEY (`com_hs`) REFERENCES `hotspot`(`hs_id`)
  ON DELETE  RESTRICT  ON UPDATE CASCADE,
  PRIMARY KEY (`com_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `pet_and_hotspot` (
  `_id` int(32) NOT NULL AUTO_INCREMENT,
  `pet_id` int(32) NOT NULL ,
  `hs_id` int(32) NOT NULL ,
  PRIMARY KEY (`_id`),
  CONSTRAINT FOREIGN KEY (`pet_id`) REFERENCES `pet`(`pet_id`)
  ON DELETE  RESTRICT  ON UPDATE CASCADE,
  CONSTRAINT FOREIGN KEY (`hs_id`) REFERENCES `hotspot`(`hs_id`)
  ON DELETE  RESTRICT  ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `pet_and_user` (
  `_id` int(32) NOT NULL AUTO_INCREMENT,
  `pet_id` int(32) NOT NULL ,
  `user_id` int(32) NOT NULL ,
  PRIMARY KEY (`_id`),
  CONSTRAINT FOREIGN KEY (`pet_id`) REFERENCES `pet`(`pet_id`)
  ON DELETE  RESTRICT  ON UPDATE CASCADE,
  CONSTRAINT FOREIGN KEY (`user_id`) REFERENCES `user`(`user_id`)
  ON DELETE  RESTRICT  ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE IF NOT EXISTS `notification` (
  `notice_status` int(32),
  `notice_user` int(32),
  `notice_comment` int(32),
  `notice_id` int(32) NOT NULL AUTO_INCREMENT,
  CONSTRAINT FOREIGN KEY (`notice_user`) REFERENCES `user`(`user_id`)
  ON DELETE  RESTRICT  ON UPDATE CASCADE,
  CONSTRAINT FOREIGN KEY (`notice_comment`) REFERENCES `comment`(`com_id`)
  ON DELETE  RESTRICT  ON UPDATE CASCADE,
  PRIMARY KEY (`notice_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `like` (
  `like_user` int(32),
  `like_hotspot` int(32),
  `like_id` int(32) NOT NULL AUTO_INCREMENT,
  CONSTRAINT FOREIGN KEY (`like_user`) REFERENCES `user`(`user_id`)
  ON DELETE  RESTRICT  ON UPDATE CASCADE,
  CONSTRAINT FOREIGN KEY (`like_hotspot`) REFERENCES `hotspot`(`hs_id`)
  ON DELETE  RESTRICT  ON UPDATE CASCADE,
  PRIMARY KEY (`like_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `good` (
  `good_name` varchar(30),
  `good_price` varchar(150),
  `good_id` int(32) NOT NULL AUTO_INCREMENT,
  `good_count` int(32),
  `good_info` varchar(30),
  PRIMARY KEY (`good_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
