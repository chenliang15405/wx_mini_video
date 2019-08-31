CREATE DATABASE /*!32312 IF NOT EXISTS*/`wx_migutou_video` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

use wx_migutou_video;

-- 用户表
create table users(
	id varchar(100) NOT NULL primary key COMMENT '主键' ,
	username varchar(20) not null comment '用户名',
	password varchar(100) not null comment '密码',
  face_image varchar(255),
  nickname varchar(20),
	fans_count int(11),
	follow_count int(11),
	receive_like_count int(11)
);

-- 用户粉丝表
create table users_fans(
	id varchar(100) NOT NULL  primary key COMMENT '主键',
	user_id varchar(100),
	fan_id varchar(50)
);

-- 视频表
create table videos(
	id varchar(100) NOT NULL primary key COMMENT '主键' ,
	user_id varchar(100) COMMENT '用户id',
	audio_id varchar(50) COMMENT '音频id',
	video_desc varchar(150) comment '视频描述',
	video_path varchar(255) comment '视频路径',
	video_seconds float(6),
	video_width int(6),
	video_height int(6),
	cover_path varchar(255),
	like_counts bigint(20),
	status int(1) comment '1发布成功 2 禁止播放',
	create_time Date
);

-- 用户点赞视频表
create table users_like_videos(
	id varchar(100) not null primary key,
	user_id varchar(100),
	video_id varchar(100)
);

-- 用户举报表
create table user_report(
	id varchar(100) not null primary key,
	deal_user_id varchar(100),
	deal_video_id varchar(100),
	title varchar(128),
	content varchar(255),
	user_id varchar(100),
	create_date date
);

-- 背景音乐表
create table bgm(
	id varchar(64) not null primary key,
	author varchar(255),
	name varchar(255),
	path varchar(255)
);

-- 搜索关键词表
create table search_records (
	id varchar(64) not null primary key,
	content varchar(255)
);

-- 留言表
create table comments(
	id varchar(100) not null primary key,
	from_user_id varchar(100),
	video_id varchar(100),
	comment text,
	create_time date
);


