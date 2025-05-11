package services.impl;

import services.*;
import entities.*;
import java.util.List;
import java.util.LinkedList;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.Arrays;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BooleanSupplier;
import org.apache.commons.lang3.SerializationUtils;
import java.util.Iterator;

public class VideoSystemImpl implements VideoSystem, Serializable {
	
	
	public static Map<String, List<String>> opINVRelatedEntity = new HashMap<String, List<String>>();
	
	
	ThirdPartyServices services;
			
	public VideoSystemImpl() {
		services = new ThirdPartyServicesImpl();
	}

	public void refresh() {
		LoginService loginservice_service = (LoginService) ServiceManager
				.getAllInstancesOf("LoginService").get(0);
		RegisterService registerservice_service = (RegisterService) ServiceManager
				.getAllInstancesOf("RegisterService").get(0);
		WatchVideoService watchvideoservice_service = (WatchVideoService) ServiceManager
				.getAllInstancesOf("WatchVideoService").get(0);
		LikeVideoService likevideoservice_service = (LikeVideoService) ServiceManager
				.getAllInstancesOf("LikeVideoService").get(0);
		CommentVideoService commentvideoservice_service = (CommentVideoService) ServiceManager
				.getAllInstancesOf("CommentVideoService").get(0);
		ManageVideoService managevideoservice_service = (ManageVideoService) ServiceManager
				.getAllInstancesOf("ManageVideoService").get(0);
		UploadVideoService uploadvideoservice_service = (UploadVideoService) ServiceManager
				.getAllInstancesOf("UploadVideoService").get(0);
		UpdateVideoService updatevideoservice_service = (UpdateVideoService) ServiceManager
				.getAllInstancesOf("UpdateVideoService").get(0);
	}			
	
	/* Generate buiness logic according to functional requirement */
	
	
	
	/* temp property for controller */
			
	/* all get and set functions for temp property*/
	
	/* invarints checking*/
	public final static ArrayList<String> allInvariantCheckingFunction = new ArrayList<String>(Arrays.asList());
			
}
