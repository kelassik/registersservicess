package com.digital.solution.generalservice.repository;

import com.digital.solution.generalservice.domain.entity.UserAuditTrail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAuditTrailRepository extends JpaRepository<UserAuditTrail, Long> {
}
