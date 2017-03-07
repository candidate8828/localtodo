drop table if exists TB_DICTIONARY;
drop table if exists TB_USER;
drop table if exists TB_WORK_LOG;
drop table if exists TB_WORK_CONTENT;
drop table if exists TB_LABEL;
drop table if exists TB_LOG_LABEL;
drop table if exists TB_FOLDER;
drop table if exists TB_LOG_FOLDER;
drop table if exists TB_FILE;


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
dic_comments varchar(300), -- 对此记录的注释
root_id int default '-1', -- 根记录此值等于id
parent_id int default '-1' -- 根记录此值等于 -1
);

-- 人员表
create table TB_USER
(
id int primary key auto_increment,
create_dt timestamp,
created_by int,
last_upd_dt timestamp,
last_upded_by int,
username varchar(20), -- 登录名
password varchar(20), -- 密码
dispname varchar(50), -- 页面显示名
is_delete int default '0', -- 0:未删除,1:已经删除
comments varchar(2000) -- 注释
);

insert into tb_user (create_dt,created_by,last_upd_dt,last_upded_by,username,password,dispname,is_delete,comments)
values (now(),1,now(),1,'gfyangsong','a123456','杨松','0','主要使用人员');

-- 工作记录表
create table TB_WORK_LOG
(
id int primary key auto_increment,
create_dt timestamp,
created_by int,
last_upd_dt timestamp,
last_upded_by int,
start_dt timestamp, -- 开始时间,每个记录都有
end_dt timestamp, -- 结束时间,需要跟进的记录才有
deadline timestamp, -- 最后截止时间,可有可无,一般是IT需求才有
log_type int, -- 0:不需要跟进,1:需要跟进
log_stat int, -- 状态[log_type为1时才关注此字段;log_type为0时,此字段为0] 1:待办,2:进行中,3:完成,4:延期
is_delete int default '0', -- 0:未删除,1:已经删除
log_title varchar(500), -- 任务名
log_desc varchar(500) -- 将对应conntent中前500个去除掉html标签的字符放入这里
);
insert into test.tb_work_log (create_dt,created_by,last_upd_dt,last_upded_by,start_dt,end_dt,deadline, log_type,log_stat,is_delete,log_title,log_desc) 
values (now(),'1',now(),'1',now(),null,null,'0','0','0','一个测试用的title','由“保护伞”公司最初研制的足以将人转变成僵尸的T型病毒仍然在地球上疯狂地蔓延着、肆虐着，威胁着要将所有的人类都转变成行动迟缓、嗜血如命的活死人。作为整个世界最后也是惟一的希望，沉睡的艾丽丝（米拉·乔沃维奇饰）这一次是在“保护伞”最机密的运作中心醒来的，随着她更深入地挖掘进这里面错综复杂的谜团的核心部分，她那充满着神秘色彩的过去也被一点点地揭露了出来。对于她来说，这个世界没有任何地方是绝对安全且隐蔽的，她只能主动出击，一个接着一个地抓捕那些需要对这场病毒的爆发负责的人，从他们的口中探知到她想要的真相。 一些若隐若现的暗示和线索让艾丽丝一路从东京追击到纽约，然后又从华盛顿辗转到莫斯科，最终却给她带来的是一系列相当令人震惊的可怕的启示，也逼迫着她不得不重新思考她曾经以为就是确凿的事实的一切。不但找到了新的同进同退的战友，还有以前熟悉的老朋友的回归，在他们的帮助下，艾丽丝必须得想办法在这样一个异常残酷的环境里活下去，因为只有这样她才能争取到足够多的时间，在濒于毁灭的地球上一边逃亡一边寻找真相，毕竟她已经没有任何回旋的余地了。');

create table TB_WORK_CONTENT  -- 类似贴吧
(
id int primary key auto_increment,
create_dt timestamp,
created_by int,
last_upd_dt timestamp,
last_upded_by int,
log_id int, -- 对应 TB_WORK_LOG.id
order_by int, -- 顺序
is_delete int default '0', -- 0:未删除,1:已经删除
log_content varchar(2000) -- 记录内容
);


-- 按照label分类  start --
create table TB_LABEL
(
id int primary key auto_increment,
label_name  varchar(500), -- 标签名
create_dt timestamp,
created_by int,
last_upd_dt timestamp,
last_upded_by int,
is_delete int default '0', -- 0:未删除,1:已经删除,在 TB_LOG_LABEL中有对应记录的不能删
order_by int -- 顺序
);

create table TB_LOG_LABEL
(
id int primary key auto_increment,
create_dt timestamp,
created_by int,
last_upd_dt timestamp,
last_upded_by int,
log_id int, -- 对应 TB_WORK_LOG.id
label_id int -- 对应 TB_LABEL.id
);
-- 按照label分类   end --

-- 按照folder分类  start --
create table TB_FOLDER
(
id int primary key auto_increment,
folder_name  varchar(500), -- 标签名
create_dt timestamp,
created_by int,
last_upd_dt timestamp,
last_upded_by int,
is_delete int default '0', -- 0:未删除,1:已经删除,在 TB_LOG_FOLDER 中有对应记录的不能删
parent_id int default '0', -- 默认为顶级目录
order_by int -- 顺序
);

insert into TB_FOLDER (folder_name, create_dt, created_by, last_upd_dt, last_upded_by, is_delete, parent_id, order_by) 
values ('工作', now(), '1', now(), '1', '0', '0', '1');
insert into TB_FOLDER (folder_name, create_dt, created_by, last_upd_dt, last_upded_by, is_delete, parent_id, order_by) 
values ('技术', now(), '1', now(), '1', '0', '0', '2');
insert into TB_FOLDER (folder_name, create_dt, created_by, last_upd_dt, last_upded_by, is_delete, parent_id, order_by) 
values ('未分类', now(), '1', now(), '1', '0', '0', '3');

create table TB_LOG_FOLDER
(
id int primary key auto_increment,
create_dt timestamp,
created_by int,
last_upd_dt timestamp,
last_upded_by int,
log_id int, -- 对应 TB_WORK_LOG.id
folder_id int -- 对应 TB_FOLDER.id
);
-- 按照folder分类   end --

create table TB_FILE
(
id int primary key auto_increment,
create_dt timestamp,
created_by int,
last_upd_dt timestamp,
last_upded_by int,
create_time bigint,
relate_path varchar(1000),
file_type varchar(100)
);
