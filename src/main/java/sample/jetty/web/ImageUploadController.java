package sample.jetty.web;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ImageUploadController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@RequestMapping("/uploadImage")
	@ResponseBody
	public Map<String, Object> uploadImage(@RequestParam(value="logId", required=false, defaultValue="0") long logId, HttpServletRequest request, Model model) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		Enumeration<String> parameterEnum = request.getParameterNames();
		String param = "";
		while(parameterEnum.hasMoreElements()) {
			param = parameterEnum.nextElement();
			System.out.println(param+"\t"+request.getParameter(param));
		}
		
		if (0L != logId) {
			try {
				//tEditorMdService.deleteLogById(logId);
				
				
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
