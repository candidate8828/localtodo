package sample.jetty.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sample.jetty.dao.TEditorMdDao;
import sample.jetty.domain.LogBean;
import sample.jetty.domain.LogContentBean;

@Service
@Transactional
public class TEditorMdService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private TEditorMdDao tEditorMdDao;
	
	@Transactional(readOnly=true)
	public LogBean selectEditorMdById(long id) {
		LogBean logbean = null;
		try {
			logbean = tEditorMdDao.selectEditorMdById(id);
		} catch(Exception e) {
			logbean = null;
			logger.error("TEditorMdService.selectEditorMdById", e);
		}
		if (null == logbean) {
			return null;
		} else {
			List<LogContentBean> contentList = null;
			try {
				contentList = tEditorMdDao.selectEditorMdContentListById(id);
			} catch(Exception e) {
				contentList = null;
				logger.error("TEditorMdService.selectEditorMdContentListById", e);
			}
			if (null == contentList || contentList.isEmpty()) {
				logbean.setLogContent("");
			} else {
				StringBuffer contentBuf = new StringBuffer("");
				for (LogContentBean logContent : contentList) {
					contentBuf.append(logContent.getLogContent());
				}
				logbean.setLogContent(contentBuf.toString());
				contentBuf = null;
			}
			return logbean;
		}
	}
	
	@Transactional(readOnly=true)
	public List<LogBean> selectLogListByFolderId(long folderId, int startNum, int pageCount) throws Exception {
		List<LogBean> resultList = null;
		// -1: 最新的文档
		if (-1L == folderId) {
			resultList = tEditorMdDao.selectLogListOrderbyCreateDt(startNum, pageCount);
		} else // -2: 垃圾箱
			if (-2L == folderId) {
			
		} else {
			
		}
		return ((null==resultList)?(new ArrayList<LogBean>()):resultList);
	}
	
}
