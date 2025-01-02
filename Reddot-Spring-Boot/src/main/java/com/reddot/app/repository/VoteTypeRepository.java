package com.reddot.app.repository;

import com.reddot.app.entity.VoteType;
import com.reddot.app.entity.enumeration.VOTETYPE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteTypeRepository extends JpaRepository<VoteType, Integer> {
    Optional<VoteType> findByType(VOTETYPE type);
}
