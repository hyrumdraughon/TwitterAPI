package com.cooksys.twitterapi.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@NoArgsConstructor
@Data
public class UserDto {

    private String username;

    private ProfileDto profile;

    private Timestamp joined;

}
