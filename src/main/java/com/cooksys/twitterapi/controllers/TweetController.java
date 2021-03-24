package com.cooksys.twitterapi.controllers;

import com.cooksys.twitterapi.entities.Hashtag;
import com.cooksys.twitterapi.models.ContextDto;
import com.cooksys.twitterapi.models.CreateTweetDto;
import com.cooksys.twitterapi.models.CredentialsDto;
import com.cooksys.twitterapi.models.HashtagDto;
import com.cooksys.twitterapi.models.TweetDto;
import com.cooksys.twitterapi.models.UserDto;
import com.cooksys.twitterapi.services.TweetService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tweets") // This controller will map all /tweets sent
@RequiredArgsConstructor
public class TweetController {

    private final TweetService tweetService;
    
    @GetMapping
	public ResponseEntity<List<TweetDto>> getAllTweets() {
		return tweetService.getAllTweets();
	}
    
    @GetMapping("/{id}")
    public ResponseEntity<TweetDto> getSpecificTweet(@PathVariable Long id){
    	return tweetService.getSpecificTweet(id);
    }
    
    @GetMapping("/{id}/mentions")
    public ResponseEntity<List<UserDto>> getMentionedUsers(@PathVariable Long id){
    	return tweetService.getMentionedUsers(id);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<TweetDto> deleteSpecificTweet(@PathVariable Long id,@RequestBody CredentialsDto credentials){
    	return tweetService.deleteSpecificTweet(id,credentials);
    }
    
    @PostMapping
    public ResponseEntity<TweetDto> createSimpleTweet(@RequestBody CreateTweetDto tweet) {
    	return tweetService.createSimpleTweet(tweet);
    }
    
    @PostMapping("/{id}/reply")
    public ResponseEntity<TweetDto> createReplyTweet(@PathVariable Long id,@RequestBody CreateTweetDto tweet) {
    	return tweetService.createReplyTweet(tweet,id);
    }
    
    @PostMapping("/{id}/like")
    public void likeTweet(@PathVariable Long id,@RequestBody CredentialsDto credentials){
 	   tweetService.likeTweet(id,credentials);
    }
    
    @PostMapping("/{id}/repost")
    public ResponseEntity<TweetDto> createRepostTweet(@RequestBody CredentialsDto credentials, @PathVariable Long id){
    	return tweetService.createRepostTweet(credentials,id);
    }
    
    @GetMapping("/{id}/replies")
    @ResponseStatus(HttpStatus.OK)
    public List<TweetDto> getReplies(@PathVariable Long id) {
    	return tweetService.getReplies(id);
    }
    
    @GetMapping("/{id}/context")
    public ResponseEntity<ContextDto> getContext(@PathVariable Long id){
    	return tweetService.getContext(id);
    }

    @GetMapping("/{id}/tags")
    public ResponseEntity<List<HashtagDto>> getTags(@PathVariable Long id){
    	return tweetService.getTags(id);
    }
    
    @GetMapping("/{id}/likes")
    public ResponseEntity<List<UserDto>> getLikes(@PathVariable Long id){
    	return tweetService.getLikes(id);
    }
    
    @GetMapping("/{id}/reposts")
    public ResponseEntity<List<TweetDto>> getReposts(@PathVariable Long id){
    	return tweetService.getReposts(id);
    }
}
