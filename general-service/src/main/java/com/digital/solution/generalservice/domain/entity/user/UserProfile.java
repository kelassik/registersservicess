package com.digital.solution.generalservice.domain.entity.user;

import com.digital.solution.generalservice.domain.constant.user.EmailStatusConstant;
import com.digital.solution.generalservice.domain.constant.user.StatusConstant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Where;

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
@Table(name = "user_profile")
@Where(clause = "is_deleted = false")
public class UserProfile {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cif", nullable = false, length = 19)
    private String cif;

    @Column(name = "email", nullable = false, length = 50)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "email_status", nullable = false, length = 20)
    private EmailStatusConstant emailStatus;

    @Column(name = "email_verified_date")
    private LocalDateTime emailVerifiedDate;

    @Column(name = "mobile_phone", nullable = false, length = 30)
    private String mobilePhone;

    @Column(name = "language", nullable = false, length = 3)
    private String language;

    @Column(name = "date_last_success_login")
    private LocalDateTime dateLastSuccessLogin;

    @Column(name = "date_last_failed_login")
    private LocalDateTime dateLastFailedLogin;

    @Column(name = "num_failed_login", nullable = false, length = 1)
    private int numFailedLogin;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private StatusConstant status;

    @Builder.Default
    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    @Column(name = "deleted_date")
    private LocalDateTime deletedDate;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || Hibernate.getClass(this) != Hibernate.getClass(object)) return false;
        UserProfile that = (UserProfile) object;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
