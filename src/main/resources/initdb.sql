drop table if exists city;
drop table if exists hotel;


-- 字典表
create table TB_DICTIONARY
(
id int primary key auto_increment,
dic_type varchar(50), -- 标明是用于什么内容的
dic_code varchar(200), -- 数据库中记录的值
dic_content varchar(300), -- 页面需要显示的值
dic_order_by int default '-1', -- 排序
active_flag char(1) default 'Y', -- 是否启用
is_delete int default '0', -- 0:未删除,1:已经删除，已经启用的记录不能删，要先失效才能删除
dic_comments varchar(300) -- 对此记录的注释
root_id int default '-1', -- 根记录此值等于id
parent_id int default '-1' -- 根记录此值等于 -1
);

-- 人员表
create table TB_USER
(
id int primary key auto_increment,
username varchar(20), -- 登录名
password varchar(20), -- 密码
dispname varchar(50), -- 页面显示名
is_delete int default '0', -- 0:未删除,1:已经删除
comments varchar(2000) -- 注释
);

