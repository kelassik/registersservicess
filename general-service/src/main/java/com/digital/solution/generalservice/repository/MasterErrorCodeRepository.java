package com.digital.solution.generalservice.repository;

import com.digital.solution.generalservice.domain.entity.MasterErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MasterErrorCodeRepository extends JpaRepository<MasterErrorCode, Long> {

    Optional<MasterErrorCode> findBySourceSystemAndErrorCode(String sourceSystem, String errorCode);
}
