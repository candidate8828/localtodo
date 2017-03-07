package sample.jetty.domain;

import java.io.Serializable;

public class FileBean extends BaseBean implements Serializable {

	private static final long serialVersionUID = 1L;

	public FileBean(){
		
	}
	
	private long createTime;
	private String relatePath;
	private String fileType = "file"; // jpg doc ...

	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public String getRelatePath() {
		return relatePath;
	}
	public void setRelatePath(String relatePath) {
		this.relatePath = relatePath;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	
	
}
