package sample.jetty.web;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import sample.jetty.SampleJettyApplication;
import sample.jetty.embedmysql.EmbedMySqlServer;

@Controller
public class ImageUploadController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@RequestMapping(value="/uploadImage",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> uploadImage(@RequestParam(value="logId", required=false, defaultValue="0") long logId, MultipartFile editormd_image_file, HttpServletRequest request, Model model) {
		int BUFFER_SIZE = 4096;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		Enumeration<String> parameterEnum = request.getParameterNames();
		String param = "";
		while(parameterEnum.hasMoreElements()) {
			param = parameterEnum.nextElement();
			System.out.println(param+"\t"+request.getParameter(param));
		}
		
		EmbedMySqlServer mysqldbServer = SampleJettyApplication.MAIN_THREAD_LOCAL.get("embedMysqlServer");
		String dbPath = mysqldbServer.getEmbedMySqlHome();
		File logFileDir = new File(dbPath).getParentFile().getAbsoluteFile();
		//String filesDirStr = logFileDir.getAbsolutePath() + File.separator + "files" + File.separator + logId;
		
		
		String path=null;// 文件路径
		String fileName=editormd_image_file.getOriginalFilename();// 文件原名称
		if (0L != logId) {
			InputStream inputStream = null;
			BufferedInputStream bufInputStream = null;
			FileOutputStream os = null;
			try {
				// TODO 路径相关信息存入表中
				// 项目在容器中实际发布运行的根路径
				String filesDirStr = logFileDir.getAbsolutePath() + File.separator + "files" + File.separator + logId;//String realPath=request.getSession().getServletContext().getRealPath("/");
				// 自定义的文件名称
				String trueFileName=String.valueOf(System.currentTimeMillis())+fileName;
				// 设置存放图片文件的路径
				path = filesDirStr + File.separator + trueFileName;
				System.out.println("存放图片文件的路径:"+path);
				// 转存文件到指定的路径
				
				//editormd_image_file.transferTo(new File(path));
				File targetFile = new File(path);
				os = new FileOutputStream(targetFile);
				inputStream = editormd_image_file.getInputStream();
				bufInputStream = new BufferedInputStream(inputStream);
				byte[] buffer = new byte[BUFFER_SIZE];
				int bytesRead = -1;
				while ((bytesRead = bufInputStream.read(buffer)) != -1) {
					os.write(buffer, 0, bytesRead);
				}
				os.flush();
				
				if (os != null) {
					os.close();
				}
				System.out.println("文件成功上传到指定目录下");
				
				
				resultMap.put("success", "1");
				resultMap.put("message", "");
				resultMap.put("url", ""); // TODO 页面上显示的图片的url
			} catch(Exception e) {
				logger.error("EditorMdController.deleteLogById", e);
				resultMap.put("success", "0");
				resultMap.put("message", e.getMessage());
			} finally {
				try {
					if (null != bufInputStream) {
						bufInputStream.close();
					}
				} catch (Exception e) {
				}
				try {
					if (null != inputStream) {
						inputStream.close();
					}
				} catch (Exception e) {
				}
				try {
					if (null != os) {
						os.close();
					}
				} catch (Exception e) {
				}
			}
		} else {
			resultMap.put("success", "0");
			resultMap.put("message", "logId is null");
		}
		return resultMap;
	}
}
