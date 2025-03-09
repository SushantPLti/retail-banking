package com.rbanking.cservice.feing;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.rbanking.cservice.request.OtpRequest;

@Service
@FeignClient(name = "Notification")
public interface NotificationClient {
	
	@PostMapping("/send-otp")
    String sendOtp(@RequestBody OtpRequest otpRequest);

}