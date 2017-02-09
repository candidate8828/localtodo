
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
log_title varchar(500) -- 任务名
);

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
label_id -- 对应 TB_LABEL.id
);
-- 按照label分类   end --

-- 按照category层次分类  start --
-- 待完善
-- 按照category层次分类  end --


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
