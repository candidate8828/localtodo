package sample.jetty.web;

import java.util.ArrayList;
import java.util.List;

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
		if(1L == id || 2L == id || 3L == id){
			return delOpResult;
		}
		try {
			delOpResult = tFolderService.deleteFolderById(id);
		} catch (Exception e) {
			logger.error("/deleteFolder", e);
		}
		return delOpResult;
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
