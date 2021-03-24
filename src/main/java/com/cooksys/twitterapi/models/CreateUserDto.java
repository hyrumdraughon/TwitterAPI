package com.cooksys.twitterapi.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CreateUserDto {

    private CredentialsDto credentials;

    private ProfileDto profile;

}
