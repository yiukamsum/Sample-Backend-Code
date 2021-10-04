package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.repository.ClockInRepo;
import com.example.demo.model.User;
import com.example.demo.exception.clockIn.ClockInRecordAlreadyExist;
import com.example.demo.exception.clockIn.ClockInRecordNotFound;
import com.example.demo.model.ClockIn;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

@Service
public class ClockInService {
    
    @Autowired
    private ClockInRepo clockInRepo;

    public ClockIn save(ClockIn clockIn){
        try {
            return clockInRepo.saveAndFlush(clockIn);
        } catch(Exception e) {
            System.out.println("----------------------------------------------");
            System.out.println(e);
            System.out.println("----------------------------------------------");
        }
        return null;
    }

    public ClockIn findByNotClockOutRecord(User staff){
        ArrayList<ClockIn> list = clockInRepo.findByNotClockOutRecord(staff.getId());
        if(list.size() > 0){
            return list.get(0);
        }
        return null;
    }

    public ClockIn start(User staff, String department, String type) throws ClockInRecordAlreadyExist{
        ClockIn clockIn = this.findByNotClockOutRecord(staff);
        if(clockIn != null){
            throw new ClockInRecordAlreadyExist();
        }
        clockIn = new ClockIn();
        clockIn.setDepartment(department);
        clockIn.setStaffId(staff.getId());
        clockIn.setSalary(staff.getSalary());
        clockIn.setType(ClockIn.Type.valueOf(type));
        clockIn.setSalaryCurrency(staff.getSalary_currency());
        clockIn.setStartTime(LocalDateTime.now());
        return this.save(clockIn);
    }

    public ClockIn end(User staff) throws ClockInRecordNotFound{
        ClockIn clockIn = this.findByNotClockOutRecord(staff);
        if(clockIn == null){
            throw new ClockInRecordNotFound();
        }
        LocalDateTime endTime = LocalDateTime.now();
        while(!clockIn.getStartTime().toLocalDate().equals(endTime.toLocalDate())){
            clockIn.setEndTime(LocalDateTime.of(clockIn.getStartTime().toLocalDate(), LocalTime.of(23, 59, 59)));
            clockIn = this.save(clockIn);
            clockIn.setId(null);
            clockIn.setStartTime(LocalDateTime.of(clockIn.getStartTime().toLocalDate().plusDays(1), LocalTime.of(0, 0, 0)));
        }
        clockIn.setEndTime(endTime);
        return this.save(clockIn);
    }

    public ArrayList<ClockIn> getClockInList(User staff, Integer year, Integer month){
        return clockInRepo.getClockInList(staff.getId(), year, month);
    }

    public ArrayList<ClockIn> getClockInList(Long staffId, Integer year, Integer month){
        return clockInRepo.getClockInList(staffId, year, month);
    }
}
