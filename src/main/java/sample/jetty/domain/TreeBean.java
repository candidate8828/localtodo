package sample.jetty.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class TreeBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public TreeBean() {
	}

	private long id;
	private String text;
	private String state = "closed";
	private boolean checked = false;
	private HashMap<String, Object> attributes = new HashMap<String, Object>();
	private ArrayList<TreeBean> children = new ArrayList<TreeBean>();

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public HashMap<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(HashMap<String, Object> attributes) {
		this.attributes = attributes;
	}

	public ArrayList<TreeBean> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<TreeBean> children) {
		this.children = children;
	}

}
