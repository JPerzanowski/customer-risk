package com.example.customer.service.job;

import com.example.customer.model.entity.CustomerEntity;
import com.example.customer.repository.CustomerRepository;
import com.example.customer.service.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.*;
import java.text.ParseException;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class CustomerRiskCalculationJob {

    @Value("${app.file.location}")
    private String directoryPath;

    private final CalculateIndicatorsForTypeA1 calculateIndicatorsForTypeA1;

    private final CalculateIndicatorsForTypeA5AndType2 calculateIndicatorsForTypeA5AndType2;

    private final CustomerRepository customerRepository;

    @Scheduled(cron = "${app.job.cron-customer-risk-calculation}")
    public void customerRiskCalculationJob() throws Exception {
        importCsvFilesFromFolder(directoryPath);
        calculateIndicatorsForTypeA1.calculateAndSaveIndicatorsForTypeA1Customers();
        calculateIndicatorsForTypeA5AndType2.calculateAndSaveIndicatorsForCustomers();
    }


    private void importCsvFilesFromFolder(String folderPath) throws IOException {
        File folder = new File(folderPath);
        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().toLowerCase().endsWith(".csv")) {
                    importCsv(file.getPath());
                }
            }
        }
    }

    private void importCsv(String filePath) throws IOException {
        try (Reader reader = new FileReader(filePath);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader())) {

            for (CSVRecord csvRecord : csvParser) {
                Date infoAsOfDate = Utils.convertStringToDate(csvRecord.get("info_as_of_date"));
                Integer customerId = Integer.valueOf(csvRecord.get("customer_id"));
                String customerName = csvRecord.get("customer_name");
                Date customerStartDate = Utils.convertStringToDate(csvRecord.get("customer_start_date"));
                String customerType = csvRecord.get("customer_type");
                Double customerIncome = Double.valueOf(csvRecord.get("customer_income"));
                String customerRiskClass = csvRecord.get("customer_risk_class");
                String customerBusinessType = csvRecord.get("customer_business_type");

                CustomerEntity customerEntity = new CustomerEntity(infoAsOfDate, customerId, customerName,
                        customerStartDate, customerType, customerIncome, customerRiskClass, customerBusinessType);
                customerRepository.save(customerEntity);
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
