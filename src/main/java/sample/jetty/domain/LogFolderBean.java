package sample.jetty.domain;

public class LogFolderBean extends BaseBean {
	private static final long serialVersionUID = 1L;

	private long logId;
	private long folderId;

	public long getLogId() {
		return logId;
	}

	public void setLogId(long logId) {
		this.logId = logId;
	}

	public long getFolderId() {
		return folderId;
	}

	public void setFolderId(long folderId) {
		this.folderId = folderId;
	}

}
