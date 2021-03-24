package com.cooksys.twitterapi.controllers;

import com.cooksys.twitterapi.models.CreateUserDto;
import com.cooksys.twitterapi.models.CredentialsDto;
import com.cooksys.twitterapi.models.TweetDto;
import com.cooksys.twitterapi.models.UserDto;
import com.cooksys.twitterapi.services.UserService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users") //will map all requests to localhost:8080/user
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserDto> getAllUsers() {
    	return userService.getAllUsers();
    }
    
    @GetMapping("/@{username}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUser(@PathVariable String username) {
    	return userService.getUser(username);
    }
    
    @PatchMapping("/@{username}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto updateUserProfile(@PathVariable String username, @RequestBody CreateUserDto createUserDto) {
    	return userService.updateUserProfile(username, createUserDto);
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public UserDto createUser(@RequestBody CreateUserDto createUserDto) {
    	return userService.createUser(createUserDto);
    }
    
    @DeleteMapping("/@{username}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto deleteUser(@PathVariable String username, @RequestBody CredentialsDto credentialsDto) {
    	return userService.deleteUser(username, credentialsDto);
    }
    
    @PostMapping("/@{username}/follow")
    @ResponseStatus(HttpStatus.OK)
    public void createFollow(@PathVariable String username, @RequestBody CredentialsDto credentialsDto) {
    	userService.createFollow(username, credentialsDto);
    }
    
    @PostMapping("/@{username}/unfollow")
    @ResponseStatus(HttpStatus.OK)
    public void unfollow(@PathVariable String username, @RequestBody CredentialsDto credentialsDto) {
    	userService.unfollow(username, credentialsDto);
    }
    
    @GetMapping("/@{username}/followers")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getFollowers(@PathVariable String username) {
    	return userService.getFollowers(username);
    }
    
    @GetMapping("/@{username}/following")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getFollowing(@PathVariable String username) {
    	return userService.getFollowing(username);

    }
    
    @GetMapping("/@{username}/tweets")
    @ResponseStatus(HttpStatus.OK)
    public List<TweetDto> getTweets(@PathVariable String username) {
    	return userService.getTweets(username);
    }

    @GetMapping("/@{username}/feed")
    @ResponseStatus(HttpStatus.OK)
    public List<TweetDto> getFeed(@PathVariable String username){
        return userService.getFeed(username);
    }

    @GetMapping("/@{username}/mentions")
    @ResponseStatus(HttpStatus.OK)
    public List<TweetDto> getMentions(@PathVariable String username){
        return userService.getMentions(username);
    }
}
