package com.transaction.notification;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notificationClient", url = "http://localhost:7070/notifications")
public interface NotificationClient {

    @PostMapping("/send-email")
    String sendEmail(@RequestBody EmailRequest emailRequest);
}
