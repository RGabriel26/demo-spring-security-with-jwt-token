package com.example.demomakebymespringsecuritywithjwt.repositoy;

import com.example.demomakebymespringsecuritywithjwt.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    Boolean existsByEmail(String email);

}
