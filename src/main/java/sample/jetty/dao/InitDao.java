package sample.jetty.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import sample.jetty.domain.BaseBean;
import sample.jetty.util.DateUtil;

@Repository
public class InitDao {
	@Autowired
    private SqlSessionTemplate sqlSessionTemplate;
	
	public boolean checkTablesExistsOrNot() throws Exception {
		return ((Long)this.sqlSessionTemplate.selectOne("SAMPLE_INIT_MAPPER.checkTablesExistsOrNot")) >= 0L;
	}
	
	public BaseBean checkTodayScanedOrNot() throws Exception {
		return (BaseBean)this.sqlSessionTemplate.selectOne("SAMPLE_INIT_MAPPER.checkTodayScanedOrNot");
	}
	
	public void execute(String sql) throws Exception {
		this.sqlSessionTemplate.update("SAMPLE_INIT_MAPPER.executeSql", sql);
	}
	
	@SuppressWarnings("unchecked")
	public <LogBean> List<LogBean> sacnDelaiedTodoList(Date today) throws Exception {
		Map<String, Object> paramMap = new HashMap<String , Object>();
		paramMap.put("today", today);
		paramMap.put("todayStr", DateUtil.date2String(today, DateUtil.PATTERN_STANDARD10H));
		return (List<LogBean>)this.sqlSessionTemplate.selectList("SAMPLE_INIT_MAPPER.sacnDelaiedTodoList", paramMap);
	}
	
	public long addScanDealRecored(BaseBean baseBean) throws Exception {
		return this.sqlSessionTemplate.insert("SAMPLE_INIT_MAPPER.addScanDealRecored", baseBean);
	}
	
	public int updateScanDealRecored(BaseBean baseBean) throws Exception {
		return this.sqlSessionTemplate.update("SAMPLE_INIT_MAPPER.updateScanDealRecored", baseBean);
	}
	
	
}
