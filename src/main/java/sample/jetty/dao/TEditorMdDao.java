package sample.jetty.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import sample.jetty.domain.LogBean;
import sample.jetty.domain.LogContentBean;

@Repository
public class TEditorMdDao {
	@Autowired
    private SqlSessionTemplate sqlSessionTemplate;
	
	@SuppressWarnings({ "unchecked", "hiding" })
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
	public <LogBean> List<LogBean> selectLogListOrderbyCreateDt(int startNum, int pageCount, String searchText) throws Exception {
		HashMap<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("startNum", startNum);
		paraMap.put("pageCount", pageCount);
		if(":todo".equalsIgnoreCase(searchText)){
			paraMap.put("todo", "todo");
		} else if (null != searchText) {
			paraMap.put("searchText", searchText);
		}
		return (List<LogBean>)this.sqlSessionTemplate.selectList("SAMPLE_EDITORMD_MAPPER.selectLogListOrderbyCreateDt", paraMap);
	}
	
	@SuppressWarnings({ "hiding", "unchecked" })
	public <LogBean> List<LogBean> selectLogListOrderbyFolderId(long folderId, int startNum, int pageCount, String searchText) throws Exception {
		HashMap<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("folderId", new Long(folderId));
		paraMap.put("startNum", new Long(startNum));
		paraMap.put("pageCount", new Long(pageCount));
		if(":todo".equalsIgnoreCase(searchText)){
			paraMap.put("todo", "todo");
		} else if (null != searchText) {
			paraMap.put("searchText", searchText);
		}
		return (List<LogBean>)this.sqlSessionTemplate.selectList("SAMPLE_EDITORMD_MAPPER.selectLogListOrderbyFolderId", paraMap);
	}
	
	@SuppressWarnings({ "unchecked", "hiding" })
	public <LogBean> List<LogBean> selectLogListOrderbyFolderIdArr(ArrayList<Long> folderIdArray, int startNum, int pageCount, String searchText) throws Exception {
		String folderIdArr = " ( ";
		int i = 0;
		for (Long folderId : folderIdArray) {
			if (0 == i) {
				folderIdArr += " TB_LOG_FOLDER.FOLDER_ID = '" + folderId + "' ";
			} else {
				folderIdArr += " or TB_LOG_FOLDER.FOLDER_ID = '" + folderId + "' ";
			}
			++i;
		}
		folderIdArr += " ) ";
		HashMap<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("folderIdArr", folderIdArr);
		paraMap.put("startNum", new Long(startNum));
		paraMap.put("pageCount", new Long(pageCount));
		if(":todo".equalsIgnoreCase(searchText)){
			paraMap.put("todo", "todo");
		} else if (null != searchText) {
			paraMap.put("searchText", searchText);
		}
		return (List<LogBean>)this.sqlSessionTemplate.selectList("SAMPLE_EDITORMD_MAPPER.selectLogListOrderbyFolderIdArr", paraMap);
	}
	
	@SuppressWarnings({ "hiding", "unchecked" })
	public <LogBean> List<LogBean> selectDeletedLogListOrderbyCreateDt(int startNum, int pageCount, String searchText) throws Exception {
		HashMap<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("startNum", new Long(startNum));
		paraMap.put("pageCount", new Long(pageCount));
		if(":todo".equalsIgnoreCase(searchText)){
			paraMap.put("todo", "todo");
		} else if (null != searchText) {
			paraMap.put("searchText", searchText);
		}
		return (List<LogBean>)this.sqlSessionTemplate.selectList("SAMPLE_EDITORMD_MAPPER.selectDeletedLogListOrderbyCreateDt", paraMap);
	}
	
	public boolean saveUpdateLogBean(LogBean logBean) throws Exception {
		boolean result = false;
		if (logBean.getId() <= 0L) {
			throw new Exception("the logBean's id is <= 0!");
		}
		result = this.sqlSessionTemplate.update("SAMPLE_EDITORMD_MAPPER.saveUpdateLogBean", logBean) > 0;
		return result;
	}
	
	public long addNewLogContentRecord(LogContentBean logContentBean) throws Exception {
		return this.sqlSessionTemplate.insert("SAMPLE_EDITORMD_MAPPER.addNewLogContentRecord", logContentBean);
	}
	
	public int updateLogContentRecord(LogContentBean logContentBean) throws Exception {
		if (logContentBean.getId() <= 0L) {
			throw new Exception("the logContentBean's id is <= 0!");
		}
		return this.sqlSessionTemplate.update("SAMPLE_EDITORMD_MAPPER.updateLogContentRecord", logContentBean);
	}
	
	public int deleteLogContentRecord(long id) throws Exception {
		if (id <= 0L) {
			throw new Exception("the logContentBean's id is <= 0!");
		}
		return this.sqlSessionTemplate.delete("SAMPLE_EDITORMD_MAPPER.deleteLogContentRecord", id);
	}
	
	public long addNewLogRecord(LogBean logBean) throws Exception {
		long result = this.sqlSessionTemplate.insert("SAMPLE_EDITORMD_MAPPER.addNewLogRecord", logBean);
		return result;
	}
	
	public boolean deleteLogById(long logId) throws Exception {
		return this.sqlSessionTemplate.insert("SAMPLE_EDITORMD_MAPPER.deleteLogById", logId) > 0;
	}
	
}
