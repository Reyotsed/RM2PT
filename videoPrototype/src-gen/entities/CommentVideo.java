package entities;

import services.impl.StandardOPs;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDate;
import java.io.Serializable;
import java.lang.reflect.Method;

public class CommentVideo implements Serializable {
	
	/* all primary attributes */
	private String CommentId;
	private String Content;
	private String VideoId;
	private String UserId;
	
	/* all references */
	
	/* all get and set functions */
	public String getCommentId() {
		return CommentId;
	}	
	
	public void setCommentId(String commentid) {
		this.CommentId = commentid;
	}
	public String getContent() {
		return Content;
	}	
	
	public void setContent(String content) {
		this.Content = content;
	}
	public String getVideoId() {
		return VideoId;
	}	
	
	public void setVideoId(String videoid) {
		this.VideoId = videoid;
	}
	public String getUserId() {
		return UserId;
	}	
	
	public void setUserId(String userid) {
		this.UserId = userid;
	}
	
	/* all functions for reference*/
	


}
