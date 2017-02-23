package sample.jetty.dao;

import java.util.HashMap;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import sample.jetty.domain.LogBean;

@Repository
public class TEditorMdDao {
	@Autowired
    private SqlSessionTemplate sqlSessionTemplate;
	
	@SuppressWarnings({ "unchecked" })
	public <LogContentBean> List<LogContentBean> selectEditorMdContentListById(long id) throws Exception {
		return (List<LogContentBean>)this.sqlSessionTemplate.selectList("SAMPLE_EDITORMD_MAPPER.selectEditorMdContentListById", id);
	}
	
	public LogBean selectEditorMdById(long id) throws Exception {
		return (LogBean)this.sqlSessionTemplate.selectOne("SAMPLE_EDITORMD_MAPPER.selectEditorMdById", id);
	}
	
	/**
	 * 最新的文档 - 查出指定 rownum 区间的log记录(按创建时间倒序)
	 * @param startNum
	 * @param endNum
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "hiding", "unchecked" })
	public <LogBean> List<LogBean> selectLogListOrderbyCreateDt(int startNum, int pageCount) throws Exception {
		HashMap<String, Integer> paraMap = new HashMap<String, Integer>();
		paraMap.put("startNum", startNum);
		paraMap.put("pageCount", pageCount);
		return (List<LogBean>)this.sqlSessionTemplate.selectList("SAMPLE_EDITORMD_MAPPER.selectLogListOrderbyCreateDt", paraMap);
	}
}
