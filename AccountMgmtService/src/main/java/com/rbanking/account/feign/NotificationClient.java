package com.rbanking.account.feign;

import com.rbanking.account.model.EmailRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@FeignClient(name = "Notification", url = "localhost:7070/notifications")
public interface NotificationClient {
	
	@PostMapping("/send-email")
    String sendEmail(@RequestBody EmailRequest emailRequest);

}