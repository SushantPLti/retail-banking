package com.Notification.DTO;

public class EmailRequest {
    private String to;
    private String subject;
    private String text;
    
	public final String getTo() {
		return to;
	}
	public final void setTo(String to) {
		this.to = to;
	}
	public final String getSubject() {
		return subject;
	}
	public final void setSubject(String subject) {
		this.subject = subject;
	}
	public final String getText() {
		return text;
	}
	public final void setText(String text) {
		this.text = text;
	}

}