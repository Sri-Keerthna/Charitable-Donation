package com.spiralforge.udaan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spiralforge.udaan.entity.Donation;
import com.spiralforge.udaan.entity.User;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Long> {

	Donation findByUser(User user);

}
