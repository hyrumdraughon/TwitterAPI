package com.cooksys.twitterapi;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.cooksys.twitterapi.entities.Credentials;
import com.cooksys.twitterapi.entities.Profile;
import com.cooksys.twitterapi.entities.Tweet;
import com.cooksys.twitterapi.entities.User;
import com.cooksys.twitterapi.repositories.TweetRepository;
import com.cooksys.twitterapi.repositories.UserRepository;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

	private UserRepository userRepo;
	private TweetRepository tweetRepository;

	@Override
	public void run(String... args) throws Exception {
		

		// Create four user objects

		User Austin = new User();
		User Thomas = new User();
		User Hyrum = new User();
		User Justin = new User();
		User Will = new User();

		// Values for user
		Austin.setJoined(new Timestamp(20));
		Thomas.setJoined(new Timestamp(20));
		Hyrum.setJoined(new Timestamp(20));
		Justin.setJoined(new Timestamp(20));
		Will.setJoined(new Timestamp(20));

		// is Deleted
		Austin.setDeleted(false);
		Thomas.setDeleted(false);
		Hyrum.setDeleted(false);
		Justin.setDeleted(false);
		Will.setDeleted(false);

		// Credentials for each user

		Credentials austinCred = new Credentials();
		Credentials thomasCred = new Credentials();
		Credentials hyrumCred = new Credentials();
		Credentials justinCred = new Credentials();
		Credentials willCred = new Credentials();

		// set values for credentials
		austinCred.setUsername("austinUser");
		austinCred.setPassword("a Strong password");
		thomasCred.setPassword("a Strong password");
		thomasCred.setUsername("thomasUser");
		hyrumCred.setUsername("hyrumUser");
		hyrumCred.setPassword("a Strong password");
		justinCred.setUsername("justinUser");
		justinCred.setPassword("password123");
		willCred.setUsername("willUser");
		willCred.setPassword("a Strong password");

		// set credentials back to user object
		Austin.setCredentials(austinCred);
		Thomas.setCredentials(thomasCred);
		Hyrum.setCredentials(hyrumCred);
		Justin.setCredentials(justinCred);
		Will.setCredentials(willCred);

		// set profiles for each of the five objects

		Profile profile1 = new Profile();
		Profile profile2 = new Profile();
		Profile profile3 = new Profile();
		Profile profile4 = new Profile();
		Profile profile5 = new Profile();

		// set profile values only going to be setting email to keep code short
		profile1.setEmail("austin@email.com");
		profile2.setEmail("thomas@email.com");
		profile3.setEmail("hyrum@email.com");
		profile4.setEmail("justin@email.com");
		profile5.setEmail("will@email.com");

		// Set profile values back to user
		Austin.setProfile(profile1);
		Thomas.setProfile(profile2);
		Hyrum.setProfile(profile3);
		Justin.setProfile(profile4);
		Will.setProfile(profile5);

		Austin.setFollowers(new ArrayList<User>());
		

		// Save and flush the Users to the repository
		userRepo.saveAndFlush(Austin);
		userRepo.saveAndFlush(Thomas);
		userRepo.saveAndFlush(Hyrum);
		userRepo.saveAndFlush(Justin);
		userRepo.saveAndFlush(Will);
		
		List<User> users = new ArrayList<User>();
		users.add(Austin);
		users.add(Thomas);
		users.add(Hyrum);
		users.add(Justin);
		users.add(Will);
		
		//Generate tweets for each user
		Random random = new Random();
		
		int tweets = Math.abs(random.nextInt()) % 10 + 2;//How many tweets each user will do, min 2, max 11
		Austin.setTweets(new ArrayList<Tweet>());
		for(int i = 0; i < tweets;i++) {
			Austin.getTweets().add(genSimpleTweet(Austin,users));
		}
		
		tweets = Math.abs(random.nextInt()) % 10 + 2;//How many tweets each user will do, min 2, max 11
		Thomas.setTweets(new ArrayList<Tweet>());
		for(int i = 0; i < tweets;i++) {
			Thomas.getTweets().add(genSimpleTweet(Thomas,users));
		}
		
		tweets = Math.abs(random.nextInt()) % 10 + 2;//How many tweets each user will do, min 2, max 11
		Hyrum.setTweets(new ArrayList<Tweet>());
		for(int i = 0; i < tweets;i++) {
			Hyrum.getTweets().add(genSimpleTweet(Hyrum,users));
		}
		
		tweets = Math.abs(random.nextInt()) % 10 + 2;//How many tweets each user will do, min 2, max 11
		Justin.setTweets(new ArrayList<Tweet>());
		for(int i = 0; i < tweets;i++) {
			Justin.getTweets().add(genSimpleTweet(Justin,users));
		}
		
		tweets = Math.abs(random.nextInt()) % 10 + 2;//How many tweets each user will do, min 2, max 11
		Will.setTweets(new ArrayList<Tweet>());
		for(int i = 0; i < tweets;i++) {
			Will.getTweets().add(genSimpleTweet(Will,users));
		}
		
		tweetRepository.saveAll(Austin.getTweets());//Flush all the saved tweets
		tweetRepository.saveAll(Thomas.getTweets());//Flush all the saved tweets
		tweetRepository.saveAll(Hyrum.getTweets());//Flush all the saved tweets
		tweetRepository.saveAll(Justin.getTweets());//Flush all the saved tweets
		tweetRepository.saveAll(Will.getTweets());//Flush all the saved tweets
		tweetRepository.flush();
		
		

	}
	
	//Possible addition
	//	List<User> mentions;
	//  List<Hashtag> tags;
	//  Randomly set likes from all users
	//	If either set to null, ignored
	//	Make separate functions for replies/repost
	Tweet genSimpleTweet(User author,List<User> possibleMentions) {
		
		Tweet tweet = new Tweet();
		
		//First generate content
		Random random = new Random();
		int leftLimit = 97; // letter 'a'
	    int rightLimit = 122; // letter 'z'
	    int targetStringLength = Math.abs(random.nextInt() % 20 + 10);
	    StringBuilder buffer = new StringBuilder(targetStringLength);
	    for (int i = 0; i < targetStringLength; i++) {
	        int randomLimitedInt = leftLimit + (int) 
	          (random.nextFloat() * (rightLimit - leftLimit + 1));
	        buffer.append((char) randomLimitedInt);
	    }
	    
	    
	    String content = buffer.toString();
	    List<User> mentions = new ArrayList<User>();
	    //25% chance for each user to be mentioned in the tweet
	    for(int i = 0; i < possibleMentions.size();i++) {
	    	int roll = Math.abs(random.nextInt()) % 100;
	    	if(roll > 75) {
	    		content += " @" + possibleMentions.get(i).getCredentials().getUsername() + " "; 
	    		mentions.add(possibleMentions.get(i));
	    	}
	    }
	    
	    tweet.setAuthor(author);
	    tweet.setContent(content);
	    tweet.setMentions(mentions);
	    //Give each tweet a 2 in 10 chance of being deleted
	    if(Math.abs(random.nextInt() % 10) <= 1) {
	    	tweet.setDeleted(true);
	    }
	    else {
	    	tweet.setDeleted(false);
	    }
	    
	    tweet.setHashtags(null);
	    tweet.setInReplyTo(null);
	    tweet.setRepostOf(null);
	    tweet.setPosted(new java.sql.Timestamp(System.currentTimeMillis()));
	    
	    return tweet;
	    
	}

}
