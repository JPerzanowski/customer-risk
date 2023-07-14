package com.example.customer.controller;

import com.example.customer.service.job.CustomerRiskService;
import com.example.customer.model.dto.CustomerDto;
import com.example.customer.model.dto.NoteDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class CustomerRiskNotifierController {

    private final CustomerRiskService customerRiskService;

    @GetMapping("/api/customer/{customer_id}")
    public ResponseEntity<CustomerDto> getCustomer(@PathVariable("customer_id") Integer customerId) throws Exception {
        return ResponseEntity.ok(customerRiskService.getLatestCustomerData(customerId));
    }

    @GetMapping("/api/customer/{customer_id}/date")
    public List<CustomerDto> getCustomer(@PathVariable("customer_id") Integer customerId,
                                   @PathVariable("date") Date date) {
        return ResponseEntity.ok(customerRiskService.findCustomerByIdAndDate(customerId, date)).getBody();
    }

    @PostMapping("/api/customer/{customer_id}/note")
    public ResponseEntity<String> addCustomerNote(@PathVariable("customer_id") Integer customerId, @RequestBody NoteDto noteDto) throws Exception {
        customerRiskService.addCustomerNote(customerId, noteDto);
        return ResponseEntity.ok("Note added successfully");
    }

    @GetMapping("/api/customer/{customerId}/note")
    public List<NoteDto> getNotesByCustomerIdAndDate(
            @PathVariable("customerId") Integer customerId,
            @RequestParam("since") String since,
            @RequestParam("until") String until
    ) throws Exception {
        return customerRiskService.getNotesByCustomerIdAndDateRange(customerId, since, until);
    }
}
