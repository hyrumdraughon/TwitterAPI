package com.cooksys.twitterapi.repositories;

import java.util.Optional;

import com.cooksys.twitterapi.entities.Hashtag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HashtagRepository extends JpaRepository<Hashtag, Long> {

    Optional<Hashtag> findByLabel(String label);
}
