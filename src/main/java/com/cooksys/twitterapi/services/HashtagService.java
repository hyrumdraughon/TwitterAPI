package com.cooksys.twitterapi.services;

import java.util.List;

import com.cooksys.twitterapi.models.HashtagDto;

public interface HashtagService {

    List<HashtagDto> getAllHashtags();

    HashtagDto getHashtag(String label);

}
