package com.reddot.app.repository;

import com.reddot.app.entity.Role;
import com.reddot.app.entity.enumeration.ROLENAME;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(ROLENAME rolename);
}
