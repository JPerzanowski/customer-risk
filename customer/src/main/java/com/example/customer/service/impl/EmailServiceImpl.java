package com.example.customer.service.impl;

import com.example.customer.model.entity.CustomerEntity;
import com.example.customer.service.NotificationService;
import com.example.customer.service.job.CustomerRiskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class EmailServiceImpl implements NotificationService {
    @Value("${email.address.directors}")
    private String emailAddressToDirectors;
    @Value("${email.address.coordinators}")
    private String emailAddressToCoordinators;
    @Value("${email.message.typeA5-type1}")
    private String emailMessageTypeA5AndType1;
    @Value("${email.message.type2}")
    private String emailMessageType2;

    private final CustomerRiskService customerAndNoteService;

    public void sendRiskClassChangeNotification(Integer customerId) throws Exception {
        CustomerEntity customer = customerAndNoteService.getCustomerById(customerId);
        if (customerAndNoteService.checkIfRiskHasChanged(customerId)) {
            if (customer.getCustomerType().equals("TYPE_1") || customer.getCustomerType().equals("TYPE_A5")) {
                sendMessageToDirectors(customerId);
            } else if (customer.getCustomerType().equals("TYPE_2") && customer.getCustomerBusinessType().equals("BR2")) {
                sendMessageToCoordinators(customerId);
            }
        }
    }

    @Override
    public void sendMessage(String recipient, String content) {
        log.info("Sending email to: {}", recipient);
        log.info("Email content: {}", content);
    }

    @Override
    public void sendMessageToDirectors(Integer customerId) {
        String emailContent = emailMessageTypeA5AndType1 + " " + customerId;
        sendMessage(emailAddressToDirectors, emailContent);
        sendMessage(emailAddressToCoordinators, emailContent);
    }

    @Override
    public void sendMessageToCoordinators(Integer customerId) {
        String emailContent = emailMessageType2 + " " + customerId;
        sendMessage(emailAddressToCoordinators, emailContent);
    }
}
