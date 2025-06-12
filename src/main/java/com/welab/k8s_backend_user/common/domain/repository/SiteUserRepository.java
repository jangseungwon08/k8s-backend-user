package com.welab.k8s_backend_user.common.domain.repository;

import com.welab.k8s_backend_user.common.domain.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SiteUserRepository extends JpaRepository<SiteUser, Long> {
    SiteUser findByUserId(String userId);
}
