package sample.jetty.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sample.jetty.dao.InitDao;

@Service
@Transactional
public class InitService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private InitDao initDao;
	
	@Transactional(readOnly=true)
	public boolean checkTablesExistsOrNot() throws Exception {
		return initDao.checkTablesExistsOrNot();
	}
	
	@Transactional
	public boolean init() throws Exception {
		//-- 字典表
		String sql_001 = "create table TB_DICTIONARY ( id bigint primary key auto_increment, dic_type varchar(50), dic_code varchar(200), dic_content varchar(300), dic_order_by int default '-1', active_flag char(1) default 'Y', is_delete int default '0', dic_comments varchar(300), root_id bigint default '-1', parent_id bigint default '-1' )";
		//-- 人员表
		String sql_002 = "create table TB_USER ( id bigint primary key auto_increment, create_dt timestamp, created_by bigint, last_upd_dt timestamp, last_upded_by bigint, username varchar(20), password varchar(20), dispname varchar(50), is_delete int default '0', comments varchar(2000) )";
		String sql_003 = "insert into tb_user (create_dt,created_by,last_upd_dt,last_upded_by,username,password,dispname,is_delete,comments) values (now(),1,now(),1,'admin','a123456','管理员','0','系统管理员')";
		
		//-- 工作记录表
		String sql_004 = "create table TB_WORK_LOG ( id bigint primary key auto_increment, create_dt timestamp, created_by bigint, last_upd_dt timestamp, last_upded_by bigint, start_dt timestamp, end_dt timestamp, deadline timestamp, log_type int, log_stat int, is_delete int default '0', log_title varchar(500), log_desc varchar(500) )";
		//-- 工作内容表
		String sql_005 = "create table TB_WORK_CONTENT ( id bigint primary key auto_increment, create_dt timestamp, created_by bigint, last_upd_dt timestamp, last_upded_by bigint, log_id bigint, order_by int, is_delete int default '0', log_content varchar(2000) )";
		//-- 按照folder分类  folder表
		String sql_006 = "create table TB_FOLDER ( id bigint primary key auto_increment, folder_name  varchar(500), create_dt timestamp, created_by bigint, last_upd_dt timestamp, last_upded_by bigint, is_delete int default '0', parent_id bigint default '0', order_by int )";
		//-- 按照folder分类  folder与log关联表
		String sql_007 = "create table TB_LOG_FOLDER( id bigint primary key auto_increment, create_dt timestamp, created_by bigint, last_upd_dt timestamp, last_upded_by bigint, log_id bigint, folder_id bigint )";
		//-- 上传的文件表
		String sql_008 = "create table TB_FILE (id bigint primary key auto_increment, create_dt timestamp, created_by bigint, last_upd_dt timestamp, last_upded_by bigint, log_id bigint, file_name varchar(1000), create_time bigint, relate_path varchar(1000), file_type varchar(100) )";
		
		initDao.execute(sql_001);
		initDao.execute(sql_002);
		initDao.execute(sql_003);
		initDao.execute(sql_004);
		initDao.execute(sql_005);
		initDao.execute(sql_006);
		initDao.execute(sql_007);
		initDao.execute(sql_008);
		
		return true;
	}
}
