package services;

import entities.*;  
import java.util.List;
import java.time.LocalDate;


public interface UpdateVideoService {

	/* all system operations of the use case*/
	boolean getVideoInfo(String videoId) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean updateVideo(String content, String videoId) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	
	/* all get and set functions for temp property*/
	
	/* all get and set functions for temp property*/
	
	/* invariant checking function */
}
