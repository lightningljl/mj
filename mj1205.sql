/*
SQLyog v10.2 
MySQL - 5.5.5-10.1.13-MariaDB : Database - mj
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`mj` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `mj`;

/*Table structure for table `action_log` */

DROP TABLE IF EXISTS `action_log`;

CREATE TABLE `action_log` (
  `log_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `action` int(11) NOT NULL DEFAULT '0' COMMENT '动作,1注册,2登录',
  `content` varchar(500) NOT NULL DEFAULT '' COMMENT '动作描述',
  `created_at` int(11) NOT NULL COMMENT '创建时间，事件发生时间',
  PRIMARY KEY (`log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户行为表';

/*Data for the table `action_log` */

/*Table structure for table `users` */

DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `account` varchar(100) NOT NULL COMMENT '账号,第三方登录随机生成',
  `password` varchar(32) NOT NULL COMMENT '密码',
  `phone` char(11) NOT NULL DEFAULT '' COMMENT '手机号码，非必填，可能第三方',
  `nick` varchar(50) NOT NULL DEFAULT '' COMMENT '昵称',
  `gender` tinyint(4) NOT NULL DEFAULT '1' COMMENT '性别,1男,2女',
  `avatar` varchar(300) NOT NULL DEFAULT '' COMMENT '头像',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `account` (`account`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='用户表';

/*Data for the table `users` */

insert  into `users`(`user_id`,`account`,`password`,`phone`,`nick`,`gender`,`avatar`) values (1,'18981076926','e10adc3949ba59abbe56e057f20f883e','18981076926','y3m3va',1,'');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
