package sample.jetty.web;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import sample.jetty.SampleJettyApplication;
import sample.jetty.domain.LogBean;
import sample.jetty.embedmysql.EmbedMySqlServer;
import sample.jetty.service.TEditorMdService;
import sample.jetty.util.DateUtil;

@Controller
public class EditorMdController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private TEditorMdService tEditorMdService;

	@RequestMapping("/showIframeMarkdownHTMLByLogId")
	public String showIframeMarkdownHTMLByLogId(@RequestParam(value="logId", required=false, defaultValue="0") long logId, HttpServletRequest request, Model model) {
		if (0L != logId) {
			model.addAttribute("logId", logId);
			LogBean logBean = tEditorMdService.selectEditorMdById(logId);
			//logBean.setLogContent("<a href=\"www.baidu.com\">百度链接</a>\r\n```\r\n<textarea>this is a test</textarea>\r\n```");
			model.addAttribute("log", logBean);
			return "editorHTML.md";
		} else {
			return "greeting";
		}
	}
	
	@RequestMapping("/showIframeMarkdownEditByLogId")
	public String showIframeMarkdownEditByLogId(@RequestParam(value="logId", required=false, defaultValue="0") long logId, HttpServletRequest request, Model model) {
		if (0L != logId) {
			model.addAttribute("logId", logId);
			LogBean logBean = tEditorMdService.selectEditorMdById(logId);
			//logBean.setLogContent("<a href=\"www.baidu.com\">百度链接</a>\r\n```\r\n<textarea>this is a test</textarea>\r\n```");
			model.addAttribute("log", logBean);
			return "editorEDIT.md";
		} else {
			return "greeting";
		}
	}
	
	/**
	 * 更新后保存日志
	 * @param logId
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/updateMarkdownEditByLogId", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> updateMarkdownEditByLogId(@RequestParam(value="logId", required=false, defaultValue="0") long logId, 
			@RequestParam(value="logTitle", required=false, defaultValue="-1") String logTitle, 
			@RequestParam(value="logContent", required=false, defaultValue="") String logContent, HttpServletRequest request, Model model) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		EmbedMySqlServer mysqldbServer = SampleJettyApplication.MAIN_THREAD_LOCAL.get("embedMysqlServer");
		String dbPath = mysqldbServer.getEmbedMySqlHome();
		File logFileDir = new File(dbPath).getParentFile().getAbsoluteFile();
		String filesDirStr = logFileDir.getAbsolutePath() + File.separator + "files" + File.separator + logId;
		
		if (0L != logId) {
			File selecedLogFile = new File(filesDirStr);
			if (!selecedLogFile.exists()) {
				selecedLogFile.mkdirs();
			}
			Date date = Calendar.getInstance().getTime();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd.HHmmss.SSS");
			String ctime = formatter.format(date);
			
			LogBean logBean = tEditorMdService.selectEditorMdById(logId);
			logBean.setLastUpdDt(date);
			if(!"-1".equalsIgnoreCase(logTitle)){
				logBean.setLogTitle(HtmlUtils.htmlEscape(logTitle));
			}
			// 存放一个title命名的空txt文件,已有就不管
			String chineseTxt = filesDirStr + File.separator + logId +"."+logBean.getLogTitle().replaceAll(" ", "")+".txt";
			File chineseFile = new File(chineseTxt);
			try {
				if (!chineseFile.exists()) {
					chineseFile.createNewFile();
				}
			} catch (Exception e) { // 如果这里有错是可以忽略的
			}
			
			try{
				// 执行入库操作
				logBean.setLogContent(logContent);
				String logDesc = HtmlUtils.htmlEscape(logContent);
				if (logDesc.length() > 500) {
					logDesc = logDesc.substring(0, 499);
				}
				logBean.setLogDesc(logDesc);
				tEditorMdService.saveUpdateLogContent(logBean);
				
				// 执行写入文件操作
				FileOutputStream outStream = null;
				OutputStreamWriter outStreamWriter = null;
				BufferedWriter bufWriter = null;
				PrintWriter printWriter = null;
				try{
					String logmd = filesDirStr + File.separator + logId+".md";
					String newMdFilePath = "";
					File logMdFile = new File(logmd);
					if (logMdFile.exists()) {
						// 把之前的加上时间戳，在生成一个最新的，最新的没有时间戳
						newMdFilePath = logMdFile.getAbsolutePath().subSequence(0, logMdFile.getAbsolutePath().lastIndexOf(".md")) + "."+ctime+".md";
						logMdFile.renameTo(new File(newMdFilePath));
						logMdFile = new File(logmd);
						logMdFile.createNewFile();
					} else {
						logMdFile.createNewFile();
					}
					// 写入文件
					outStream = new FileOutputStream(logMdFile);
					outStreamWriter = new OutputStreamWriter(outStream);
					bufWriter = new BufferedWriter(outStreamWriter);
					//printWriter = new PrintWriter(bufWriter);
					bufWriter.write(logBean.getLogContent());
					bufWriter.flush();
				} catch(Exception e) {
					// 入库成功，但是保存文件失败，则返回入库成功，但是保存文件失败  db_succeed
					resultMap.put("state", "update_db_succeed");
					resultMap.put("errorContent", "occured errors when saving into .md file\r\n" + e.getMessage());
					logger.error("EditorMdController.updateMarkdownEditByLogId", e);
				} finally {
					try {
						if (null != printWriter) {
							printWriter.close();
						}
					} catch (Exception e) {
					}
					try {
						if (null != bufWriter) {
							bufWriter.close();
						}
					} catch (Exception e) {
					}
					try {
						if (null != outStreamWriter) {
							outStreamWriter.close();
						}
					} catch (Exception e) {
					}
					try {
						if (null != outStream) {
							outStream.close();
						}
					} catch (Exception e) {
					}
				}
				
				// 都成功  返回 succeed
				resultMap.put("state", "update_succeed");
				resultMap.put("errorContent", "");
			} catch(Exception e){
				// 如果入库失败,则返回入库失败的信息 failed
				resultMap.put("state", "update_db_failed");
				resultMap.put("errorContent", "occured errors when saving into db\r\n" + e.getMessage());
				logger.error("EditorMdController.updateMarkdownEditByLogId", e);
			}
		} else {
			// logId 为0,直接返回保存报错的信息 failed
			resultMap.put("state", "update_logid_failed");
			resultMap.put("errorContent", "logId is null");
		}
		return resultMap;
	}
	
	
	/**
	 * 根据日志的id获取对应的日志记录
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping("/getEditorMdById")
	@ResponseBody
	public Map<String, Object> getEditorMdById(@RequestParam(value="id", required=false, defaultValue="0") long id, HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (id == 0L) {
			resultMap.put("log", null);
			return resultMap;
		}
		LogBean logBean = tEditorMdService.selectEditorMdById(id);
		if (null != logBean) {
			resultMap.put("log", logBean);
			return resultMap;
		} else {
			resultMap.put("log", null);
			return resultMap;
		}
	}
	
	/**
	 * layout center 中展示的根据folderid获取
	 * @param folderId
	 * @param request
	 * @return
	 */
	@RequestMapping("/getLogListByFolderId")
	@ResponseBody
	public Map<String, Object> getLogListByFolderId(@RequestParam(value="folderId", required=false, defaultValue="0") long folderId, 
			@RequestParam(value="rownum", required=false, defaultValue="20") int rownum,
			@RequestParam(value="searchText", required=false) String searchText, HttpServletRequest request) {
		if ("".equals(searchText)) {
			searchText = null;
		}
		int pageCount = 20;
		/*
		 * folderId 0:最新的文档; -1:我的文件夾 ; -2: 垃圾箱  (delete==1)
		 */
		Map<String, Object> resultMap = new HashMap<String, Object>();
//		if (0 == folderId) {
//			resultMap.put("state", "get_failed");
//			resultMap.put("errorContent", "folderId is 0");
//			resultMap.put("rows", null);
//			resultMap.put("counts", 0);
//			return resultMap;
//		}
		List<LogBean> logBeanList = null;
		try {
			logBeanList = tEditorMdService.selectLogListByFolderId(folderId, rownum - pageCount, pageCount, searchText);

			resultMap.put("state", "get_succeed");
			resultMap.put("errorContent", "");
			resultMap.put("rows", logBeanList);
			resultMap.put("counts", logBeanList.size());
		} catch (Exception e) {
			logger.error("EditorMdController.getLogListByFolderId", e);
			resultMap.put("state", "get_failed");
			resultMap.put("errorContent", "query occured exception");
			resultMap.put("rows", null);
			resultMap.put("counts", 0);
		}
		return resultMap;
	}
	
	@RequestMapping("/setTodoProperties")
	@ResponseBody
	public Map<String, Object> setTodoProperties(@RequestParam(value="logId", required=false, defaultValue="0") long logId,
			@RequestParam(value="startDt", required=false, defaultValue="0") String startDt,
			@RequestParam(value="endDt", required=false, defaultValue="") String endDt,
			@RequestParam(value="deadline", required=false, defaultValue="") String deadline,
			@RequestParam(value="logType", required=false, defaultValue="0") int logType,
			@RequestParam(value="logStat", required=false, defaultValue="0") int logStat,
			HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			if(logId <= 0){
				throw new Exception("the logId is null");
			}
			LogBean selectedLogBean = tEditorMdService.selectEditorMdById(logId);
			selectedLogBean.setLastUpdDt(new Date());
			selectedLogBean.setStartDt(DateUtil.string2Date(startDt, DateUtil.PATTERN_STANDARD19H));
			if(null != endDt && !"".equals(endDt)) {
				selectedLogBean.setEndDt(DateUtil.string2Date(endDt, DateUtil.PATTERN_STANDARD19H));
			}
			if (null != deadline && !"".equals(deadline)) {
				selectedLogBean.setDeadline(DateUtil.string2Date(deadline, DateUtil.PATTERN_STANDARD19H));
			}
			selectedLogBean.setLogType(logType);
			selectedLogBean.setLogStat(logStat);
			tEditorMdService.setTodoProperties(selectedLogBean);
			resultMap.put("state", "update_succeed");
			resultMap.put("errorContent", "");
		} catch (Exception e) {
			logger.error("EditorMdController.setTodoProperties", e);
			resultMap.put("state", "update_failed");
			resultMap.put("errorContent", "occured errors "+e.getMessage());
		}
		return resultMap;
	}
	
	
	@RequestMapping("/addNewLog")
	@ResponseBody
	public Map<String, Object> addNewLog(@RequestParam(value="parentFolderId", required=false, defaultValue="-1") long parentFolderId, 
			HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			tEditorMdService.addNewLog(parentFolderId);
			resultMap.put("state", "add_succeed");
			resultMap.put("errorContent", "");
		} catch (Exception e) {
			logger.error("EditorMdController.addNewLog", e);
			resultMap.put("state", "add_failed");
			resultMap.put("errorContent", "occured errors "+e.getMessage());
		}
		return resultMap;
	}
	
	@RequestMapping("/changeTitle")
	@ResponseBody
	public Map<String, Object> changeTitle(@RequestParam(value="logId", required=false, defaultValue="0") long logId, 
			@RequestParam(value="logTitle", required=false, defaultValue="-1") String logTitle,
			HttpServletRequest request, Model model) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		EmbedMySqlServer mysqldbServer = SampleJettyApplication.MAIN_THREAD_LOCAL.get("embedMysqlServer");
		String dbPath = mysqldbServer.getEmbedMySqlHome();
		File logFileDir = new File(dbPath).getParentFile().getAbsoluteFile();
		String filesDirStr = logFileDir.getAbsolutePath() + File.separator + "files" + File.separator + logId;
		
		if (0L != logId) {
			File selecedLogFile = new File(filesDirStr);
			if (!selecedLogFile.exists()) {
				selecedLogFile.mkdirs();
			}
			Date date = Calendar.getInstance().getTime();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd.HHmmss.SSS");
			String ctime = formatter.format(date);
			
			LogBean logBean = tEditorMdService.selectEditorMdById(logId);
			logBean.setLastUpdDt(date);
			logBean.setLogTitle(HtmlUtils.htmlEscape(logTitle));
			// 存放一个title命名的空txt文件,已有就不管
			String chineseTxt = filesDirStr + File.separator + logId +"."+logBean.getLogTitle().replaceAll(" ", "")+".txt";
			File chineseFile = new File(chineseTxt);
			try {
				if (!chineseFile.exists()) {
					chineseFile.createNewFile();
				}
			} catch (Exception e) { // 如果这里有错是可以忽略的
			}
			
			try{
				// 执行入库操作
				tEditorMdService.saveUpdateLogContent(logBean);
				
				// 都成功  返回 succeed
				resultMap.put("state", "update_succeed");
				resultMap.put("errorContent", "");
			} catch(Exception e){
				// 如果入库失败,则返回入库失败的信息 failed
				resultMap.put("state", "update_db_failed");
				resultMap.put("errorContent", "occured errors when saving into db\r\n" + e.getMessage());
				logger.error("EditorMdController.updateMarkdownEditByLogId", e);
			}
		} else {
			// logId 为0,直接返回保存报错的信息 failed
			resultMap.put("state", "update_logid_failed");
			resultMap.put("errorContent", "logId is null");
		}
		return resultMap;
	}
	
	@RequestMapping("/deleteLogById")
	@ResponseBody
	public Map<String, Object> deleteLogById(@RequestParam(value="logId", required=false, defaultValue="0") long logId,
			HttpServletRequest request, Model model) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (0L != logId) {
			try {
				tEditorMdService.deleteLogById(logId);
				resultMap.put("state", "update_succeed");
				resultMap.put("errorContent", "");
			} catch(Exception e) {
				logger.error("EditorMdController.deleteLogById", e);
				resultMap.put("state", "update_failed");
				resultMap.put("errorContent", e.getMessage());
			}
		} else {
			resultMap.put("state", "update_failed");
			resultMap.put("errorContent", "logId is null");
		}
		return resultMap;
	}
}
