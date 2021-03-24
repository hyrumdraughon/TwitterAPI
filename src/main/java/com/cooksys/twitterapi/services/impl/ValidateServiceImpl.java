package com.cooksys.twitterapi.services.impl;

import org.springframework.stereotype.Service;

import com.cooksys.twitterapi.repositories.HashtagRepository;
import com.cooksys.twitterapi.repositories.UserRepository;
import com.cooksys.twitterapi.services.ValidateService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ValidateServiceImpl implements ValidateService {
	private HashtagRepository hR;
	private UserRepository uR;
	
	@Override
	public boolean validateHashtagExist(String hashtag) {
		return hR.findByLabel(hashtag).isPresent();
	}

	@Override
	public boolean validateUsernameAvailable(String username) {
		return uR.findByCredentialsUsername(username).isPresent();
	}

	@Override
	public boolean validateUserExist(String username) {
		return !uR.findByCredentialsUsername(username).isPresent();

	}

}
