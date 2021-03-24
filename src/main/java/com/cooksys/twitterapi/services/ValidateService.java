package com.cooksys.twitterapi.services;

public interface ValidateService {

	boolean validateHashtagExist(String hashtag);

	boolean validateUsernameAvailable(String username);

	boolean validateUserExist(String username);

}
