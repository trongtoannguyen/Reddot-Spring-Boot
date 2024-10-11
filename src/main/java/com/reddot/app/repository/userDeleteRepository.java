package com.reddot.app.repository;

import com.reddot.app.entity.UserOnDelete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface userDeleteRepository extends JpaRepository<UserOnDelete, Integer> {
}
