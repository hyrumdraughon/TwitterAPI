package com.cooksys.twitterapi.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cooksys.twitterapi.entities.Hashtag;
import com.cooksys.twitterapi.exceptions.BadRequestException;
import com.cooksys.twitterapi.exceptions.NotFoundException;
import com.cooksys.twitterapi.mappers.HashtagMapper;
import com.cooksys.twitterapi.models.HashtagDto;
import com.cooksys.twitterapi.repositories.HashtagRepository;
import com.cooksys.twitterapi.services.HashtagService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class HashtagServiceImpl implements HashtagService {
    
    private HashtagRepository hashtagRepository;
    private HashtagMapper hashtagMapper;


    @Override
    public List<HashtagDto> getAllHashtags() {
    	return hashtagMapper.entitiesToDtos(hashtagRepository.findAll());
    }

    @Override
    public HashtagDto getHashtag(String label) {
       if (label.isEmpty()){
           throw new BadRequestException("Must provide a hashtag");
       }

       Optional<Hashtag> optionalHashtag = hashtagRepository.findByLabel(label);

       if(optionalHashtag.isEmpty()){
           throw new NotFoundException("The hashtag" + label + "does not exist");
       }

       return hashtagMapper.entityToDto(optionalHashtag.get());
    }

}
