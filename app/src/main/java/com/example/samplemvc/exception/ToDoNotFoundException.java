package com.example.samplemvc.exception;

public class ToDoNotFoundException extends Exception {
    public ToDoNotFoundException(String message){
        super(message);
    }
}
