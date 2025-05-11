package entities;

import services.impl.StandardOPs;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDate;
import java.io.Serializable;
import java.lang.reflect.Method;

public class VideoComponent implements Serializable {
	
	/* all primary attributes */
	
	/* all references */
	private WatchVideo ComponenttoVideo; 
	private CommentVideo ComponenttoComment; 
	private LikeVideo Componenttolike; 
	
	/* all get and set functions */
	
	/* all functions for reference*/
	public WatchVideo getComponenttoVideo() {
		return ComponenttoVideo;
	}	
	
	public void setComponenttoVideo(WatchVideo watchvideo) {
		this.ComponenttoVideo = watchvideo;
	}			
	public CommentVideo getComponenttoComment() {
		return ComponenttoComment;
	}	
	
	public void setComponenttoComment(CommentVideo commentvideo) {
		this.ComponenttoComment = commentvideo;
	}			
	public LikeVideo getComponenttolike() {
		return Componenttolike;
	}	
	
	public void setComponenttolike(LikeVideo likevideo) {
		this.Componenttolike = likevideo;
	}			
	


}
