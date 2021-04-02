package com.yoga.qrCode.repository;

import com.yoga.qrCode.model.entity.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {
    Optional<UserGroup> findByCustomerId(long customerId);
}
