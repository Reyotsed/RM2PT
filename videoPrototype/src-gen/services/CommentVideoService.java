package services;

import entities.*;  
import java.util.List;
import java.time.LocalDate;


public interface CommentVideoService {

	/* all system operations of the use case*/
	boolean commentVideo(String userId, String videoId, String content) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	
	/* all get and set functions for temp property*/
	
	/* all get and set functions for temp property*/
	
	/* invariant checking function */
}
