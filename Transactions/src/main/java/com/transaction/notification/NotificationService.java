package com.transaction.notification;

import org.springframework.stereotype.Service;

@Service
public interface NotificationService {

	String sendEmail(String to, String subject, String text);

}
