package com.tqk.profileservice.repository;

import com.tqk.profileservice.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile,Integer> {

    Optional<Profile> findByAccountId(Integer accountId);

    boolean existsByPhone(String phone);
}
