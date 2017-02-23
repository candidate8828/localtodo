package sample.jetty.domain;

import java.io.Serializable;
import java.util.Date;

public class LogBean extends BaseBean implements Serializable {

	private static final long serialVersionUID = 1L;

	public LogBean() {
	}

	private Date startDt; // START_DT timestamp,
	private Date endDt; // END_DT timestamp,
	private Date deadline; // DEADLINE timestamp,
	private int logType = 0; // LOG_TYPE int
	private int logStat = 0; // LOG_STAT int
	private int isDelete = 0; // is_delete int default '0', -- 0:未删除,1:已经删除,在 TB_LOG 中有对应记录的不能删
	private String logTitle = ""; // folder_name varchar(500), -- 标签名 LOG_TITLE
	private String logContent = "";
	private String logDesc = "";

	public Date getStartDt() {
		return startDt;
	}

	public void setStartDt(Date startDt) {
		this.startDt = startDt;
	}

	public Date getEndDt() {
		return endDt;
	}

	public void setEndDt(Date endDt) {
		this.endDt = endDt;
	}

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	public int getLogType() {
		return logType;
	}

	public void setLogType(int logType) {
		this.logType = logType;
	}

	public int getLogStat() {
		return logStat;
	}

	public void setLogStat(int logStat) {
		this.logStat = logStat;
	}

	public int getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}

	public String getLogTitle() {
		return logTitle;
	}

	public void setLogTitle(String logTitle) {
		this.logTitle = logTitle;
	}

	public String getLogContent() {
		return logContent;
	}

	public void setLogContent(String logContent) {
		this.logContent = logContent;
	}

	public String getLogDesc() {
		return logDesc;
	}

	public void setLogDesc(String logDesc) {
		this.logDesc = logDesc;
	}

}
