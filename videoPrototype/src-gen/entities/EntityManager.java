package entities;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.lang.reflect.Method;
import java.util.Map;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.File;

public class EntityManager {

	private static Map<String, List> AllInstance = new HashMap<String, List>();
	
	private static List<User> UserInstances = new LinkedList<User>();
	private static List<WatchVideo> WatchVideoInstances = new LinkedList<WatchVideo>();
	private static List<CommentVideo> CommentVideoInstances = new LinkedList<CommentVideo>();
	private static List<VideoComponent> VideoComponentInstances = new LinkedList<VideoComponent>();
	private static List<LikeVideo> LikeVideoInstances = new LinkedList<LikeVideo>();
	private static List<Viewer> ViewerInstances = new LinkedList<Viewer>();
	private static List<Poster> PosterInstances = new LinkedList<Poster>();
	private static List<UploadVideo> UploadVideoInstances = new LinkedList<UploadVideo>();
	private static List<UpdateVideo> UpdateVideoInstances = new LinkedList<UpdateVideo>();

	
	/* Put instances list into Map */
	static {
		AllInstance.put("User", UserInstances);
		AllInstance.put("WatchVideo", WatchVideoInstances);
		AllInstance.put("CommentVideo", CommentVideoInstances);
		AllInstance.put("VideoComponent", VideoComponentInstances);
		AllInstance.put("LikeVideo", LikeVideoInstances);
		AllInstance.put("Viewer", ViewerInstances);
		AllInstance.put("Poster", PosterInstances);
		AllInstance.put("UploadVideo", UploadVideoInstances);
		AllInstance.put("UpdateVideo", UpdateVideoInstances);
	} 
		
	/* Save State */
	public static void save(File file) {
		
		try {
			
			ObjectOutputStream stateSave = new ObjectOutputStream(new FileOutputStream(file));
			
			stateSave.writeObject(UserInstances);
			stateSave.writeObject(WatchVideoInstances);
			stateSave.writeObject(CommentVideoInstances);
			stateSave.writeObject(VideoComponentInstances);
			stateSave.writeObject(LikeVideoInstances);
			stateSave.writeObject(ViewerInstances);
			stateSave.writeObject(PosterInstances);
			stateSave.writeObject(UploadVideoInstances);
			stateSave.writeObject(UpdateVideoInstances);
			
			stateSave.close();
					
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/* Load State */
	public static void load(File file) {
		
		try {
			
			ObjectInputStream stateLoad = new ObjectInputStream(new FileInputStream(file));
			
			try {
				
				UserInstances =  (List<User>) stateLoad.readObject();
				AllInstance.put("User", UserInstances);
				WatchVideoInstances =  (List<WatchVideo>) stateLoad.readObject();
				AllInstance.put("WatchVideo", WatchVideoInstances);
				CommentVideoInstances =  (List<CommentVideo>) stateLoad.readObject();
				AllInstance.put("CommentVideo", CommentVideoInstances);
				VideoComponentInstances =  (List<VideoComponent>) stateLoad.readObject();
				AllInstance.put("VideoComponent", VideoComponentInstances);
				LikeVideoInstances =  (List<LikeVideo>) stateLoad.readObject();
				AllInstance.put("LikeVideo", LikeVideoInstances);
				ViewerInstances =  (List<Viewer>) stateLoad.readObject();
				AllInstance.put("Viewer", ViewerInstances);
				PosterInstances =  (List<Poster>) stateLoad.readObject();
				AllInstance.put("Poster", PosterInstances);
				UploadVideoInstances =  (List<UploadVideo>) stateLoad.readObject();
				AllInstance.put("UploadVideo", UploadVideoInstances);
				UpdateVideoInstances =  (List<UpdateVideo>) stateLoad.readObject();
				AllInstance.put("UpdateVideo", UpdateVideoInstances);
				
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
		
	/* create object */  
	public static Object createObject(String Classifer) {
		try
		{
			Class c = Class.forName("entities.EntityManager");
			Method createObjectMethod = c.getDeclaredMethod("create" + Classifer + "Object");
			return createObjectMethod.invoke(c);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	/* add object */  
	public static Object addObject(String Classifer, Object ob) {
		try
		{
			Class c = Class.forName("entities.EntityManager");
			Method addObjectMethod = c.getDeclaredMethod("add" + Classifer + "Object", Class.forName("entities." + Classifer));
			return  (boolean) addObjectMethod.invoke(c, ob);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}	
	
	/* add objects */  
	public static Object addObjects(String Classifer, List obs) {
		try
		{
			Class c = Class.forName("entities.EntityManager");
			Method addObjectsMethod = c.getDeclaredMethod("add" + Classifer + "Objects", Class.forName("java.util.List"));
			return  (boolean) addObjectsMethod.invoke(c, obs);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	/* Release object */
	public static boolean deleteObject(String Classifer, Object ob) {
		try
		{
			Class c = Class.forName("entities.EntityManager");
			Method deleteObjectMethod = c.getDeclaredMethod("delete" + Classifer + "Object", Class.forName("entities." + Classifer));
			return  (boolean) deleteObjectMethod.invoke(c, ob);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	/* Release objects */
	public static boolean deleteObjects(String Classifer, List obs) {
		try
		{
			Class c = Class.forName("entities.EntityManager");
			Method deleteObjectMethod = c.getDeclaredMethod("delete" + Classifer + "Objects", Class.forName("java.util.List"));
			return  (boolean) deleteObjectMethod.invoke(c, obs);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}		 	
	
	 /* Get all objects belongs to same class */
	public static List getAllInstancesOf(String ClassName) {
			 return AllInstance.get(ClassName);
	}	

   /* Sub-create object */
	public static User createUserObject() {
		User o = new User();
		return o;
	}
	
	public static boolean addUserObject(User o) {
		return UserInstances.add(o);
	}
	
	public static boolean addUserObjects(List<User> os) {
		return UserInstances.addAll(os);
	}
	
	public static boolean deleteUserObject(User o) {
		return UserInstances.remove(o);
	}
	
	public static boolean deleteUserObjects(List<User> os) {
		return UserInstances.removeAll(os);
	}
	public static WatchVideo createWatchVideoObject() {
		WatchVideo o = new WatchVideo();
		return o;
	}
	
	public static boolean addWatchVideoObject(WatchVideo o) {
		return WatchVideoInstances.add(o);
	}
	
	public static boolean addWatchVideoObjects(List<WatchVideo> os) {
		return WatchVideoInstances.addAll(os);
	}
	
	public static boolean deleteWatchVideoObject(WatchVideo o) {
		return WatchVideoInstances.remove(o);
	}
	
	public static boolean deleteWatchVideoObjects(List<WatchVideo> os) {
		return WatchVideoInstances.removeAll(os);
	}
	public static CommentVideo createCommentVideoObject() {
		CommentVideo o = new CommentVideo();
		return o;
	}
	
	public static boolean addCommentVideoObject(CommentVideo o) {
		return CommentVideoInstances.add(o);
	}
	
	public static boolean addCommentVideoObjects(List<CommentVideo> os) {
		return CommentVideoInstances.addAll(os);
	}
	
	public static boolean deleteCommentVideoObject(CommentVideo o) {
		return CommentVideoInstances.remove(o);
	}
	
	public static boolean deleteCommentVideoObjects(List<CommentVideo> os) {
		return CommentVideoInstances.removeAll(os);
	}
	public static VideoComponent createVideoComponentObject() {
		VideoComponent o = new VideoComponent();
		return o;
	}
	
	public static boolean addVideoComponentObject(VideoComponent o) {
		return VideoComponentInstances.add(o);
	}
	
	public static boolean addVideoComponentObjects(List<VideoComponent> os) {
		return VideoComponentInstances.addAll(os);
	}
	
	public static boolean deleteVideoComponentObject(VideoComponent o) {
		return VideoComponentInstances.remove(o);
	}
	
	public static boolean deleteVideoComponentObjects(List<VideoComponent> os) {
		return VideoComponentInstances.removeAll(os);
	}
	public static LikeVideo createLikeVideoObject() {
		LikeVideo o = new LikeVideo();
		return o;
	}
	
	public static boolean addLikeVideoObject(LikeVideo o) {
		return LikeVideoInstances.add(o);
	}
	
	public static boolean addLikeVideoObjects(List<LikeVideo> os) {
		return LikeVideoInstances.addAll(os);
	}
	
	public static boolean deleteLikeVideoObject(LikeVideo o) {
		return LikeVideoInstances.remove(o);
	}
	
	public static boolean deleteLikeVideoObjects(List<LikeVideo> os) {
		return LikeVideoInstances.removeAll(os);
	}
	public static Viewer createViewerObject() {
		Viewer o = new Viewer();
		return o;
	}
	
	public static boolean addViewerObject(Viewer o) {
		return ViewerInstances.add(o);
	}
	
	public static boolean addViewerObjects(List<Viewer> os) {
		return ViewerInstances.addAll(os);
	}
	
	public static boolean deleteViewerObject(Viewer o) {
		return ViewerInstances.remove(o);
	}
	
	public static boolean deleteViewerObjects(List<Viewer> os) {
		return ViewerInstances.removeAll(os);
	}
	public static Poster createPosterObject() {
		Poster o = new Poster();
		return o;
	}
	
	public static boolean addPosterObject(Poster o) {
		return PosterInstances.add(o);
	}
	
	public static boolean addPosterObjects(List<Poster> os) {
		return PosterInstances.addAll(os);
	}
	
	public static boolean deletePosterObject(Poster o) {
		return PosterInstances.remove(o);
	}
	
	public static boolean deletePosterObjects(List<Poster> os) {
		return PosterInstances.removeAll(os);
	}
	public static UploadVideo createUploadVideoObject() {
		UploadVideo o = new UploadVideo();
		return o;
	}
	
	public static boolean addUploadVideoObject(UploadVideo o) {
		return UploadVideoInstances.add(o);
	}
	
	public static boolean addUploadVideoObjects(List<UploadVideo> os) {
		return UploadVideoInstances.addAll(os);
	}
	
	public static boolean deleteUploadVideoObject(UploadVideo o) {
		return UploadVideoInstances.remove(o);
	}
	
	public static boolean deleteUploadVideoObjects(List<UploadVideo> os) {
		return UploadVideoInstances.removeAll(os);
	}
	public static UpdateVideo createUpdateVideoObject() {
		UpdateVideo o = new UpdateVideo();
		return o;
	}
	
	public static boolean addUpdateVideoObject(UpdateVideo o) {
		return UpdateVideoInstances.add(o);
	}
	
	public static boolean addUpdateVideoObjects(List<UpdateVideo> os) {
		return UpdateVideoInstances.addAll(os);
	}
	
	public static boolean deleteUpdateVideoObject(UpdateVideo o) {
		return UpdateVideoInstances.remove(o);
	}
	
	public static boolean deleteUpdateVideoObjects(List<UpdateVideo> os) {
		return UpdateVideoInstances.removeAll(os);
	}
  
}

