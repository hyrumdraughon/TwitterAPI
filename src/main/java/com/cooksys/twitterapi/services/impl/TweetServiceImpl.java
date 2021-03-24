package com.cooksys.twitterapi.services.impl;

import com.cooksys.twitterapi.entities.Hashtag;
import com.cooksys.twitterapi.entities.Tweet;
import com.cooksys.twitterapi.entities.User;
import com.cooksys.twitterapi.exceptions.BadRequestException;
import com.cooksys.twitterapi.exceptions.NotFoundException;
import com.cooksys.twitterapi.mappers.HashtagMapper;
import com.cooksys.twitterapi.mappers.TweetMapper;
import com.cooksys.twitterapi.mappers.UserMapper;
import com.cooksys.twitterapi.models.ContextDto;
import com.cooksys.twitterapi.models.CreateTweetDto;
import com.cooksys.twitterapi.models.CredentialsDto;
import com.cooksys.twitterapi.models.HashtagDto;
import com.cooksys.twitterapi.models.TweetDto;
import com.cooksys.twitterapi.models.UserDto;
import com.cooksys.twitterapi.repositories.HashtagRepository;
import com.cooksys.twitterapi.repositories.TweetRepository;
import com.cooksys.twitterapi.repositories.UserRepository;
import com.cooksys.twitterapi.services.TweetService;

import lombok.AllArgsConstructor;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TweetServiceImpl implements TweetService {
	
	private TweetMapper tweetMapper;
	
	private TweetRepository tweetRepository;
	
	private UserMapper userMapper;
	
	private UserRepository userRepository;
	
	private HashtagRepository hashtagRepository;
	
	private HashtagMapper hashtagMapper;
	
	public ResponseEntity<List<TweetDto>> getAllTweets(){
		List<Tweet> tweets = new ArrayList<Tweet>(tweetRepository.findAll());
		//Remove all deleted tweets
		for(Tweet tweet : new ArrayList<Tweet>(tweets)) {
			if(tweet.isDeleted()) {
				tweets.remove(tweet);
			}
		}
		Collections.reverse(tweets);//Wants it in reverse chronological order
		return new ResponseEntity<List<TweetDto>>(tweetMapper.entitiesToDtoList(tweets),HttpStatus.OK);
	}
	
	
	//Tweet must exist and not be deleted, then it will return correctly
	//Otherwise it will throw a not found exception
	public ResponseEntity<TweetDto> getSpecificTweet(Long id) {
		if(tweetRepository.existsById(id)) {
			Tweet tweet = tweetRepository.getOne(id);
			if (tweet.isDeleted()) {
				throw new NotFoundException("Tweet " + id.toString() + " does not exist");
			}
			else {
				return new ResponseEntity<TweetDto>(tweetMapper.entityToDto(tweet),HttpStatus.OK);
			}
		}
		else {
			throw new NotFoundException("Tweet " + id.toString() + " does not exist");
		}
	}
	
	//Tweet must exist and not be deleted, then it will return correctly
	//Otherwise it will throw a not found exception
	private  Tweet getSpecificTweetEntity(Long id) {
		if(tweetRepository.existsById(id)) {
			Tweet tweet = tweetRepository.getOne(id);
			if (tweet.isDeleted()) {
				throw new NotFoundException("Tweet " + id.toString() + " does not exist");
			}
			else {
				return tweet;
			}
		}
		else {
			throw new NotFoundException("Tweet " + id.toString() + " does not exist");
		}
	}
	
	//Given valid credentials to the user who created a tweet
	//Allows it to be deleted
	//Returns the tweet's value before being deleted
	public ResponseEntity<TweetDto> deleteSpecificTweet(Long id, CredentialsDto credentials){
		validateCredentials(credentials,id);
		Tweet tweetToEdit = tweetRepository.getOne(id);
		TweetDto returnValue = tweetMapper.entityToDto(tweetToEdit);
		tweetToEdit.setDeleted(true);
		tweetRepository.saveAndFlush(tweetToEdit);
		return new ResponseEntity<TweetDto>(returnValue,HttpStatus.OK);
	}
	
	
	//Validate, if anything goes wrong an error will be thrown
	//Checks
	//	If user exists
	//  If user has been deleted
	//  If the password matches the user
	//  If the tweet exists
	//  If the tweet belongs to the username
	private void validateCredentials(CredentialsDto credentials,Long tweetId) {
		// check user exists and is not deleted
		Optional<User> optionalUser = userRepository.findByCredentialsUsername(credentials.getUsername());
		if (optionalUser.isEmpty()) {
			throw new NotFoundException("the user " + credentials.getUsername() + " does not exist");
		} else if (optionalUser.get().isDeleted()) {
			throw new NotFoundException("the user " + credentials.getUsername() + " has been deleted");
		}
		
		if (!credentials.getPassword().equals(optionalUser.get().getCredentials().getPassword())) {
			throw new BadRequestException("incorrect password for user " + credentials.getUsername());
		}
		
		//Check if the username in credentials matches the owner of the tweetId
		if(getSpecificTweet(tweetId).getBody().getAuthor().getUsername().compareTo(credentials.getUsername())!= 0) {
			throw new BadRequestException("Tweet " + tweetId + " does not belong to " + credentials.getUsername()+ ".");
		}
		
		//If you have reached this far without reaching any exceptions, it is a valid tweet and user

	}
	
	//Validate, if anything goes wrong an error will be thrown
	//Checks
	//	If user exists
	//  If user has been deleted
	//  If the password matches the user
	private void validateCredentials(CredentialsDto credentials) {
		// check user exists and is not deleted
		Optional<User> optionalUser = userRepository.findByCredentialsUsername(credentials.getUsername());
		if (optionalUser.isEmpty()) {
			throw new NotFoundException("the user " + credentials.getUsername() + " does not exist");
		} else if (optionalUser.get().isDeleted()) {
			throw new NotFoundException("the user " + credentials.getUsername() + " has been deleted");
		}
		
		if (!credentials.getPassword().equals(optionalUser.get().getCredentials().getPassword())) {
			throw new BadRequestException("incorrect password for user " + credentials.getUsername());
		}
	}
	
	//Fails if
	//Credentials do not match, or are not a valid user
	//Tweet is not valid
	public void likeTweet(Long id, CredentialsDto credentials){
		validateCredentials(credentials);//Validate the credentials are valid
		Tweet tweet = getSpecificTweetEntity(id);//Tweet to be liked
		Optional<User> userOptional = userRepository.findByCredentialsUsername(credentials.getUsername());
		User user = userOptional.get();
		tweet.getLikes().add(user);
		tweetRepository.saveAndFlush(tweet);
		return;
	}
	
	public ResponseEntity<List<UserDto>> getMentionedUsers(Long id){
		Tweet tweet = getSpecificTweetEntity(id);
		
		List<User> users = tweet.getMentions();
		
		//Remove deleted users
		for(User user : new ArrayList<User>(tweet.getMentions())) {
			if(user.isDeleted()) {
				users.remove(user);
			}
		}
		List<UserDto> userFinal = userMapper.entitiesToDtos(users);
		return new ResponseEntity<List<UserDto>>(userFinal,HttpStatus.OK);
	}
	
	//Given a create tweet dto, POST a new tweet
	public ResponseEntity<TweetDto> createSimpleTweet(CreateTweetDto tweetCreate){
		//Ensure the credentials are valid
		CredentialsDto credentials = tweetCreate.getCredentials();
		validateCredentials(credentials);
		
		String content = tweetCreate.getContent();//Content is the text of the tweet
		
		//Find the author
		Optional<User> userOptional = userRepository.findByCredentialsUsername(credentials.getUsername());
		User user = userOptional.get();
		
		//Fill out tweet fields
		//Parse for any information you are not explicity given
		Tweet newTweet = new Tweet();
		newTweet.setPosted(new java.sql.Timestamp(System.currentTimeMillis()));
		newTweet.setAuthor(user);
		newTweet.setContent(content);
		newTweet.setDeleted(false);
		newTweet.setHashtags(parseHashtags(content,newTweet.getPosted()));
		newTweet.setInReplyTo(null);
		newTweet.setLikes(new ArrayList<User>());
		newTweet.setMentions(parseMentions(content));
		newTweet.setReplies(new ArrayList<Tweet>());
		newTweet.setRepostOf(null);
		newTweet.setReposts(new ArrayList<Tweet>());
		//Save it to the repository and return a tweetDto of it
		tweetRepository.saveAndFlush(newTweet);
		return new ResponseEntity<TweetDto>(tweetMapper.entityToDto(newTweet),HttpStatus.OK);
	}
	
	public ResponseEntity<TweetDto> createReplyTweet(CreateTweetDto tweetCreate, Long id){
		//Ensure the credentials are valid
		CredentialsDto credentials = tweetCreate.getCredentials();
		validateCredentials(credentials);
		
		String content = tweetCreate.getContent();//Content is the text of the tweet
		
		//Find the author
		Optional<User> userOptional = userRepository.findByCredentialsUsername(credentials.getUsername());
		User user = userOptional.get();
		
		//Fill out tweet fields
		//Parse for any information you are not explicity given
		Tweet newTweet = new Tweet();
		newTweet.setPosted(new java.sql.Timestamp(System.currentTimeMillis()));
		newTweet.setAuthor(user);
		newTweet.setContent(content);
		newTweet.setDeleted(false);
		newTweet.setHashtags(parseHashtags(content,newTweet.getPosted()));
		
		//Set the tweet you are replying to as the one whose id you are given
		newTweet.setInReplyTo(getSpecificTweetEntity(id));
		
		newTweet.setLikes(new ArrayList<User>());
		newTweet.setMentions(parseMentions(content));
		newTweet.setReplies(new ArrayList<Tweet>());
		newTweet.setRepostOf(null);
		newTweet.setReposts(new ArrayList<Tweet>());
		//Save it to the repository and return a tweetDto of it
		tweetRepository.saveAndFlush(newTweet);
		return new ResponseEntity<TweetDto>(tweetMapper.entityToDto(newTweet),HttpStatus.OK);
	}
	
	public ResponseEntity<TweetDto> createRepostTweet(CredentialsDto credentials, Long id){
		//Ensure the credentials are valid
		validateCredentials(credentials);
		
		//Find the author
		Optional<User> userOptional = userRepository.findByCredentialsUsername(credentials.getUsername());
		User user = userOptional.get();
		
		//Fill out tweet fields
		//Parse for any information you are not explicity given
		Tweet newTweet = new Tweet();
		newTweet.setPosted(new java.sql.Timestamp(System.currentTimeMillis()));
		newTweet.setAuthor(user);
		newTweet.setContent(null);
		newTweet.setDeleted(false);
		newTweet.setHashtags(null);
		
		//Set the tweet you are replying to as the one whose id you are given
		newTweet.setInReplyTo(null);
		
		newTweet.setLikes(new ArrayList<User>());
		newTweet.setMentions(null);
		newTweet.setReplies(new ArrayList<Tweet>());
		newTweet.setRepostOf(getSpecificTweetEntity(id));
		newTweet.setReposts(new ArrayList<Tweet>());
		//Save it to the repository and return a tweetDto of it
		tweetRepository.saveAndFlush(newTweet);
		return new ResponseEntity<TweetDto>(tweetMapper.entityToDto(newTweet),HttpStatus.OK);
	}


	//Given a string it will gather all mentions
	private List<User> parseMentions(String content) {
		String pattern = "@[A-Za-z0-9_]+";
		
		Matcher m = Pattern.compile(pattern).matcher(content);
		
		List<User> returnValue = new ArrayList<User>();
		while(m.find()) {
			String username = m.group();
			username = username.replace("@", "");
			Optional<User> userOptional = userRepository.findByCredentialsUsername(username);
			if(userOptional.isPresent()) {
				User user = userOptional.get();
				returnValue.add(user);
			}
		}
		
		
		return returnValue;
	}


	private List<Hashtag> parseHashtags(String content, java.sql.Timestamp timestamp) {
		String pattern = "^*#[A-Za-z0-9_]+";
		
		Matcher m = Pattern.compile(pattern).matcher(content);
		
		List<Hashtag> hashtags = new ArrayList<Hashtag>();
		while(m.find()) {
			String hashtag = m.group();
			hashtag = hashtag.replace("#", "");
			if (!hashtag.isEmpty()){
				Optional<Hashtag> optionalHashtag = hashtagRepository.findByLabel(hashtag);
				if(optionalHashtag.isEmpty()){//If it does not exist, make it
					Hashtag newHashtag = new Hashtag();
					newHashtag.setFirstUsed(timestamp);
					newHashtag.setLabel(hashtag);
					newHashtag.setLastUsed(timestamp);
					hashtagRepository.saveAndFlush(newHashtag);
					hashtags.add(newHashtag);
			    }
				else {
					Hashtag foundHashtag = optionalHashtag.get();
					foundHashtag.setLastUsed(timestamp);
					hashtagRepository.saveAndFlush(foundHashtag);
					hashtags.add(optionalHashtag.get());
				}
			}
		}
		return hashtags;
	}


	@Override
	public List<TweetDto> getReplies(Long id) {
		if (id == null) {
			throw new BadRequestException("must provide the tweet id");
		}
		
		Optional<Tweet> optionalTweet = tweetRepository.findById(id);
		if (optionalTweet.isEmpty()) {
			throw new NotFoundException("the tweet with id: " + id + " does not exist");
		} else if (optionalTweet.get().isDeleted()) {
			throw new NotFoundException("the tweet with id: " + id + " has been deleted");
		}
		
		List<Tweet> activeReplies = new ArrayList<>();
		for (Tweet r : optionalTweet.get().getReplies()) {
			if (!r.isDeleted()) {
				activeReplies.add(r);
			}
		}
		
		return tweetMapper.entitiesToDtoList(activeReplies);
	}
	
	public ResponseEntity<ContextDto> getContext(Long id){
		Tweet tweet = getSpecificTweetEntity(id);
		ContextDto context = new ContextDto();
		context.setTarget(tweetMapper.entityToDto(tweet));
		
		Deque<Tweet> afterTweetsToCheck = new ArrayDeque<Tweet>();
		List<Tweet> tweetsAfter = new ArrayList<Tweet>();
		if(tweet.getReplies().size() != 0) {
			for(int i = 0; i < tweet.getReplies().size();i++) {
				Tweet tweetInQuestion = tweet.getReplies().get(i);
				if(tweetInQuestion.isDeleted() == false){ //If its not deleted add it to the list
					tweetsAfter.add(tweetInQuestion);
				}
				afterTweetsToCheck.push(tweetInQuestion);//Check all
			}
		}
		
		while(!afterTweetsToCheck.isEmpty()) {//While there are still elements to check
			Tweet poppedTweet = afterTweetsToCheck.pop();
			for(int i = 0; i < poppedTweet.getReplies().size();i++) {
				Tweet tweetInQuestion = poppedTweet.getReplies().get(i);
				if(tweetInQuestion.isDeleted() == false){ //If its not deleted add it to the list
					tweetsAfter.add(tweetInQuestion);
				}
				afterTweetsToCheck.push(tweetInQuestion);//Check all
			}
		}
		
		List<TweetDto> tweetsBefore = new ArrayList<TweetDto>();
		Tweet tweetIterator = tweet.getInReplyTo();
		while(tweetIterator != null) {
			tweetsBefore.add(tweetMapper.entityToDto(tweetIterator));
			tweetIterator = tweetIterator.getInReplyTo();
		}
		
		//Sort it based on id (as chronological order is the same as ID order)
		Collections.sort(tweetsBefore, new Comparator<TweetDto>(){
			public int compare(TweetDto o1, TweetDto o2) {
				return (int) (o1.getId() - o2.getId());
			}
		});
		
		List<TweetDto> tweetAfterReturn = tweetMapper.entitiesToDtoList(tweetsAfter);
		//Sort it based on id (as chronological order is the same as ID order)
		Collections.sort(tweetAfterReturn, new Comparator<TweetDto>(){
			public int compare(TweetDto o1, TweetDto o2) {
				return (int) (o1.getId() - o2.getId());
			}
		});
		
		context.setAfter(tweetAfterReturn);
		context.setBefore(tweetsBefore);
		return new ResponseEntity<ContextDto>(context,HttpStatus.OK);
	}
	
	public ResponseEntity<List<HashtagDto>> getTags(Long id){
		Tweet tweet = getSpecificTweetEntity(id);
		return new ResponseEntity<List<HashtagDto>>(hashtagMapper.entitiesToDtos(tweet.getHashtags()),HttpStatus.OK);
	}
	
	public ResponseEntity<List<UserDto>> getLikes(Long id){
		Tweet tweet = getSpecificTweetEntity(id);
		List<User> users = new ArrayList<User>(tweet.getLikes());//Make shallow copy of the array so you can delete members from your new array
		for(User user : new ArrayList<User>(users)) {
			if(user.isDeleted()) {
				users.remove(user);//Remove all delete users
			}
		}
		return new ResponseEntity<List<UserDto>>(userMapper.entitiesToDtos(users),HttpStatus.OK);
	}
	
	public ResponseEntity<List<TweetDto>> getReposts(Long id){
		Tweet tweetTarget = getSpecificTweetEntity(id);
		List<Tweet> tweets = new ArrayList<Tweet>(tweetTarget.getReposts());
		for(Tweet tweet : new ArrayList<Tweet>(tweets)) {
			if(tweet.isDeleted()) {
				tweets.remove(tweet);
			}
		}
		return new ResponseEntity<List<TweetDto>>(tweetMapper.entitiesToDtoList(tweets),HttpStatus.OK);
		
	}

}
