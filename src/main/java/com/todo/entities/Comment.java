package com.todo.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Comment implements Serializable{
	
	@Id
	@GeneratedValue
	private Integer commnetId;
	private String comment;
	@ManyToOne
    @JoinColumn(name="itemId")
	@JsonIgnore
	private Item itemId;
	private Date commentDate;
	public Integer getCommnetId() {
		return commnetId;
	}
	public void setCommnetId(Integer commnetId) {
		this.commnetId = commnetId;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Date getCommentDate() {
		return commentDate;
	}
	public void setCommentDate(Date commentDate) {
		this.commentDate = commentDate;
	}
	public Item getItemId() {
		return itemId;
	}
	public void setItemId(Item itemId) {
		this.itemId = itemId;
	}
	
	
	
	
	
	
}