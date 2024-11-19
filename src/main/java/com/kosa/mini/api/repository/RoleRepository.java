package com.kosa.mini.api.repository;

import com.kosa.mini.api.entity.Member;
import com.kosa.mini.api.entity.Role;
import com.kosa.mini.api.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(String roleName);

}
