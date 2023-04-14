package com.digital.solution.generalservice.domain.dto.user.audit;

import com.digital.solution.generalservice.domain.constant.error.ErrorCodeConstant;
import com.digital.solution.generalservice.domain.constant.error.SourceSystemConstant;
import com.digital.solution.generalservice.domain.constant.user.audit.ActivityStatusConstant;
import com.digital.solution.generalservice.domain.constant.user.audit.ModuleNameConstant;
import com.digital.solution.generalservice.domain.constant.user.audit.UserActivityConstant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreAuditRequest implements Serializable {
    private static final long serialVersionUID = -6446975208144327637L;

    private String traceId;
    private String transactionId;

    @NotNull
    private Long userProfileId;

    @NotNull
    private ModuleNameConstant moduleName;

    @NotNull
    private UserActivityConstant userActivity;

    @NotNull
    private ActivityStatusConstant activityStatus;

    @NotNull
    private SourceSystemConstant sourceSystem;

    @NotNull
    private ErrorCodeConstant errorCode;

    @NotNull
    private String userAgent;

    @NotNull
    private String ipAddress;
    private AdditionalDataAudit additionalData;

    public String getResultCode() {
        return StringUtils.join(sourceSystem.getSourceSystem(), ":", errorCode.getErrorCode());
    }
}
