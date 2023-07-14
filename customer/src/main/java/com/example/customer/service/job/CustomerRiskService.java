package com.example.customer.service.job;

import com.example.customer.model.entity.CustomerEntity;
import com.example.customer.model.entity.NoteEntity;
import com.example.customer.service.utils.Utils;
import com.example.customer.model.dto.CustomerDto;
import com.example.customer.model.dto.NoteDto;
import com.example.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CustomerRiskService {

    private final CustomerRepository customerRepository;

    public CustomerDto getLatestCustomerData(Integer customerId) throws Exception {
        CustomerDto customerDto = new CustomerDto();
        CustomerEntity customerEntity = customerRepository.findFirstByCustomerIdOrderByInfoAsOfDateDesc(customerId);

        if (customerEntity == null)
            throw new Exception("User with id " + customerId + " not found.");

        BeanUtils.copyProperties(customerEntity, customerDto);
        customerDto.setCustomerStartDate(Utils.convertToLocalDateViaInstant(customerEntity.getCustomerStartDate()).atStartOfDay());
        customerDto.setInfoAsOfDate(Utils.convertToLocalDateViaInstant(customerEntity.getInfoAsOfDate()).atStartOfDay());

        return customerDto;
    }

    public CustomerEntity getCustomerById(Integer customerId) throws Exception {
        CustomerEntity customerEntity = customerRepository.findFirstByCustomerIdOrderByInfoAsOfDateDesc(customerId);

        if (customerEntity == null)
            throw new Exception("User with id " + customerId + " not found.");

        return customerEntity;
    }

    public void addCustomerNote(Integer customerId, NoteDto noteDto) throws Exception {
        CustomerEntity customerEntity = customerRepository.findFirstByCustomerIdOrderByInfoAsOfDateDesc(customerId);

        if (customerEntity == null)
            throw new Exception("User with id " + customerId + " not found.");

        NoteEntity note = new NoteEntity();
        note.setCustomerEntity(customerEntity);
        note.setType(noteDto.getType());
        note.setTitle(noteDto.getTitle());
        note.setContent(noteDto.getContent());
        note.setNoteStartDay(Utils.convertDateToString());

        customerEntity.addNote(note);

        customerRepository.save(customerEntity);
    }

    public List<NoteDto> getNotesByCustomerIdAndDateRange(Integer customerId, String since, String until) throws Exception {
        CustomerEntity customerEntity = customerRepository.findFirstByCustomerIdOrderByInfoAsOfDateDesc(customerId);
        List<NoteDto> noteDtoList = new ArrayList<>();

        if (customerEntity == null)
            throw new Exception("User with id " + customerId + " not found.");

        List<NoteEntity> noteEntityList = customerEntity.getNotes().stream()
                .filter(note -> note.getNoteStartDay().compareTo(since) >= 0 && note.getNoteStartDay().compareTo(until) <= 0)
                .toList();

        for (NoteEntity noteEntity : noteEntityList) {
            NoteDto noteDto = new NoteDto();
            noteDto.setCustomerId(customerId);
            BeanUtils.copyProperties(noteEntity, noteDto);
            noteDtoList.add(noteDto);
        }
        return noteDtoList;
    }

    public List<CustomerDto> findCustomerByIdAndDate(Integer customerId, Date date) {
        return customerRepository.findCustomerByIdAndDate(customerId, date).stream()
                .map(customerEntity -> {
                    CustomerDto customerDto = new CustomerDto();
                    BeanUtils.copyProperties(customerEntity, customerDto);
                    return customerDto;
                }).collect(Collectors.toList());
    }

    public boolean checkIfRiskHasChanged(Integer customerId) {
        boolean isChanged = false;
        List<CustomerEntity> customerEntities = customerRepository.findTop2ByCustomerIdOrderByInfoAsOfDateDesc(customerId);
        int sizeOfCustomers = customerEntities.size();
        if (sizeOfCustomers < 2 || customerEntities.get(0).getCustomerRiskClass().equals(customerEntities.get(1).getCustomerRiskClass()))
            return isChanged;
        return true;
    }
}
