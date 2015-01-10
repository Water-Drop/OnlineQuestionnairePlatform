package model;

import java.util.Date;

public class FillRecord {
	private Integer id;
	private Integer uid;
	private Integer qnid;
	private Date fillTime;
	private Integer status;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public Integer getQnid() {
		return qnid;
	}
	public void setQnid(Integer qnid) {
		this.qnid = qnid;
	}
	public Date getFillTime() {
		return fillTime;
	}
	public void setFillTime(Date fillTime) {
		this.fillTime = fillTime;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
}
