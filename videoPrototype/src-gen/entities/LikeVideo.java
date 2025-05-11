package entities;

import services.impl.StandardOPs;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDate;
import java.io.Serializable;
import java.lang.reflect.Method;

public class LikeVideo implements Serializable {
	
	/* all primary attributes */
	private String UserId;
	private String VideoId;
	
	/* all references */
	
	/* all get and set functions */
	public String getUserId() {
		return UserId;
	}	
	
	public void setUserId(String userid) {
		this.UserId = userid;
	}
	public String getVideoId() {
		return VideoId;
	}	
	
	public void setVideoId(String videoid) {
		this.VideoId = videoid;
	}
	
	/* all functions for reference*/
	


}
