package com.week5.SecurityApp.SecurityApplication.advice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ApiError {

    private HttpStatus httpStatus;
    private String message;
    private List<String> suberrors;

}
