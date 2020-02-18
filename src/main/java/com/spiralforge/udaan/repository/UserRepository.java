package com.spiralforge.udaan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spiralforge.udaan.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
