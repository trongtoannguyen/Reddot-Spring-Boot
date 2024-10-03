package com.reddot.app.repository;

import com.reddot.app.entity.RecoveryToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecoveryTokenRepository extends JpaRepository<RecoveryToken, Integer> {
}
