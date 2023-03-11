package com.malakhov.journalofpractice.repositories;

import com.malakhov.journalofpractice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
