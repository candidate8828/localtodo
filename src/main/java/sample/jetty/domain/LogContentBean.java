package sample.jetty.domain;

import java.io.Serializable;

public class LogContentBean extends BaseBean implements Serializable {

	private static final long serialVersionUID = 1L;

	public LogContentBean() {
	}

	private long logId; // LOG_ID" jdbcType="INTEGER"
	private int orderBy = 0; // ORDER_BY" jdbcType="INTEGER"
	private int isDelete = 0; // is_delete int default '0', -- 0:未删除,1:已经删除
	private String logContent = ""; // LOG_CONTENT" varchar 2000

	public long getLogId() {
		return logId;
	}

	public void setLogId(long logId) {
		this.logId = logId;
	}

	public int getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(int orderBy) {
		this.orderBy = orderBy;
	}

	public int getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}

	public String getLogContent() {
		return logContent;
	}

	public void setLogContent(String logContent) {
		this.logContent = logContent;
	}

}
