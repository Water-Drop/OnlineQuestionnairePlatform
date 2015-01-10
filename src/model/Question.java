package model;

import java.util.Date;

public class Question {
	private Integer id;
	private Integer qnid;
	private String title;
	private String description;
	private Date createTime;
	private Double order;
	private Integer type;
	private Integer status;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getQnid() {
		return qnid;
	}
	public void setQnid(Integer qnid) {
		this.qnid = qnid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Double getOrder() {
		return order;
	}
	public void setOrder(Double order) {
		this.order = order;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
}
