package sample.jetty.domain;

public class UserBean extends BaseBean {

	private static final long serialVersionUID = 1L;

	public UserBean() {
	}
	
	private String username; // username varchar(20), -- 登录名
	private String password; // password varchar(20), -- 密码
	private String dispname; // dispname varchar(50), -- 页面显示名
	private int isDelete = 0; // is_delete int default '0', -- 0:未删除,1:已经删除
	private String comments; // comments varchar(2000) -- 注释

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDispname() {
		return dispname;
	}

	public void setDispname(String dispname) {
		this.dispname = dispname;
	}

	public int getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

}
