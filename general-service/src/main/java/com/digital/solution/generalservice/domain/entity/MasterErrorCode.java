package com.digital.solution.generalservice.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "master_error_code")
public class MasterErrorCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "source_system", nullable = false, length = 50)
    private String sourceSystem;

    @Column(name = "error_code", nullable = false, length = 10)
    private String errorCode;

    @Column(name = "eng_title", length = 100)
    private String engTitle;

    @Column(name = "ind_title", length = 100)
    private String indTitle;

    @Column(name = "eng_message", length = 500)
    private String engMessage;

    @Column(name = "ind_message", length = 500)
    private String indMessage;

    @Column(name = "description", length = 1000)
    private String description;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || Hibernate.getClass(this) != Hibernate.getClass(object)) return false;
        MasterErrorCode masterErrorCode = (MasterErrorCode) object;
        return id != null && Objects.equals(id, masterErrorCode.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
