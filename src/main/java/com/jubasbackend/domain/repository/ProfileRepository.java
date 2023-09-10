package com.jubasbackend.domain.repository;

import com.jubasbackend.domain.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, UUID> {
    List<Profile> findAllByUserId(UUID id);

}
