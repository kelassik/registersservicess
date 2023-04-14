package com.digital.solution.generalservice.domain.entity;

import com.digital.solution.generalservice.domain.constant.user.audit.ActivityStatusConstant;
import com.digital.solution.generalservice.domain.dto.user.audit.AdditionalDataAudit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_audit_trail")
public class UserAuditTrail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "trace_id", length = 100)
    private String traceId;

    @Column(name = "transaction_id", length = 15)
    private String transactionId;

    @Column(name = "user_profile_id", nullable = false)
    private Long userProfileId;

    @Builder.Default
    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "module_name", nullable = false, length = 100)
    private String moduleName;

    @Column(name = "activity", nullable = false, length = 1000)
    private String activity;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 10)
    private ActivityStatusConstant activityStatus;

    @Column(name = "result_code", nullable = false, length = 15)
    private String resultCode;

    @Column(name = "user_agent", nullable = false, length = 50)
    private String userAgent;

    @Column(name = "ip_address", nullable = false, length = 16)
    private String ipAddress;

    @Column(name = "additional_data", length = 4000)
    private AdditionalDataAudit additionalData;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || Hibernate.getClass(this) != Hibernate.getClass(object)) return false;
        UserAuditTrail that = (UserAuditTrail) object;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
