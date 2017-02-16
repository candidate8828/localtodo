package sample.jetty.domain;

import java.io.Serializable;
import java.util.Date;

public class BaseBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public BaseBean() {
	}
	
	private long id; // int primary key auto_increment,
	private Date createDt; // create_dt timestamp,
	private long createdBy; // created_by int,
	private Date lastUpdDt; // last_upd_dt timestamp,
	private long lastUpdedBy; // last_upded_by int,

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getCreateDt() {
		return createDt;
	}

	public void setCreateDt(Date createDt) {
		this.createDt = createDt;
	}

	public long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(long createdBy) {
		this.createdBy = createdBy;
	}

	public Date getLastUpdDt() {
		return lastUpdDt;
	}

	public void setLastUpdDt(Date lastUpdDt) {
		this.lastUpdDt = lastUpdDt;
	}

	public long getLastUpdedBy() {
		return lastUpdedBy;
	}

	public void setLastUpdedBy(long lastUpdedBy) {
		this.lastUpdedBy = lastUpdedBy;
	}

}
