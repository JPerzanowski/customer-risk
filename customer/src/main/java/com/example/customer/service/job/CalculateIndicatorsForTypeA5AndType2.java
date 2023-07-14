package com.example.customer.service.job;

import com.example.customer.model.entity.CustomerEntity;
import com.example.customer.repository.CustomerRepository;
import com.example.customer.service.impl.EmailServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CalculateIndicatorsForTypeA5AndType2 {

    private final CustomerRepository customerRepository;
    private final EmailServiceImpl emailService;

    public void calculateAndSaveIndicatorsForCustomers() throws Exception {
        List<CustomerEntity> typeA5CustomersWithRiskClassA3 = customerRepository
                .findByCustomerTypeAndRiskClass("TYPE_A5", "A3");
        List<CustomerEntity> typeA5CustomersWithOtherRiskClass = customerRepository
                .findByCustomerTypeAndOtherRiskClass("TYPE_A5", "A3");
        List<CustomerEntity> typeA2Customers = customerRepository.findByCustomerType("TYPE_A2");

        calculateAndSaveIndicatorsForTypeA5CustomersWithRiskClassA3(typeA5CustomersWithRiskClassA3);
        calculateAndSaveIndicatorsForTypeA5CustomersWithOtherRiskClass(typeA5CustomersWithOtherRiskClass);
        calculateAndSaveIndicatorsForTypeA2Customers(typeA2Customers);
    }

    private void calculateAndSaveIndicatorsForTypeA5CustomersWithRiskClassA3(List<CustomerEntity> customerEntities) throws Exception {
        for (CustomerEntity customerEntity : customerEntities) {
            double r1 = (customerEntity.getCustomerIncome() / 10) * calculateF1(customerEntity.getCustomerBusinessType());
            double r2 = customerEntity.getCustomerIncome() / 100;
            customerEntity.setR1(r1);
            customerEntity.setR2(r2);
            emailService.sendRiskClassChangeNotification(customerEntity.getCustomerId());
            customerRepository.save(customerEntity);
        }
    }

    private void calculateAndSaveIndicatorsForTypeA5CustomersWithOtherRiskClass(List<CustomerEntity> customerEntities) throws Exception {
        for (CustomerEntity customerEntity : customerEntities) {
            double r1 = customerEntity.getCustomerIncome() / 10;
            double r2 = customerEntity.getCustomerIncome() / 100;
            customerEntity.setR1(r1);
            customerEntity.setR2(r2);
            emailService.sendRiskClassChangeNotification(customerEntity.getCustomerId());
            customerRepository.save(customerEntity);
        }
    }

    private void calculateAndSaveIndicatorsForTypeA2Customers(List<CustomerEntity> customerEntities) throws Exception {
        for (CustomerEntity customerEntity : customerEntities) {
            double r1 = customerEntity.getCustomerIncome() / 10;
            double r2 = customerEntity.getCustomerIncome() / 100;
            customerEntity.setR1(r1);
            customerEntity.setR2(r2);
            emailService.sendRiskClassChangeNotification(customerEntity.getCustomerId());
            customerRepository.save(customerEntity);
        }
    }

    private double calculateF1(String customerBusinessType) {
        if (customerBusinessType.equals("BR_3")) {
            return 0.1;
        } else {
            return 0.2;
        }
    }

}
