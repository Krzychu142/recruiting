package com.krzysiek.recruiting.repository;

import com.krzysiek.recruiting.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.isConfirmed = :isConfirmed, u.confirmationToken = null WHERE u.id = :id")
    int updateUserFields(@Param("id") Long id, @Param("isConfirmed") Boolean isConfirmed);

    Optional<User> findByEmail(String email);

}
