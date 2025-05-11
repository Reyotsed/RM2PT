package services;

import entities.*;  
import java.util.List;
import java.time.LocalDate;


public interface WatchVideoService {

	/* all system operations of the use case*/
	boolean getVideoList() throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean getLowVideoList() throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	WatchVideo getLikedVideoList(String userId) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	
	/* all get and set functions for temp property*/
	boolean getPasswordValidated();
	void setPasswordValidated(boolean passwordvalidated);
	String getUserId();
	void setUserId(String userid);
	
	/* all get and set functions for temp property*/
	
	/* invariant checking function */
}
