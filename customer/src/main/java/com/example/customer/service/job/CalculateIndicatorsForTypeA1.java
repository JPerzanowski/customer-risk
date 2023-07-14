package com.example.customer.service.job;

import com.example.customer.model.entity.CustomerEntity;
import com.example.customer.repository.CustomerRepository;
import com.example.customer.service.impl.EmailServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CalculateIndicatorsForTypeA1 {
    private final CustomerRepository customerRepository;
    private final EmailServiceImpl emailService;

    public void calculateAndSaveIndicatorsForTypeA1Customers() throws Exception {
        List<CustomerEntity> typeA1Customers = customerRepository.findByCustomerType("TYPE_A1");
        for (CustomerEntity customerEntity : typeA1Customers) {
            double r1 = calculateR1(customerEntity);
            double r2 = calculateR2(customerEntity);
            customerEntity.setR1(r1);
            customerEntity.setR2(r2);
            emailService.sendRiskClassChangeNotification(customerEntity.getCustomerId());
            customerRepository.save(customerEntity);
        }
    }

    private double calculateR1(CustomerEntity customerEntity) {
        double customerIncome = customerEntity.getCustomerIncome();
        double f1 = calculateF1(customerEntity);
        return (customerIncome / 10) * f1;
    }

    private double calculateF1(CustomerEntity customerEntity) {
        List<CustomerEntity> historicalIncomes = customerRepository.findHistoricalIncomes(customerEntity.getCustomerId());
        double currentIncome = customerEntity.getCustomerIncome();
        double averageIncome = calculateAverageIncome(historicalIncomes);

        if (historicalIncomes.isEmpty()) {
            return 0.25;
        } else if (averageIncome < 0.8 * currentIncome) {
            return 0.3;
        } else {
            return 0.2;
        }
    }

    private double calculateAverageIncome(List<CustomerEntity> historicalIncomes) {
        double sum = 0;
        for (CustomerEntity customerDetails : historicalIncomes) {
            sum += customerDetails.getCustomerIncome();
        }
        return sum / historicalIncomes.size();
    }

    private double calculateR2(CustomerEntity customerEntity) {
        double customerIncome = customerEntity.getCustomerIncome();
        double f2 = calculateF2(customerEntity.getCustomerBusinessType());
        return (customerIncome / 1000) * f2;
    }

    private double calculateF2(String customerBusinessType) {
        if (customerBusinessType.equals("BT_1") || customerBusinessType.equals("BR_2")) {
            return 0.5;
        } else if (customerBusinessType.equals("BR_3")) {
            return 0.8;
        } else {
            return 0.9;
        }
    }
}


