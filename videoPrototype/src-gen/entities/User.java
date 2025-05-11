package entities;

import services.impl.StandardOPs;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDate;
import java.io.Serializable;
import java.lang.reflect.Method;

public class User implements Serializable {
	
	/* all primary attributes */
	private int UserId;
	
	/* all references */
	private VideoComponent Usertocomponent; 
	
	/* all get and set functions */
	public int getUserId() {
		return UserId;
	}	
	
	public void setUserId(int userid) {
		this.UserId = userid;
	}
	
	/* all functions for reference*/
	public VideoComponent getUsertocomponent() {
		return Usertocomponent;
	}	
	
	public void setUsertocomponent(VideoComponent videocomponent) {
		this.Usertocomponent = videocomponent;
	}			
	


}
