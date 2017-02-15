package sample.jetty.domain;

import java.io.Serializable;

public class FolderBean extends BaseBean implements Serializable {

	private static final long serialVersionUID = 1L;

	public FolderBean() {
	}

	private String folderName; // folder_name varchar(500), -- 标签名
	private int isDelete = 0; // is_delete int default '0', -- 0:未删除,1:已经删除,在TB_LOG_FOLDER 中有对应记录的不能删
	private long parentId = 0; // parent_id int default '0', -- 默认为顶级目录
	private int orderBy = 0; // order_by int -- 顺序

	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	public int getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public int getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(int orderBy) {
		this.orderBy = orderBy;
	}

}
