package com.cooksys.twitterapi.repositories;

import com.cooksys.twitterapi.entities.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	Optional<User> findByCredentialsUsername(String username);


}
