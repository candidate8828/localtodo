package sample.jetty.web;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import sample.jetty.SampleJettyApplication;
import sample.jetty.domain.FileBean;
import sample.jetty.embedmysql.EmbedMySqlServer;
import sample.jetty.service.TFileService;

@Controller
public class ImageUploadController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private TFileService tFileService;
	
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
		
		
		String targetpath=null;// 文件路径
		String fileName=editormd_image_file.getOriginalFilename();// 文件原名称
		Date now = new Date();
		if (0L != logId) {
			InputStream inputStream = null;
			BufferedInputStream bufInputStream = null;
			FileOutputStream os = null;
			try {
				
				// 项目在容器中实际发布运行的根路径
				String rootPath = logFileDir.getAbsolutePath() + File.separator + "files" + File.separator;
				String filesDirStr = rootPath + logId;//String realPath=request.getSession().getServletContext().getRealPath("/");
				long createTime = now.getTime(); //  System.currentTimeMillis();
				// 自定义的文件名称
				String trueFileName=String.valueOf(createTime)+fileName;
				// 设置存放图片文件的路径
				targetpath = filesDirStr + File.separator + trueFileName;
				logger.debug("存放图片文件的路径:"+targetpath);
				// 转存文件到指定的路径
				
				//editormd_image_file.transferTo(new File(path));
				File targetFile = new File(targetpath);
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
				logger.debug("文件成功上传到指定目录下");
				
				FileBean fileBean = new FileBean();
				fileBean.setCreateTime(createTime);
				if (fileName.lastIndexOf(".") < fileName.length()) {
					fileBean.setFileType((fileName.substring(fileName.lastIndexOf(".")+1)+"").toLowerCase());
				}
				String relatePath = File.separator + "files" + File.separator + logId + File.separator + trueFileName;
				fileBean.setRelatePath(relatePath.replaceAll("\\\\", "/"));
				fileBean.setCreatedBy(1L);
				fileBean.setLastUpdedBy(1L);
				fileBean.setCreateDt(now);
				fileBean.setLastUpdDt(now);
				fileBean.setLogId(logId);
				fileBean.setFileName(trueFileName);
				
				// 路径相关信息存入表中
				tFileService.addNewFile(fileBean);
				
				logger.debug(request.getRequestURI());
				logger.debug(request.getRemoteHost());
				logger.debug(request.getRemotePort()+"");
				logger.debug(request.getRequestURL()+"");
				logger.debug(request.getServerName()+":"+request.getServerPort());
				
				String url = "";
				//"jpg", "jpeg", "gif", "png", "bmp", "webp"
				if("jpg|jpeg|gif|png|bmp|webp".indexOf((fileName.substring(fileName.lastIndexOf(".")+1)+"").toLowerCase()) != -1) {
					// image
					url = "http://" + request.getServerName() + ":" + request.getServerPort() + "/getImage/" + logId + "/" + createTime;
				} else {
					// other file
					url = targetpath.replaceAll("\\\\", "/");
				}
				
				resultMap.put("success", 1);
				resultMap.put("message", "");
				resultMap.put("url", url); // 页面上显示的图片的url
			} catch(Exception e) {
				logger.error("EditorMdController.deleteLogById", e);
				resultMap.put("success", 0);
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
	
	@RequestMapping(value="/getImage/{logId}/{createTime}")
	public void getImage(HttpServletRequest request, HttpServletResponse response, HttpSession session, 
			@PathVariable String logId,@PathVariable String createTime) {
		FileBean file = null;
		try{
			// 从数据库中获取存放地址
			file = tFileService.getFileByLogIdAndCreateTime(new Long(logId), new Long(createTime));
		}catch(Exception e){
			file = new FileBean();
			logger.error("ImageUploadController.getImage", e);
		}
		EmbedMySqlServer mysqldbServer = SampleJettyApplication.MAIN_THREAD_LOCAL.get("embedMysqlServer");
		String dbPath = mysqldbServer.getEmbedMySqlHome();
		File logFileDir = new File(dbPath).getParentFile().getAbsoluteFile();
		String rootPath = logFileDir.getAbsolutePath() + File.separator + "files" + File.separator;
		String filesDirStr = rootPath + logId;
		String targetImagePath = filesDirStr + File.separator + file.getFileName();
//		File targetImageFile = new File(targetImagePath);
		FileInputStream inputStream = null;
//		BufferedInputStream bufInputStream = null;
//		int BUFFER_SIZE = 4096;
		OutputStream os = null;
		BufferedImage image = null;
		try {
			inputStream = new FileInputStream(targetImagePath);
			image = ImageIO.read(inputStream);
			os = response.getOutputStream();
			ImageIO.write(image, file.getFileType(), os);
			/*
			inputStream = new FileInputStream(targetImageFile);
			bufInputStream = new BufferedInputStream(inputStream);
			byte[] buffer = new byte[BUFFER_SIZE];
			int bytesRead = -1;
			while ((bytesRead = bufInputStream.read(buffer)) != -1) {
				os.write(buffer, 0, bytesRead);
			}*/
			os.flush();
		} catch (Exception e) {
			logger.error("ImageUploadController.getImage", e);
		} finally {
			try{if(null != os){os.close();}}catch(Exception e){}
			try{if(null != inputStream){inputStream.close();}}catch(Exception e){}
		}
	}
}
