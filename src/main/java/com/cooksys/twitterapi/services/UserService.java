package com.cooksys.twitterapi.services;

import java.util.List;

import com.cooksys.twitterapi.models.CreateUserDto;
import com.cooksys.twitterapi.models.CredentialsDto;
import com.cooksys.twitterapi.models.TweetDto;
import com.cooksys.twitterapi.models.UserDto;

public interface UserService {

	List<UserDto> getAllUsers();

	UserDto getUser(String username);

	UserDto updateUserProfile(String username, CreateUserDto createUserDto);

	UserDto createUser(CreateUserDto createUserDto);
	UserDto deleteUser(String username, CredentialsDto credentialsDto);

	void createFollow(String username, CredentialsDto credentialsDto);

	void unfollow(String username, CredentialsDto credentialsDto);

	List<UserDto> getFollowers(String username);

	List<UserDto> getFollowing(String username);

	List<TweetDto> getTweets(String username);

    List<TweetDto> getFeed(String username);

    List<TweetDto> getMentions(String username);

}
