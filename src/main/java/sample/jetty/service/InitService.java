package sample.jetty.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sample.jetty.dao.InitDao;
import sample.jetty.dao.TEditorMdDao;
import sample.jetty.domain.BaseBean;
import sample.jetty.domain.LogBean;
import sample.jetty.util.DateUtil;

@Service
@Transactional
public class InitService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private InitDao initDao;
	@Autowired
	private TEditorMdDao tEditorMdDao;
	
	@Transactional(readOnly=true)
	public boolean checkTablesExistsOrNot() throws Exception {
		return initDao.checkTablesExistsOrNot();
	}
	
	@Transactional(readOnly=true)
	public BaseBean checkTodayScanedOrNot() throws Exception {
		BaseBean baseBean = initDao.checkTodayScanedOrNot();
		return baseBean;
	}
	
	@Transactional
	public void dealDelayRecored(BaseBean selectedBaseBean) throws Exception {
		Date today = new Date();
		today = DateUtil.string2Date(DateUtil.date2String(today, DateUtil.PATTERN_STANDARD10H)+" 00:00:00", DateUtil.PATTERN_STANDARD19H);
		String doneOrNot = null;
		if (null == selectedBaseBean) {
			doneOrNot = null;
		} else {
			if (selectedBaseBean.getCreateDt().getTime() >= today.getTime()) {
				//说明当天已经做过了
				doneOrNot = "true";
			} else {
				//说明当天还没有做过
				doneOrNot = "false";
			}
		}
		List<LogBean> delayList = initDao.sacnDelaiedTodoList(today);
		today = new Date();
		if (null != delayList && !delayList.isEmpty()) {
			for (LogBean logBean : delayList) {
				logBean.setLastUpdDt(today);
				logBean.setLogStat(4); // 4:延期
				tEditorMdDao.saveUpdateLogBean(logBean);
			}
		}
		if (null == doneOrNot) {
			// 直接insert
			selectedBaseBean = new BaseBean();
			selectedBaseBean.setCreatedBy(1L);
			selectedBaseBean.setLastUpdedBy(1L);
			selectedBaseBean.setCreateDt(today);
			selectedBaseBean.setLastUpdDt(today);
			initDao.addScanDealRecored(selectedBaseBean);
		} else if ("false".equalsIgnoreCase(doneOrNot)) {
			// 做update
			selectedBaseBean.setCreateDt(today);
			selectedBaseBean.setLastUpdDt(today);
			initDao.updateScanDealRecored(selectedBaseBean);
		}
	}
	
	@Transactional
	public boolean init() throws Exception {
		//-- 字典表
		String sql_001 = "create table test.TB_DICTIONARY ( id bigint primary key auto_increment, dic_type varchar(50), dic_code varchar(200), dic_content varchar(300), dic_order_by int default '-1', active_flag char(1) default 'Y', is_delete int default '0', dic_comments varchar(300), root_id bigint default '-1', parent_id bigint default '-1' )";
		//-- 人员表
		String sql_002 = "create table test.TB_USER ( id bigint primary key auto_increment, create_dt timestamp, created_by bigint, last_upd_dt timestamp, last_upded_by bigint, username varchar(20), password varchar(20), dispname varchar(50), is_delete int default '0', comments varchar(2000) )";
		String sql_003 = "insert into test.TB_USER (CREATE_DT,CREATED_BY,LAST_UPD_DT,LAST_UPDED_BY,USERNAME,PASSWORD,DISPNAME,IS_DELETE,COMMENTS) values (now(),1,now(),1,'admin','a123456','管理员','0','系统管理员')";
		
		//-- 工作记录表
		String sql_004 = "create table test.TB_WORK_LOG ( id bigint primary key auto_increment, create_dt timestamp, created_by bigint, last_upd_dt timestamp, last_upded_by bigint, start_dt timestamp, end_dt timestamp, deadline timestamp, log_type int, log_stat int, is_delete int default '0', log_title varchar(500), log_desc varchar(500) )";
		//-- 工作内容表
		String sql_005 = "create table test.TB_WORK_CONTENT ( id bigint primary key auto_increment, create_dt timestamp, created_by bigint, last_upd_dt timestamp, last_upded_by bigint, log_id bigint, order_by int, is_delete int default '0', log_content varchar(2000) )";
		//-- 按照folder分类  folder表
		String sql_006 = "create table test.TB_FOLDER ( id bigint primary key auto_increment, folder_name  varchar(500), create_dt timestamp, created_by bigint, last_upd_dt timestamp, last_upded_by bigint, is_delete int default '0', parent_id bigint default '0', order_by int )";
		//-- 按照folder分类  folder与log关联表
		String sql_007 = "create table test.TB_LOG_FOLDER( id bigint primary key auto_increment, create_dt timestamp, created_by bigint, last_upd_dt timestamp, last_upded_by bigint, log_id bigint, folder_id bigint )";
		//-- 上传的文件表
		String sql_008 = "create table test.TB_FILE (id bigint primary key auto_increment, create_dt timestamp, created_by bigint, last_upd_dt timestamp, last_upded_by bigint, log_id bigint, file_name varchar(1000), create_time bigint, relate_path varchar(1000), file_type varchar(100) )";
		//-- 用于记录每天第一次页面访问时对所有待办进行过期扫描的记录
		String sql_009 = "create table test.TB_SCAN ( id bigint primary key auto_increment, create_dt timestamp, created_by bigint, last_upd_dt timestamp, last_upded_by bigint )";
		
		initDao.execute(sql_001);
		initDao.execute(sql_002);
		initDao.execute(sql_003);
		initDao.execute(sql_004);
		initDao.execute(sql_005);
		initDao.execute(sql_006);
		initDao.execute(sql_007);
		initDao.execute(sql_008);
		initDao.execute(sql_009);
		
		return true;
	}
}
