package com.cooksys.twitterapi.services;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.cooksys.twitterapi.models.ContextDto;
import com.cooksys.twitterapi.models.CreateTweetDto;
import com.cooksys.twitterapi.models.CredentialsDto;
import com.cooksys.twitterapi.models.HashtagDto;
import com.cooksys.twitterapi.models.TweetDto;
import com.cooksys.twitterapi.models.UserDto;

public interface TweetService {
	
	ResponseEntity<List<TweetDto>> getAllTweets();

	ResponseEntity<TweetDto> getSpecificTweet(Long id);

	ResponseEntity<TweetDto> deleteSpecificTweet(Long id, CredentialsDto credentials);

	void likeTweet(Long id, CredentialsDto credentials);

	ResponseEntity<List<UserDto>> getMentionedUsers(Long id);

	ResponseEntity<TweetDto> createSimpleTweet(CreateTweetDto tweet);

	ResponseEntity<TweetDto> createReplyTweet(CreateTweetDto tweet, Long id);

	ResponseEntity<TweetDto> createRepostTweet(CredentialsDto credentials, Long id);

	List<TweetDto> getReplies(Long id);

	ResponseEntity<ContextDto> getContext(Long id);

	ResponseEntity<List<HashtagDto>> getTags(Long id);

	ResponseEntity<List<UserDto>> getLikes(Long id);

	ResponseEntity<List<TweetDto>> getReposts(Long id);


}
