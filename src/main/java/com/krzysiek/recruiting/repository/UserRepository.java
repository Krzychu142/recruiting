package com.krzysiek.recruiting.repository;

import com.krzysiek.recruiting.model.User;
import io.micrometer.common.lang.NonNullApi;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@NonNullApi
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.isConfirmed = :isConfirmed, u.confirmationToken = null WHERE u.id = :id")
    int updateUserConfirmationFields(@Param("id") Long id, @Param("isConfirmed") Boolean isConfirmed);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.resetPasswordToken = :resetPasswordToken WHERE u.id = :id")
    int setUserResetPasswordToken(@Param("id") Long id, @Param("resetPasswordToken") String resetPasswordToken);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.password = :password, u.resetPasswordToken = null WHERE u.id = :id")
    int updatePassword(@Param("id") Long id, @Param("password") String password);

    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);

}
