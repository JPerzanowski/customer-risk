package com.example.customer.repository;

import com.example.customer.model.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Integer> {
    List<CustomerEntity> findByCustomerType(String typeA1);
    @Query("select ce from CustomerEntity ce WHERE ce.customerType = ?1 AND ce.customerRiskClass = ?2")
    List<CustomerEntity> findByCustomerTypeAndRiskClass(String customerType, String customerRiskClass);
    @Query("select ce from CustomerEntity ce WHERE ce.customerType = ?1 AND ce.customerRiskClass <> ?2")
    List<CustomerEntity> findByCustomerTypeAndOtherRiskClass(String customerType, String customerRiskClass);

    @Query("select avg(ce.customerIncome) from CustomerEntity ce "
                    + "where ce.customerStartDate = CURRENT_DATE - 30 "
                    + "group by ce.customerId having ce.customerId = :customerId")
    List<CustomerEntity> findHistoricalIncomes(Integer customerId);

    CustomerEntity findFirstByCustomerIdOrderByInfoAsOfDateDesc(Integer customerId);
    @Query("select ce from CustomerEntity ce WHERE ce.customerId = ?1 AND ce.infoAsOfDate = ?2")
    List<CustomerEntity> findCustomerByIdAndDate(Integer customerId, Date date);

    List<CustomerEntity> findTop2ByCustomerIdOrderByInfoAsOfDateDesc(Integer customerId);
}
