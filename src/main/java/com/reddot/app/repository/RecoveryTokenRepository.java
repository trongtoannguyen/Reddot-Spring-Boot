package com.reddot.app.repository;

import com.reddot.app.entity.RecoveryToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecoveryTokenRepository extends JpaRepository<RecoveryToken, Integer> {
    Optional<RecoveryToken> findByToken(String code);
}
