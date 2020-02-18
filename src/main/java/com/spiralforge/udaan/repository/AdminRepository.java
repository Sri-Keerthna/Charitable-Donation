package com.spiralforge.udaan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spiralforge.udaan.entity.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

	Admin findByMobileNumberAndPassword(Long mobileNumber, String password);

}
