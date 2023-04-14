package com.digital.solution.generalservice.service.user.audit;

import com.digital.solution.generalservice.domain.dto.user.audit.StoreAuditRequest;
import com.digital.solution.generalservice.domain.entity.UserAuditTrail;
import com.digital.solution.generalservice.repository.UserAuditTrailRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class AuditService {

    @Autowired
    @SuppressWarnings("all")
    private UserAuditTrailRepository userAuditTrailRepository;

    public void storeAudit(StoreAuditRequest request) {
        log.info("start storeAudit");
        log.info("request data : {}", request);
        var storeAuditRequest = generateStoreAuditRequest(request);
        userAuditTrailRepository.save(storeAuditRequest);
        log.info("success store audit");
    }

    private UserAuditTrail generateStoreAuditRequest(StoreAuditRequest request) {
        return UserAuditTrail.builder()
                .traceId(request.getTraceId())
                .transactionId(request.getTransactionId())
                .userProfileId(request.getUserProfileId())
                .createdDate(LocalDateTime.now())
                .moduleName(request.getModuleName().getModuleName())
                .activity(request.getUserActivity().getUserActivity())
                .activityStatus(request.getActivityStatus())
                .resultCode(request.getResultCode())
                .userAgent(request.getUserAgent())
                .ipAddress(request.getIpAddress())
                .additionalData(request.getAdditionalData())
                .build();
    }
}
