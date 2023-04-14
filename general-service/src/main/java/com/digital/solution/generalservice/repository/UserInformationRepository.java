package com.digital.solution.generalservice.repository;

import com.digital.solution.generalservice.domain.entity.user.UserInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInformationRepository extends JpaRepository<UserInformation, Long> {
    Optional<UserInformation> findByUserProfileId(Long userProfileId);
}
