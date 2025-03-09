package com.Notification.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Notification.DTO.EmailRequest;
import com.Notification.DTO.OtpRequest;
import com.Notification.service.EmailService;

@RestController
@RequestMapping("/notifications")
public class emailController {
	
	private static final Logger logger = LoggerFactory.getLogger(emailController.class);

	 @Autowired
	    EmailService emailService;

	    @PostMapping("/send-email")
	    public String sendEmail(@RequestBody EmailRequest emailRequest) {
	    	logger.info("Mail" + emailRequest.getTo()+"  "+ emailRequest.getSubject()+ "  "+ emailRequest.getText());
	        emailService.sendEmail(emailRequest.getTo(), emailRequest.getSubject(), emailRequest.getText());
	        return "Email sent successfully!";
	    }

	    @PostMapping("/send-otp")
	    public String sendOtp(@RequestBody OtpRequest otpRequest) {
	        String otp = emailService.sendOtp(otpRequest.getTo());
	        return "OTP sent successfully! Your OTP is: " + otp;
	    }
}
