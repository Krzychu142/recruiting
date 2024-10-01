package com.krzysiek.recruiting.repository;

import com.krzysiek.recruiting.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {}
