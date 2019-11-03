CREATE DATABASE /*!32312 IF NOT EXISTS*/`api_info` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

use api_info;

-- 用户表
create table api_earthly_sweet (
	id int (100) NOT NULL primary key AUTO_INCREMENT COMMENT '主键' ,
	content varchar(255) not null comment '内容',
	create_date date comment '创建日期',
	source  varchar(100) comment '来源'
);