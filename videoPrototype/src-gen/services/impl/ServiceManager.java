package services.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import services.*;
	
public class ServiceManager {
	
	private static Map<String, List> AllServiceInstance = new HashMap<String, List>();
	
	private static List<VideoSystem> VideoSystemInstances = new LinkedList<VideoSystem>();
	private static List<ThirdPartyServices> ThirdPartyServicesInstances = new LinkedList<ThirdPartyServices>();
	private static List<LoginService> LoginServiceInstances = new LinkedList<LoginService>();
	private static List<RegisterService> RegisterServiceInstances = new LinkedList<RegisterService>();
	private static List<WatchVideoService> WatchVideoServiceInstances = new LinkedList<WatchVideoService>();
	private static List<LikeVideoService> LikeVideoServiceInstances = new LinkedList<LikeVideoService>();
	private static List<CommentVideoService> CommentVideoServiceInstances = new LinkedList<CommentVideoService>();
	private static List<ManageVideoService> ManageVideoServiceInstances = new LinkedList<ManageVideoService>();
	private static List<UploadVideoService> UploadVideoServiceInstances = new LinkedList<UploadVideoService>();
	private static List<UpdateVideoService> UpdateVideoServiceInstances = new LinkedList<UpdateVideoService>();
	
	static {
		AllServiceInstance.put("VideoSystem", VideoSystemInstances);
		AllServiceInstance.put("ThirdPartyServices", ThirdPartyServicesInstances);
		AllServiceInstance.put("LoginService", LoginServiceInstances);
		AllServiceInstance.put("RegisterService", RegisterServiceInstances);
		AllServiceInstance.put("WatchVideoService", WatchVideoServiceInstances);
		AllServiceInstance.put("LikeVideoService", LikeVideoServiceInstances);
		AllServiceInstance.put("CommentVideoService", CommentVideoServiceInstances);
		AllServiceInstance.put("ManageVideoService", ManageVideoServiceInstances);
		AllServiceInstance.put("UploadVideoService", UploadVideoServiceInstances);
		AllServiceInstance.put("UpdateVideoService", UpdateVideoServiceInstances);
	} 
	
	public static List getAllInstancesOf(String ClassName) {
			 return AllServiceInstance.get(ClassName);
	}	
	
	public static VideoSystem createVideoSystem() {
		VideoSystem s = new VideoSystemImpl();
		VideoSystemInstances.add(s);
		return s;
	}
	public static ThirdPartyServices createThirdPartyServices() {
		ThirdPartyServices s = new ThirdPartyServicesImpl();
		ThirdPartyServicesInstances.add(s);
		return s;
	}
	public static LoginService createLoginService() {
		LoginService s = new LoginServiceImpl();
		LoginServiceInstances.add(s);
		return s;
	}
	public static RegisterService createRegisterService() {
		RegisterService s = new RegisterServiceImpl();
		RegisterServiceInstances.add(s);
		return s;
	}
	public static WatchVideoService createWatchVideoService() {
		WatchVideoService s = new WatchVideoServiceImpl();
		WatchVideoServiceInstances.add(s);
		return s;
	}
	public static LikeVideoService createLikeVideoService() {
		LikeVideoService s = new LikeVideoServiceImpl();
		LikeVideoServiceInstances.add(s);
		return s;
	}
	public static CommentVideoService createCommentVideoService() {
		CommentVideoService s = new CommentVideoServiceImpl();
		CommentVideoServiceInstances.add(s);
		return s;
	}
	public static ManageVideoService createManageVideoService() {
		ManageVideoService s = new ManageVideoServiceImpl();
		ManageVideoServiceInstances.add(s);
		return s;
	}
	public static UploadVideoService createUploadVideoService() {
		UploadVideoService s = new UploadVideoServiceImpl();
		UploadVideoServiceInstances.add(s);
		return s;
	}
	public static UpdateVideoService createUpdateVideoService() {
		UpdateVideoService s = new UpdateVideoServiceImpl();
		UpdateVideoServiceInstances.add(s);
		return s;
	}
}	
