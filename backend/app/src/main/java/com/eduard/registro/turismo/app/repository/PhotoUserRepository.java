package com.eduard.registro.turismo.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eduard.registro.turismo.app.model.UserPhoto;

@Repository
public interface PhotoUserRepository extends JpaRepository<UserPhoto, Long> {

    Optional<UserPhoto> findByUserId(Long userId);

}
