package com.digital.solution.generalservice.repository;

import com.digital.solution.generalservice.domain.constant.user.EmailStatusConstant;
import com.digital.solution.generalservice.domain.entity.user.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    List<UserProfile> findByCifOrEmailOrMobilePhone(String cif, String email, String mobilePhone);
    Optional<UserProfile> findByCifAndEmail(String cif, String email);
    Optional<UserProfile> findByCifAndEmailStatus(String cif, EmailStatusConstant emailStatus);
}
