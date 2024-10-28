package com.zilmar.springsecurity.database.repositories;

import com.zilmar.springsecurity.modules.roles.entities.Role;
import com.zilmar.springsecurity.modules.roles.enums.RolesValues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    public Optional<Role> findByName(RolesValues name);
}
