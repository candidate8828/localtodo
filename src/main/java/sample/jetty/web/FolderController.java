package sample.jetty.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import sample.jetty.domain.TreeBean;
import sample.jetty.service.TFolderService;

@Controller
public class FolderController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private TFolderService tFolderService;

	@RequestMapping("/getFolders")
	@ResponseBody
	public List<TreeBean> getFolders(@RequestParam(value="id", required=false, defaultValue="0") long id, HttpServletRequest request) {
		List<TreeBean> folderList = null;
		try {
			folderList = tFolderService.selectChildrenFoldersByParentId(id);
		} catch (Exception e) {
			logger.error("/getFolders", e);
		}
		if (null != folderList) {
			logger.debug("folderList="+folderList.toArray());
			return folderList;
		} else {
			return new ArrayList<TreeBean>();
		}
	}
	
	/**
	 * 
	 * @param logId 
	 * @param id expanded folder's id
	 * @param request
	 * @return
	 */
	@RequestMapping("/getCheckedFolders")
	@ResponseBody
	public List<TreeBean> getCheckedFolders(@RequestParam(value="logId", required=false, defaultValue="0") long logId,@RequestParam(value="id", required=false, defaultValue="0") long id, HttpServletRequest request) {
		List<TreeBean> folderList = null;
		logger.debug("logId is ["+logId+"]");
		logger.debug("folderId is ["+id+"]");
		try {
			if (id == 0) {
				// 获取整个树，并设置 checked by logId
				folderList = tFolderService.getCheckedFoldersByLogId(logId);
			} else {
				folderList = new ArrayList<TreeBean>();
			}
		} catch (Exception e) {
			logger.error("/getFolders", e);
		}
		if (null != folderList) {
			logger.debug("folderList="+folderList.toArray());
			return folderList;
		} else {
			return new ArrayList<TreeBean>();
		}
	}
	
	@RequestMapping("/changeLogFolderRelation")
	@ResponseBody
	public Map<String, Object> changeLogFolderRelation(@RequestParam(value="logId", required=false, defaultValue="0") long logId, @RequestParam(value="checkedFolderIds", required=false, defaultValue="0") String checkedFolderIds, HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (logId == 0L || "0".equals(checkedFolderIds)) {
			resultMap.put("state", "update_failed");
			resultMap.put("errorContent", "the logId is 0 or checkedFolderIds is 0");
		} else {
			try {
				List<Long> folderIdList = new ArrayList<Long>();
				String[] folderIdStrArr = checkedFolderIds.split(",");
				for (String folderIdStr : folderIdStrArr) {
					folderIdList.add(new Long(folderIdStr));
				}
				tFolderService.changeLogFolderRelation(logId, folderIdList);
				resultMap.put("state", "update_succeed");
				resultMap.put("errorContent", "");
			} catch (Exception e) {
				//logger.error("/moveUp", e);
				resultMap.put("state", "update_failed");
				resultMap.put("errorContent", e.getMessage());
			}
		}
		return resultMap;
	}
	
	@RequestMapping("/checkFolderExistsOrNot")
	@ResponseBody
	public boolean checkFolderExistsOrNot(@RequestParam(value="id", required=true) long id, 
			@RequestParam(value="folderName", required=true) String folderName, 
			HttpServletRequest request) {
		if (!StringUtils.isEmpty(folderName)) {
			folderName = folderName.trim();
		}
		boolean isExists = false;
		try {
			isExists = tFolderService.checkFolderExistsOrNot(folderName, id);
		} catch (Exception e) {
			logger.error("/checkFolderExistsOrNot", e);
		}
 		return !isExists;
	}
	
	
	@RequestMapping("/addNewFolder")
	@ResponseBody
	public boolean addNewFolder(@RequestParam(value="id", required=true) long id, 
			@RequestParam(value="folderName", required=true) String folderName, 
			HttpServletRequest request) {
		if (!StringUtils.isEmpty(folderName)) {
			folderName = folderName.trim();
		}
		boolean addResult = false;
		if (!StringUtils.isEmpty(folderName)) {
			try {
				addResult = tFolderService.addNewFolder(folderName, id);
			} catch (Exception e) {
				logger.error("/addNewFolder", e);
			}
		}
		return addResult;
	}
	
	@RequestMapping("/deleteFolder")
	@ResponseBody
	public boolean deleteFolder(@RequestParam(value="id", required=true) long id, HttpServletRequest request) {
		boolean delOpResult = false;
		/*if (1L == id) {
			return delOpResult;
		}*/
		try {
			delOpResult = tFolderService.deleteFolderById(id);
		} catch (Exception e) {
			logger.error("/deleteFolder", e);
		}
		return delOpResult;
	}
	
	@RequestMapping("/moveUp")
	@ResponseBody
	public Map<String, Object> moveUp(@RequestParam(value="selectedFolderId", required=true) long selectedFolderId, 
			HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			tFolderService.moveUp(selectedFolderId);
			resultMap.put("state", "move_succeed");
			resultMap.put("errorContent", "");
		} catch (Exception e) {
			//logger.error("/moveUp", e);
			resultMap.put("state", "move_failed");
			resultMap.put("errorContent", e.getMessage());
		}
		return resultMap;
	}
	
	@RequestMapping("/moveDown")
	@ResponseBody
	public Map<String, Object> moveDown(@RequestParam(value="selectedFolderId", required=true) long selectedFolderId, 
			HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			tFolderService.moveDown(selectedFolderId);
			resultMap.put("state", "move_succeed");
			resultMap.put("errorContent", "");
		} catch (Exception e) {
			//logger.error("/moveUp", e);
			resultMap.put("state", "move_failed");
			resultMap.put("errorContent", e.getMessage());
		}
		return resultMap;
	}
	
	@RequestMapping("/updateFolderName")
	@ResponseBody
	public boolean updateFolderName(@RequestParam(value="id", required=true) long id, 
			@RequestParam(value="folderName", required=true) String folderName, 
			HttpServletRequest request) {
		if (!StringUtils.isEmpty(folderName)) {
			folderName = folderName.trim();
		}
		boolean updateResult = false;
		if (!StringUtils.isEmpty(folderName)) {
			try {
				updateResult = tFolderService.updateFolderName(folderName, id);
			} catch (Exception e) {
				logger.error("/addNewFolder", e);
			}
		}
		return updateResult;
	}
	
	
}
