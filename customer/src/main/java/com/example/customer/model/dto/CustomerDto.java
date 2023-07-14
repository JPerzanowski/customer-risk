package com.example.customer.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CustomerDto {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime infoAsOfDate;
    private Integer customerId;
    private String customerName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime customerStartDate;
    private String customerType;
    private Double customerIncome;
    private String customerRiskClass;
    private String customerBusinessType;
    private Double r1;
    private Double r2;
}
