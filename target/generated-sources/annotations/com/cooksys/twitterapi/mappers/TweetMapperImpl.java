package com.cooksys.twitterapi.mappers;

import com.cooksys.twitterapi.entities.Profile;
import com.cooksys.twitterapi.entities.Tweet;
import com.cooksys.twitterapi.entities.User;
import com.cooksys.twitterapi.models.ProfileDto;
import com.cooksys.twitterapi.models.TweetDto;
import com.cooksys.twitterapi.models.UserDto;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-03-18T15:35:44-0700",
    comments = "version: 1.2.0.Final, compiler: javac, environment: Java 11.0.1 (Oracle Corporation)"
)
@Component
public class TweetMapperImpl implements TweetMapper {

    @Autowired
    private UserMapper userMapper;

    @Override
    public Tweet dtoToEntity(TweetDto tweetDto) {
        if ( tweetDto == null ) {
            return null;
        }

        Tweet tweet = new Tweet();

        tweet.setId( tweetDto.getId() );
        tweet.setAuthor( userDtoToUser( tweetDto.getAuthor() ) );
        tweet.setPosted( tweetDto.getPosted() );
        tweet.setContent( tweetDto.getContent() );
        tweet.setRepostOf( dtoToEntity( tweetDto.getRepostOf() ) );
        tweet.setInReplyTo( dtoToEntity( tweetDto.getInReplyTo() ) );

        return tweet;
    }

    @Override
    public TweetDto entityToDto(Tweet tweet) {
        if ( tweet == null ) {
            return null;
        }

        TweetDto tweetDto = new TweetDto();

        tweetDto.setId( tweet.getId() );
        tweetDto.setAuthor( userMapper.entityToDto( tweet.getAuthor() ) );
        tweetDto.setPosted( tweet.getPosted() );
        tweetDto.setContent( tweet.getContent() );
        tweetDto.setInReplyTo( entityToDto( tweet.getInReplyTo() ) );
        tweetDto.setRepostOf( entityToDto( tweet.getRepostOf() ) );

        return tweetDto;
    }

    @Override
    public List<TweetDto> entitiesToDtoList(List<Tweet> tweets) {
        if ( tweets == null ) {
            return null;
        }

        List<TweetDto> list = new ArrayList<TweetDto>( tweets.size() );
        for ( Tweet tweet : tweets ) {
            list.add( entityToDto( tweet ) );
        }

        return list;
    }

    protected Profile profileDtoToProfile(ProfileDto profileDto) {
        if ( profileDto == null ) {
            return null;
        }

        Profile profile = new Profile();

        profile.setFirstName( profileDto.getFirstName() );
        profile.setLastName( profileDto.getLastName() );
        profile.setEmail( profileDto.getEmail() );
        profile.setPhone( profileDto.getPhone() );

        return profile;
    }

    protected User userDtoToUser(UserDto userDto) {
        if ( userDto == null ) {
            return null;
        }

        User user = new User();

        user.setJoined( userDto.getJoined() );
        user.setProfile( profileDtoToProfile( userDto.getProfile() ) );

        return user;
    }
}
