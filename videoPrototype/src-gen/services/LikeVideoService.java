package services;

import entities.*;  
import java.util.List;
import java.time.LocalDate;


public interface LikeVideoService {

	/* all system operations of the use case*/
	boolean likeVideo(String userId, String videoId) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean isLiked(String userId, String videoId) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	
	/* all get and set functions for temp property*/
	
	/* all get and set functions for temp property*/
	
	/* invariant checking function */
}
