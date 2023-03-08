package com.malakhov.journalofpractice.repositories;

import com.malakhov.journalofpractice.models.Role;
import com.malakhov.journalofpractice.models.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
