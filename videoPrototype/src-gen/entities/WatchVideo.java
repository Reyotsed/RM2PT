package entities;

import services.impl.StandardOPs;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDate;
import java.io.Serializable;
import java.lang.reflect.Method;

public class WatchVideo implements Serializable {
	
	/* all primary attributes */
	private String VideoId;
	private String VideoContent;
	
	/* all references */
	private UploadVideo VideotoUploadVideo; 
	private UpdateVideo VideotoUpdateVideo; 
	
	/* all get and set functions */
	public String getVideoId() {
		return VideoId;
	}	
	
	public void setVideoId(String videoid) {
		this.VideoId = videoid;
	}
	public String getVideoContent() {
		return VideoContent;
	}	
	
	public void setVideoContent(String videocontent) {
		this.VideoContent = videocontent;
	}
	
	/* all functions for reference*/
	public UploadVideo getVideotoUploadVideo() {
		return VideotoUploadVideo;
	}	
	
	public void setVideotoUploadVideo(UploadVideo uploadvideo) {
		this.VideotoUploadVideo = uploadvideo;
	}			
	public UpdateVideo getVideotoUpdateVideo() {
		return VideotoUpdateVideo;
	}	
	
	public void setVideotoUpdateVideo(UpdateVideo updatevideo) {
		this.VideotoUpdateVideo = updatevideo;
	}			
	


}
