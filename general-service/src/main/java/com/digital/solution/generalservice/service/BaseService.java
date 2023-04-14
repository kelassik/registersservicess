package com.digital.solution.generalservice.service;

import com.digital.solution.generalservice.domain.constant.error.ErrorCodeConstant;
import com.digital.solution.generalservice.domain.constant.error.SourceSystemConstant;
import com.digital.solution.generalservice.domain.constant.user.audit.ActivityStatusConstant;
import com.digital.solution.generalservice.domain.constant.user.audit.ModuleNameConstant;
import com.digital.solution.generalservice.domain.constant.user.audit.UserActivityConstant;
import com.digital.solution.generalservice.domain.dto.user.Metadata;
import com.digital.solution.generalservice.domain.dto.user.audit.AdditionalDataAudit;
import com.digital.solution.generalservice.domain.dto.user.audit.StoreAuditRequest;
import com.digital.solution.generalservice.service.user.audit.AuditService;
import com.digital.solution.generalservice.utils.ReadAndWriteFileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BaseService {

    @Autowired
    @SuppressWarnings("all")
    protected ReadAndWriteFileUtils readAndWriteFileUtils;

    @Autowired
    @SuppressWarnings("all")
    protected AuditService auditService;

    protected void generateScriptQuerySchema(StringBuilder query, String schemaName) {
        if (StringUtils.isNotEmpty(schemaName)) {
            query.append(StringUtils.join(schemaName.toUpperCase(), "."));
        }
    }

    @SuppressWarnings("all")
    protected StoreAuditRequest generateStoreAuditRequest(String transactionId, Metadata metadata, ModuleNameConstant moduleName, UserActivityConstant userActivity,
                                                          ActivityStatusConstant activityStatus, SourceSystemConstant sourceSystem, ErrorCodeConstant errorCode,
                                                          AdditionalDataAudit additionalData) {
        return StoreAuditRequest.builder()
                .transactionId(transactionId)
                .userProfileId(metadata.getUserProfile().getId())
                .moduleName(moduleName)
                .userActivity(userActivity)
                .activityStatus(activityStatus)
                .sourceSystem(sourceSystem)
                .errorCode(errorCode)
                .userAgent(metadata.getUserAgent())
                .ipAddress(metadata.getIpAddress())
                .additionalData(additionalData)
                .build();
    }
}
