package services;

import entities.*;  
import java.util.List;
import java.time.LocalDate;


public interface WatchVideoService {

	/* all system operations of the use case*/
	boolean getVideoList() throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean getLowVideoList() throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean getLikedVideoList(String userId) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	
	/* all get and set functions for temp property*/
	
	/* all get and set functions for temp property*/
	
	/* invariant checking function */
}
