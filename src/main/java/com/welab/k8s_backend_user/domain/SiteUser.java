package com.welab.k8s_backend_user.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Entity
@Slf4j
@Table(name = "site_user")
public class SiteUser {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;

    @Column(name = "user_id", unique = true, nullable = false)
    @Getter @Setter
    private String userId;

    @Column(name = "password", nullable = false)
//    null이 있을 수 있는 엔티티 칼럼이면 Optional로 주는 것이 바람직
    @Getter @Setter
    private String password;

    @Column(name = "phone_number", nullable = false)
    @Getter @Setter
    private String phoneNumber;

//    flage를 두는 것이 더 바람직하다.
    @Column(name = "deleted", nullable = false)
    @Getter @Setter
    private Boolean deleted = false; //flag를 하나 두는 방법

}
