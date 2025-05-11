package entities;

import services.impl.StandardOPs;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDate;
import java.io.Serializable;
import java.lang.reflect.Method;

public class UpdateVideo implements Serializable {
	
	/* all primary attributes */
	private String VideoInfo;
	
	/* all references */
	
	/* all get and set functions */
	public String getVideoInfo() {
		return VideoInfo;
	}	
	
	public void setVideoInfo(String videoinfo) {
		this.VideoInfo = videoinfo;
	}
	
	/* all functions for reference*/
	


}
