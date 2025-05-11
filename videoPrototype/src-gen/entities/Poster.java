package entities;

import services.impl.StandardOPs;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDate;
import java.io.Serializable;
import java.lang.reflect.Method;

public class Poster extends User  implements Serializable {
	
	/* all primary attributes */
	private String UserName;
	private String Password;
	
	/* all references */
	
	/* all get and set functions */
	public String getUserName() {
		return UserName;
	}	
	
	public void setUserName(String username) {
		this.UserName = username;
	}
	public String getPassword() {
		return Password;
	}	
	
	public void setPassword(String password) {
		this.Password = password;
	}
	
	/* all functions for reference*/
	


}
