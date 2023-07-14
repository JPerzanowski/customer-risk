package com.example.customer.service;

import org.springframework.stereotype.Service;

@Service
public interface NotificationService {

    void sendMessage(String recipient, String content);

    void sendMessageToDirectors(Integer customerId);

    void sendMessageToCoordinators(Integer customerId);

}
