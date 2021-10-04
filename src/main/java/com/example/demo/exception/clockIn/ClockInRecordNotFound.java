package com.example.demo.exception.clockIn;

public class ClockInRecordNotFound extends Exception {
    public ClockInRecordNotFound() {
        super("Cannot found clock in record");
    }
    
}
