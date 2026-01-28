package com.helmes.sectorsapi.repository;

import com.helmes.sectorsapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDataRepository extends JpaRepository<User, Long> {
}
