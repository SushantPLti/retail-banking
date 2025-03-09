package com.transaction.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationClient notificationClient;

    @Override
    public String sendEmail(String to, String subject, String text) {
        EmailRequest emailRequest = new EmailRequest(to, subject, text);
        return notificationClient.sendEmail(emailRequest);
    }
}
