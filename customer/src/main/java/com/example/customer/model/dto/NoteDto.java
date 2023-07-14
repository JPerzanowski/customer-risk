package com.example.customer.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoteDto {

    private Integer customerId;
    private Type type;
    private String title;
    private String content;
}
