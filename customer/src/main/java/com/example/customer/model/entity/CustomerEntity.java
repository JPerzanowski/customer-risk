package com.example.customer.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "customer_entity")
public class CustomerEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "info_as_of_date")
    private Date infoAsOfDate;

    @Column(name = "customer_id")
    private Integer customerId;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "customer_start_date")
    private Date customerStartDate;

    @Column(name = "customer_type")
    private String customerType;

    @Column(name = "customer_income")
    private Double customerIncome;

    @Column(name = "customer_risk_class")
    private String customerRiskClass;

    @Column(name = "customer_business_type")
    private String customerBusinessType;

    @Column(name = "r_1")
    private Double r1;

    @Column(name = "r_2")
    private Double r2;

    @OneToMany(mappedBy = "customerEntity", cascade = CascadeType.ALL)
    private List<NoteEntity> notes = new ArrayList<>();

    public void addNote(NoteEntity note) {
        notes.add(note);
        note.setCustomerEntity(this);
    }

    public CustomerEntity(Date infoAsOfDate, Integer customerId, String customerName, Date customerStartDate,
                          String customerType, Double customerIncome, String customerRiskClass,
                          String customerBusinessType, Double r1, Double r2) {
        this.infoAsOfDate = infoAsOfDate;
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerStartDate = customerStartDate;
        this.customerType = customerType;
        this.customerIncome = customerIncome;
        this.customerRiskClass = customerRiskClass;
        this.customerBusinessType = customerBusinessType;
        this.r1 = r1;
        this.r2 = r2;
    }

    public CustomerEntity(Date infoAsOfDate, Integer customerId, String customerName, Date customerStartDate,
                          String customerType, Double customerIncome, String customerRiskClass,
                          String customerBusinessType) {
        this.infoAsOfDate = infoAsOfDate;
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerStartDate = customerStartDate;
        this.customerType = customerType;
        this.customerIncome = customerIncome;
        this.customerRiskClass = customerRiskClass;
        this.customerBusinessType = customerBusinessType;
    }
}
