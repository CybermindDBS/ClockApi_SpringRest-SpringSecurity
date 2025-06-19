package com.example.springrest_refresher.restclient.clockwebapp.repository;

import com.example.springrest_refresher.restclient.clockwebapp.model.entity.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<MyUser, Long> {
    Optional<MyUser> findUserByUsername(String username);
}
