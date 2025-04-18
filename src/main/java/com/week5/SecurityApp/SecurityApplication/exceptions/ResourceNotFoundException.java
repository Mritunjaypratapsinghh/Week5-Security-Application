package com.week5.SecurityApp.SecurityApplication.exceptions;


public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message){
        super(message);
    }

}
