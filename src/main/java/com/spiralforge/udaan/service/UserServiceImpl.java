package com.spiralforge.udaan.service;

import java.util.Objects;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spiralforge.udaan.constants.ApiConstant;
import com.spiralforge.udaan.constants.ApplicationConstants;
import com.spiralforge.udaan.dto.PaymentRequestDto;
import com.spiralforge.udaan.dto.PaymentResponseDto;
import com.spiralforge.udaan.entity.Donation;
import com.spiralforge.udaan.entity.Scheme;
import com.spiralforge.udaan.entity.User;
import com.spiralforge.udaan.exception.DonationNotFoundException;
import com.spiralforge.udaan.exception.SchemeNotFoundException;
import com.spiralforge.udaan.exception.UserNotFoundException;
import com.spiralforge.udaan.helper.GeneratePdfReport;
import com.spiralforge.udaan.helper.MailService;
import com.spiralforge.udaan.repository.DonationRepository;
import com.spiralforge.udaan.repository.SchemeRepository;
import com.spiralforge.udaan.repository.UserRepository;
import com.spiralforge.udaan.util.Utility;

/**
 * @author Sri Keerthna.
 * @author Sujal
 * @since 2020-02-14.
 */
@Service
public class UserServiceImpl implements UserService {

	/**
	 * The Constant log.
	 */
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private SchemeRepository schemeRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private DonationRepository donationRepository;

	@Autowired
	private GeneratePdfReport generatePdfReport;

	@Autowired
	private MailService mailService;

	@Override
	public Optional<User> getUser(Long userId) {
		return userRepository.findById(userId);
	}

	/**
	 * @author Sri Keerthna.
	 * @since 2020-02-14. In this method user will give their details to make a
	 *        payment.
	 * @param paymentRequestDto has username,pan number,emailId and mobile number.
	 * @return success message is sent to user.
	 * @throws SchemeNotFoundException if no scheme found.
	 * @throws UserNotFoundException if user not found
	 */
	@Transactional
	public PaymentResponseDto charitablePayment(@Valid PaymentRequestDto paymentRequestDto)
			throws SchemeNotFoundException, UserNotFoundException {
		User user1=null;
		Optional<Scheme> scheme = schemeRepository.findById(paymentRequestDto.getSchemeId());
		if (!scheme.isPresent()) {
			logger.error("No scheme found");
			throw new SchemeNotFoundException(ApplicationConstants.SCHEME_NOT_FOUND_EXCEPTION);
		}
		User user = new User();
		PaymentResponseDto paymentResponseDto = new PaymentResponseDto();
		BeanUtils.copyProperties(paymentRequestDto, user);
		user.setUserStatus(ApplicationConstants.ACTIVE_STATUS);
		user1=userRepository.save(user);
		saveDonationData(user1, scheme.get());
		BeanUtils.copyProperties(paymentRequestDto, paymentResponseDto);
		BeanUtils.copyProperties(scheme.get(), paymentResponseDto);
		paymentResponseDto.setUserId(user.getUserId());
		Double taxBenefitAmount=Utility.calculateCharges(scheme.get().getSchemeAmount(), scheme.get().getTaxBenefit());
		paymentResponseDto.setTaxBenefit(taxBenefitAmount);
		paymentResponseDto.setMessage(ApiConstant.PAYMENT_SUCCESS);
		paymentResponseDto.setStatusCode(ApiConstant.SUCCESS_CODE);
		logger.info("Payment Success");
		return paymentResponseDto;
	}

	public void saveDonationData(User user1, Scheme scheme) {
		Donation donation= new Donation();
		donation.setPaymentStatus(ApplicationConstants.SUCCESS_STATUS);
		donation.setScheme(scheme);
		donation.setUser(user1);
		donationRepository.save(donation);
	}

	/**
	 * @author Sujal.
	 * @since 2020-02-14. In this method user will download the donation detail
	 *        after payment.
	 * @param userId is user Id after saving donation detail.
	 * @return byte[] for download pdf.
	 * @throws UserNotFoundException if no user found.
	 * @throws DonationNotFoundException 
	 */
	@Override
	public byte[] download(Long userId) throws UserNotFoundException, DonationNotFoundException {
		Optional<User> user = getUser(userId);
		if (!user.isPresent()) {
			throw new UserNotFoundException(ApiConstant.USER_NOT_FOUND);
		} else {
			logger.info("generate pdf file");
			Donation donation = donationRepository.findByUser(user.get());
			if (Objects.isNull(donation)) {
				throw new DonationNotFoundException(ApiConstant.DONATION_NOTFOUND_MESSAGE);

			} else {
				logger.info("inside generate pdf:::::");

				return generatePdfReport.generatePdf(user.get(), donation);
			}
		}
	}

	/**
	 * @author Sujal.
	 * @since 2020-02-14. In this method user will download the donation detail
	 *        after payment and send send the mail with pdf attachment.
	 * @param userId is user Id after saving donation detail.
	 * @return byte[] for download pdf.
	 * @throws UserNotFoundException     if no user found.
	 * @throws DonationNotFoundException
	 */
	@Override
	public byte[] sendPDFInMail(Long userId) throws UserNotFoundException, DonationNotFoundException {

		Optional<User> user = getUser(userId);
		if (!user.isPresent()) {
			throw new UserNotFoundException(ApiConstant.USER_NOT_FOUND);
		} else {
			logger.info("generating the pdf file in mail");

			Donation donation = donationRepository.findByUser(user.get());
			if (Objects.isNull(donation)) {
				throw new DonationNotFoundException(ApiConstant.DONATION_NOTFOUND_MESSAGE);

			} else {
				byte[] byteData = generatePdfReport.generatePdf(user.get(), donation);
				if (!Objects.isNull(byteData) && byteData.length > 0) {
					logger.info("sending the pdf file in mail");
					mailService.sendMail(user.get().getEmailId(), ApiConstant.MAIL_SUBJECT,
							Utility.getContent(user.get().getUserName(), donation.getScheme().getSchemeAmount()),
							byteData);
				}
				return byteData;
			}
		}
	}
}
