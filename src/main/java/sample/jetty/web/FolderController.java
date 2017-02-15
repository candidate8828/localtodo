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
	public List<TreeBean> getFolders(@RequestParam(value="id", required=false, defaultValue="0") String id, HttpServletRequest request) {
		//logger.debug("id="+id);
		List<TreeBean> folderList = null;
		//if ("All".equalsIgnoreCase(id)) {
		//	folderList = tFolderService.selectRootFolders();
		//} else {
			folderList = tFolderService.selectChildrenFoldersByParentId(Long.valueOf(id).longValue());
		//}
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
		boolean isExists = tFolderService.checkFolderExistsOrNot(folderName, id);
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
		if (!StringUtils.isEmpty(folderName)) {
			return tFolderService.addNewFolder(folderName, id);
		} else {
			return false;
		}
	}
	
	@RequestMapping("/deleteFolder")
	@ResponseBody
	public boolean deleteFolder(@RequestParam(value="id", required=true) long id, HttpServletRequest request) {
		boolean delOpResult = false;
		if(1L == id || 2L == id || 3L == id){
			return delOpResult;
		}
		delOpResult = tFolderService.deleteFolderById(id);
		return delOpResult;
	}
}
