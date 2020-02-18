package com.spiralforge.udaan.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

import lombok.Data;

@Entity
@Data
@SequenceGenerator(name = "donationsequence", initialValue = 100000, allocationSize = 1)
public class Donation {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "donationsequence")
	private Long donationId;
	
	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@OneToOne
	@JoinColumn(name = "scheme_id")
	private Scheme scheme;
	private String paymentStatus;
}
