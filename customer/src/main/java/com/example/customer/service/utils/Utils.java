package com.example.customer.service.utils;

import lombok.experimental.UtilityClass;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@UtilityClass
public class Utils {

    public String convertDateToString() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date();
        return format.format(currentDate);
    }

    public Date convertStringToDate(String dateString) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.parse(dateString);
    }

    public LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
}
