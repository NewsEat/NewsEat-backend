package com.company.newseat.email.domain;

import com.company.newseat.email.domain.type.BooleanType;
import com.company.newseat.email.domain.type.Purpose;
import com.company.newseat.global.domain.type.Status;
import com.company.newseat.global.domain.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;


@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@AllArgsConstructor
public class EmailAuth extends BaseTimeEntity {

    @Id
    @Getter
    @Column(name = "email_auth_id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long emailAuthId;

    @Getter
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status = Status.ACTIVE;

    @NotNull
    @Column(name = "email", length = 50)
    private String email;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "purpose", length = 10)
    private Purpose purpose;

    @NotNull
    @Column(name = "code", length = 6)
    private String code;

    @NotNull
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'F'")
    private BooleanType isChecked;

    public void setCode(String code) {
        this.code = code;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked ? BooleanType.T : BooleanType.F;
    }
}
