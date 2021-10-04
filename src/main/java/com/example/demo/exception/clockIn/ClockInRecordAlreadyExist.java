package com.example.demo.exception.clockIn;

public class ClockInRecordAlreadyExist extends Exception {
    public ClockInRecordAlreadyExist() {
        super("Clock In Record Already Exist");
    }
}
