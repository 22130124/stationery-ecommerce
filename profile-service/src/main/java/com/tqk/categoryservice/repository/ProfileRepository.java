package com.tqk.categoryservice.repository;

import com.tqk.categoryservice.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile,Integer> {

    Optional<Profile> findByAccountId(Integer accountId);
}
