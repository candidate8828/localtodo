package sample.jetty.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sample.jetty.dao.TEditorMdDao;
import sample.jetty.dao.TFolderDao;
import sample.jetty.domain.LogBean;
import sample.jetty.domain.LogContentBean;

@Service
@Transactional
public class TEditorMdService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private TEditorMdDao tEditorMdDao;
	@Autowired
	private TFolderDao tFolderDao;
	
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
		// 0: 最新的文档
		if (0L == folderId) {
			resultList = tEditorMdDao.selectLogListOrderbyCreateDt(startNum, pageCount);
		} // -1: 我的文件夹
		else if (-1L == folderId) {
			resultList = tEditorMdDao.selectLogListOrderbyFolderId(-1L, startNum, pageCount);
		} else if (-2L == folderId) { // -2: 垃圾箱  (delete==1)
			resultList = tEditorMdDao.selectDeletedLogListOrderbyCreateDt(startNum, pageCount);
		} else {
			resultList = tEditorMdDao.selectLogListOrderbyFolderId(folderId, startNum, pageCount);
		}
		return ((null==resultList)?(new ArrayList<LogBean>()):resultList);
	}
	
	@Transactional
	public boolean saveUpdateLogContent(LogBean logBean) throws Exception {
		boolean result = false;
		int logContentMaxLength = 2000;
		if (logBean.getId() <= 0L) {
			throw new Exception("the logBean's id is <= 0!");
		}
		tEditorMdDao.saveUpdateLogContent(logBean);
		List<LogContentBean> contentList = tEditorMdDao.selectEditorMdContentListById(logBean.getId());
		String logContent = logBean.getLogContent();
		LogContentBean newLogContentBean = null;
		if (null == contentList || contentList.isEmpty()) {// 如果原来数据库中没有对应的 logContent 记录
			// 判断后直接insert
			if (logContent.length() > logContentMaxLength) { // 大于单个logcontent的最大长度，则存入多条记录
				int pieceNum = logContent.length() / logContentMaxLength;
				int extralNum = logContent.length() % logContentMaxLength;
				pieceNum = (extralNum > 0) ? (pieceNum + 1) : (pieceNum);
				List<LogContentBean> newLogContentBeanlist = new ArrayList<LogContentBean>();
				for (int i = 0; i < pieceNum; i++) {
					newLogContentBean = new LogContentBean();
					newLogContentBean.setCreatedBy(logBean.getLastUpdedBy());
					newLogContentBean.setCreateDt(logBean.getLastUpdDt());
					newLogContentBean.setLastUpdedBy(logBean.getLastUpdedBy());
					newLogContentBean.setLastUpdDt(logBean.getLastUpdDt());
					newLogContentBean.setLogId(logBean.getId());
					newLogContentBean.setOrderBy(i+1);
					if (i == pieceNum-1) {
						newLogContentBean.setLogContent(logContent.substring(i*logContentMaxLength));
					} else {
						newLogContentBean.setLogContent(logContent.substring(i*logContentMaxLength, (i+1)*logContentMaxLength));
					}
					newLogContentBeanlist.add(newLogContentBean);
				}
				for (LogContentBean logContentBean : newLogContentBeanlist) {
					tEditorMdDao.addNewLogContentRecord(logContentBean);
				}
			} else { // 不超过(<=)单个logcontent的最大长度，则存入一条记录
				newLogContentBean = new LogContentBean();
				newLogContentBean.setCreatedBy(logBean.getLastUpdedBy());
				newLogContentBean.setCreateDt(logBean.getLastUpdDt());
				newLogContentBean.setLastUpdedBy(logBean.getLastUpdedBy());
				newLogContentBean.setLastUpdDt(logBean.getLastUpdDt());
				newLogContentBean.setLogId(logBean.getId());
				newLogContentBean.setOrderBy(1);
				newLogContentBean.setLogContent(logContent);
				tEditorMdDao.addNewLogContentRecord(newLogContentBean);
			}
		} else {// 如果原来数据库中有对应的 logContent 记录存在
			// 判断后看具体情况具体处理
			if (logContent.length() > logContentMaxLength) { // 大于单个logcontent的最大长度，则存入多条记录
				int pieceNum = logContent.length() / logContentMaxLength;
				int extralNum = logContent.length() % logContentMaxLength;
				pieceNum = (extralNum > 0) ? (pieceNum + 1) : (pieceNum);
				
				if (contentList.size() < pieceNum) {  // 如果新记录数比原来更多
					long createdBy = -1L;
					Date createDt = null;
					for (int i = 0; i < pieceNum; i++) {
						if (i+1 <= contentList.size()) {
							newLogContentBean = contentList.get(i);
							createdBy = newLogContentBean.getCreatedBy();
							createDt = newLogContentBean.getCreateDt();
							
							newLogContentBean.setLastUpdedBy(logBean.getLastUpdedBy());
							newLogContentBean.setLastUpdDt(logBean.getLastUpdDt());
							newLogContentBean.setLogId(logBean.getId());
							newLogContentBean.setOrderBy(i+1);
							newLogContentBean.setLogContent(logContent.substring(i*logContentMaxLength, (i+1)*logContentMaxLength));
							tEditorMdDao.updateLogContentRecord(newLogContentBean);
						} else {
							newLogContentBean = new LogContentBean();
							newLogContentBean.setCreatedBy(createdBy);
							newLogContentBean.setCreateDt(createDt);
							newLogContentBean.setLastUpdedBy(logBean.getLastUpdedBy());
							newLogContentBean.setLastUpdDt(logBean.getLastUpdDt());
							newLogContentBean.setLogId(logBean.getId());
							newLogContentBean.setOrderBy(i+1);
							if (i == pieceNum-1) {
								newLogContentBean.setLogContent(logContent.substring(i*logContentMaxLength));
							} else {
								newLogContentBean.setLogContent(logContent.substring(i*logContentMaxLength, (i+1)*logContentMaxLength));
							}
							tEditorMdDao.addNewLogContentRecord(newLogContentBean);
						}
						newLogContentBean = null;
					}
				} else { // 如果新记录数比原来更少或相同
					for (int i=0; i < contentList.size(); i++) {
						newLogContentBean = contentList.get(i);
						if (i <= pieceNum - 1) {
							newLogContentBean.setLastUpdedBy(logBean.getLastUpdedBy());
							newLogContentBean.setLastUpdDt(logBean.getLastUpdDt());
							newLogContentBean.setLogId(logBean.getId());
							newLogContentBean.setOrderBy(i+1);
							if (i == pieceNum-1) {
								newLogContentBean.setLogContent(logContent.substring(i*logContentMaxLength));
							} else {
								newLogContentBean.setLogContent(logContent.substring(i*logContentMaxLength, (i+1)*logContentMaxLength));
							}
							tEditorMdDao.updateLogContentRecord(newLogContentBean);
						} else {
							// 删除多余的
							tEditorMdDao.deleteLogContentRecord(newLogContentBean.getId());
						}
						newLogContentBean = null;
					}
				}
			} else { // 不超过(<=)单个logcontent的最大长度，则将最新的 logContent update到第一个记录中,后面的记录设置为空字符串
				for (int i = 0; i < contentList.size(); i++) {
					if (i == 0) {
						newLogContentBean = contentList.get(i);
						newLogContentBean.setLastUpdedBy(logBean.getLastUpdedBy());
						newLogContentBean.setLastUpdDt(logBean.getLastUpdDt());
						newLogContentBean.setLogId(logBean.getId());
						newLogContentBean.setOrderBy(i+1);
						newLogContentBean.setLogContent(logContent);
						tEditorMdDao.updateLogContentRecord(newLogContentBean);
					} else {
						newLogContentBean = contentList.get(i);
						tEditorMdDao.deleteLogContentRecord(newLogContentBean.getId());
					}
					newLogContentBean = null;
				}
			}
		}
		result = true;
		return result;
	}
	
	@Transactional
	@SuppressWarnings("unused")
	public boolean addNewLog(long parentFolderId) throws Exception {
		Date date = Calendar.getInstance().getTime();
		LogBean logBean = new LogBean();
		logBean.setCreateDt(date);
		logBean.setLastUpdDt(date);
		logBean.setLogContent("");
		logBean.setLogDesc("");
		logBean.setCreatedBy(1L);
		logBean.setLastUpdedBy(1L);
		logBean.setLogTitle("md");
		logBean.setStartDt(date);
		// 保存入库
		tEditorMdDao.addNewLogRecord(logBean);
		long newId = tFolderDao.addFolderAndLogRelation(parentFolderId, logBean.getId());
		return true;
	}
}
