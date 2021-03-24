package com.cooksys.twitterapi.controllers;

import java.util.List;

import com.cooksys.twitterapi.models.HashtagDto;
import com.cooksys.twitterapi.services.HashtagService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/tags")
@AllArgsConstructor
public class HashtagController {

    private final HashtagService hashtagService;

    @GetMapping
    public List<HashtagDto> getAllHashtags(){
        return hashtagService.getAllHashtags();
    }

    @GetMapping("/{label}")
    public HashtagDto getHashtag (@PathVariable String label) {
        return hashtagService.getHashtag(label);
    }
}
