package com.cooksys.twitterapi.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CreateTweetDto {

    private String content;

    private CredentialsDto credentials;

}
