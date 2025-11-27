package com.beyondsales.beyondsales.security.service;

public interface EmailService {
    void sendSimpleMessage(String to, String subject, String text);
}
