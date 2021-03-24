package com.cooksys.twitterapi.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cooksys.twitterapi.entities.Tweet;
import com.cooksys.twitterapi.entities.User;
import com.cooksys.twitterapi.exceptions.BadRequestException;
import com.cooksys.twitterapi.exceptions.NotAuthorizedException;
import com.cooksys.twitterapi.exceptions.NotFoundException;
import com.cooksys.twitterapi.mappers.CredentialsMapper;
import com.cooksys.twitterapi.mappers.ProfileMapper;
import com.cooksys.twitterapi.mappers.TweetMapper;
import com.cooksys.twitterapi.mappers.UserMapper;
import com.cooksys.twitterapi.models.CreateUserDto;
import com.cooksys.twitterapi.models.CredentialsDto;
import com.cooksys.twitterapi.models.TweetDto;
import com.cooksys.twitterapi.models.UserDto;
import com.cooksys.twitterapi.repositories.UserRepository;
import com.cooksys.twitterapi.services.UserService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

	private UserRepository userRepository;
	private UserMapper userMapper;
	private ProfileMapper profileMapper;
	private TweetMapper tweetMapper;
	private CredentialsMapper credMapper;

	private Optional<User> validateUserExist(String username) {
		Optional<User> optionalUser = userRepository.findByCredentialsUsername(username);
		if (optionalUser.isEmpty()) {
			throw new NotFoundException("This user doesnt exist.");
		} else if (optionalUser.get().isDeleted()) {
			throw new NotFoundException("The user has been deleted.");
		}
		return optionalUser;

	}

	// method to validate credentials in endpoint methods
	public void validateCredentials(String username, CredentialsDto credentialsDto) {
		Optional<User> optionalUser = userRepository.findByCredentialsUsername(username);

		if (!credentialsDto.getUsername().equals(username)) {
			throw new NotAuthorizedException("incorrect username for user " + username);
		} else if (!credentialsDto.getPassword().equals(optionalUser.get().getCredentials().getPassword())) {
			throw new NotAuthorizedException("incorrect password for user " + username);
		}
	}

	// method to check if user exists and is not deleted
	public void checkExistsNotDeleted(String username, Optional<User> optionalUser) {
		if (optionalUser.isEmpty()) {
			throw new NotFoundException("the user " + username + " does not exist");
		} else if (optionalUser.get().isDeleted()) {
			throw new NotFoundException("the user " + username + " has been deleted");
		}

	}

	@Override
	public List<UserDto> getAllUsers() {
		List<UserDto> activeUsers = new ArrayList<>();
		for (User u : userRepository.findAll()) {
			if (!u.isDeleted()) {
				activeUsers.add(userMapper.entityToDto(u));
			}
		}
		return activeUsers;
	}

	@Override
	public UserDto getUser(String username) {
		if (username.isEmpty()) {
			throw new BadRequestException("must provide a username");
		}
		Optional<User> optionalUser = userRepository.findByCredentialsUsername(username);
		checkExistsNotDeleted(username, optionalUser);

		return userMapper.entityToDto(optionalUser.get());
	}

	@Override
	public UserDto updateUserProfile(String username, CreateUserDto createUserDto) {
		if (username.isEmpty()) {
			throw new BadRequestException("must provide a username");
		}
		// check user exists and is not deleted
		Optional<User> optionalUser = userRepository.findByCredentialsUsername(username);
		checkExistsNotDeleted(username, optionalUser);

		// check for correct credentials
		validateCredentials(username, createUserDto.getCredentials());

		// update user's profile, flush, and return UserDto
		optionalUser.get().setProfile(profileMapper.dtoToEntity(createUserDto.getProfile()));
		userRepository.flush();
		return userMapper.entityToDto(optionalUser.get());
	}

	@Override
	public UserDto deleteUser(String username, CredentialsDto credentialsDto) {
		if (username.isEmpty()) {
			throw new BadRequestException("must provide a username");
		}

		// check user exists and is not deleted
		Optional<User> optionalUser = userRepository.findByCredentialsUsername(username);
		if (optionalUser.isEmpty()) {
			throw new NotFoundException("the user " + username + " does not exist");
		} else if (optionalUser.get().isDeleted()) {
			throw new NotFoundException("the user " + username + " has already been deleted");
		}

		// check for correct credentials
		validateCredentials(username, credentialsDto);

		optionalUser.get().setDeleted(true);
		userRepository.flush();
		return userMapper.entityToDto(optionalUser.get());
	}

	@Override
	public void createFollow(String username, CredentialsDto credentialsDto) {
		if (username.isEmpty()) {
			throw new BadRequestException("must provide a username");
		}

		// check followed exists and is not deleted
		Optional<User> optionalFollowed = userRepository.findByCredentialsUsername(username);
		checkExistsNotDeleted(username, optionalFollowed);

		// check follower exists and is not deleted
		Optional<User> optionalFollower = userRepository.findByCredentialsUsername(credentialsDto.getUsername());
		checkExistsNotDeleted(credentialsDto.getUsername(), optionalFollower);

		// check for correct credentials
		validateCredentials(credentialsDto.getUsername(), credentialsDto);

		// check if follow relationship already exists
		if (optionalFollower.get().getFollowing().contains(optionalFollowed.get())) {
			throw new BadRequestException(
					"the user " + credentialsDto.getUsername() + " already follows the user " + username);
		}

		optionalFollower.get().getFollowing().add(optionalFollowed.get());
		optionalFollowed.get().getFollowers().add(optionalFollower.get());
		userRepository.flush();
	}

	@Override
	public void unfollow(String username, CredentialsDto credentialsDto) {
		if (username.isEmpty()) {
			throw new BadRequestException("must provide a username");
		}

		// check unfollowed exists and is not deleted
		Optional<User> optionalUnfollowed = userRepository.findByCredentialsUsername(username);
		checkExistsNotDeleted(username, optionalUnfollowed);

		// check unfollower exists and is not deleted
		Optional<User> optionalUnfollower = userRepository.findByCredentialsUsername(credentialsDto.getUsername());
		checkExistsNotDeleted(credentialsDto.getUsername(), optionalUnfollower);

		// check for correct credentials
		validateCredentials(credentialsDto.getUsername(), credentialsDto);

		// check if follow relationship does not already exist
		if (!optionalUnfollower.get().getFollowing().contains(optionalUnfollowed.get())) {
			throw new BadRequestException(
					"the user " + credentialsDto.getUsername() + " does not already follow the user " + username);
		}

		optionalUnfollower.get().getFollowing().remove(optionalUnfollowed.get());
		optionalUnfollowed.get().getFollowers().remove(optionalUnfollower.get());
		userRepository.flush();
	}

	@Override
	public List<UserDto> getFollowers(String username) {
		if (username.isEmpty()) {
			throw new BadRequestException("must provide a username");
		}

		// check user exists and is not deleted
		Optional<User> optionalUser = userRepository.findByCredentialsUsername(username);
		checkExistsNotDeleted(username, optionalUser);

		// add non-deleted followers to followers list
		List<User> activeFollowers = new ArrayList<>();
		for (User f : optionalUser.get().getFollowers()) {
			if (!f.isDeleted()) {
				activeFollowers.add(f);
			}
		}

		return userMapper.entitiesToDtos(activeFollowers);
	}

	@Override
	public List<UserDto> getFollowing(String username) {
		if (username.isEmpty()) {
			throw new BadRequestException("must provide a username");
		}

		// check user exists and is not deleted
		Optional<User> optionalUser = userRepository.findByCredentialsUsername(username);
		checkExistsNotDeleted(username, optionalUser);

		// add non-deleted followings to followings list
		List<User> activeFollowings = new ArrayList<>();
		for (User f : optionalUser.get().getFollowing()) {
			if (!f.isDeleted()) {
				activeFollowings.add(f);
			}
		}

		return userMapper.entitiesToDtos(activeFollowings);
	}

	@Override
	public List<TweetDto> getTweets(String username) {
		if (username.isEmpty()) {
			throw new BadRequestException("must provide a username");
		}

		// check user exists and is not deleted
		Optional<User> optionalUser = userRepository.findByCredentialsUsername(username);
		checkExistsNotDeleted(username, optionalUser);

		// add non-deleted tweets to tweet list
		List<Tweet> activeTweets = new ArrayList<>();
		for (Tweet t : optionalUser.get().getTweets()) {
			if (!t.isDeleted()) {
				activeTweets.add(t);
			}
		}
		activeTweets.sort((e1, e2) -> e2.getPosted().compareTo(e1.getPosted()));
		return tweetMapper.entitiesToDtoList(activeTweets);
	}

	@Override
	public UserDto createUser(CreateUserDto createUserDto) {
		// Creates a new user. If any required fields are missing or the username
		// provided is already taken, an error should be sent in lieu of a response.
		// If the given credentials match a previously-deleted user, re-activate the
		// deleted user instead of creating a new one.
		if (userRepository.equals(createUserDto.getCredentials().getUsername())) {
			User user = userRepository.findByCredentialsUsername(createUserDto.getCredentials().getUsername()).get();
			if (user.isDeleted()) {
				user.setDeleted(false);
				return userMapper.entityToDto(user);
			}
		} else if (createUserDto.getCredentials() == null || createUserDto.getProfile() == null) {
			throw new BadRequestException("You need to provide both credentials and Profile to create a user");
		} else {
			User user = new User();
			user.setCredentials(credMapper.dtoToEntity(createUserDto.getCredentials()));
			user.setProfile(profileMapper.dtoToEntity(createUserDto.getProfile()));
			return userMapper.entityToDto(userRepository.saveAndFlush(user));
		}

		throw new NullPointerException("you messed up dummy");
	}

	@Override
	public List<TweetDto> getFeed(String username) {
		if(username == null){
			throw new BadRequestException("You need to pass a username");
		}

		Optional<User> optionalUser = validateUserExist(username);

		List<Tweet> tweets = new ArrayList<>();

		for(User u : optionalUser.get().getFollowing()){
			for(Tweet t : u.getTweets()){
				if (!t.isDeleted()){
					tweets.add(t);
				}
			}
		}

		for(Tweet t : optionalUser.get().getTweets()){
			if(!t.isDeleted()){
				tweets.add(t);
			}
		}

		tweets.sort((e1, e2) -> e2.getPosted().compareTo(e1.getPosted()));
		return tweetMapper.entitiesToDtoList(tweets);

	}

	@Override
	public List<TweetDto> getMentions(String username) {
		if(username == null){
			throw new BadRequestException("You need to pass a username");
		}

		Optional<User> optionalUser = validateUserExist(username);

		List<Tweet> tweets = new ArrayList<>();

		for(Tweet t : optionalUser.get().getMentioned()){
			if(!t.isDeleted()){
				tweets.add(t);
			}
		}

		tweets.sort((e1, e2) -> e2.getPosted().compareTo(e1.getPosted()));
		return tweetMapper.entitiesToDtoList(tweets);
	};

}
