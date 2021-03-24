package com.cooksys.twitterapi.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.twitterapi.services.ValidateService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/validate")
@AllArgsConstructor
public class ValidateController {

    private final ValidateService validateService;

    @GetMapping("/tag/exists/{label}")
    public boolean validateHashtagExist(@PathVariable(name = "label") String hashtag) {
    	return validateService.validateHashtagExist(hashtag);
    }
    @GetMapping("/username/exists/@{username}")
    public boolean validateUserExist(@PathVariable String username) {
    	return validateService.validateUserExist(username);
    }
    @GetMapping("/username/available/@{username}")
    public boolean validateUsernameAvailable(@PathVariable String username) {
    	return validateService.validateUsernameAvailable(username);
    }
}
