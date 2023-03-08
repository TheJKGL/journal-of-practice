package com.malakhov.journalofpractice.repositories;

import com.malakhov.journalofpractice.models.Credential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CredentialsRepository extends JpaRepository<Credential, Long> {
    Optional<Credential> findByEmail(String email);
}
