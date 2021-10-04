package com.example.demo.controller;

import java.time.LocalDate;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.exception.clockIn.ClockInRecordAlreadyExist;
import com.example.demo.exception.clockIn.ClockInRecordNotFound;
import com.example.demo.model.ClockIn;
import com.example.demo.service.ClockInService;
import com.example.demo.service.UserService;
import com.example.demo.util.CurrentUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;


@RestController
public class HomeController {

	@Autowired
	UserService usersService;

	@Autowired
	ClockInService clockInService;

    @Autowired
    CurrentUser currentUser;
    

    @RequestMapping("/hello")
    public ResponseEntity<String> index(){
        return new ResponseEntity<String>("Hello", HttpStatus.OK);
    }

    @RequestMapping("/clock_in/status")
    public ResponseEntity<JSONObject> clockInStatus(){
        ClockIn clockIn = clockInService.findByNotClockOutRecord(currentUser.getCurrentUser());
        JSONObject output = new JSONObject();
        if(clockIn != null){
            output.put("status", "success");
            output.put("data", clockIn);
            return new ResponseEntity<JSONObject>(output, HttpStatus.OK);
        } else {
            output.put("status", "success");
            output.put("data", null);
            return new ResponseEntity<JSONObject>(output, HttpStatus.OK);
        }
    }

    @RequestMapping("/clock_in/start")
    public ResponseEntity<JSONObject> clockInStart(){
        JSONObject output = new JSONObject();
        try {
            output.put("status", "success");
            output.put("data", clockInService.start(currentUser.getCurrentUser(), "Home", "Home"));
        } catch (ClockInRecordAlreadyExist e){
            output.put("status", "error");
            output.put("message", "Cannot clock in in this moment.");
        }
        return new ResponseEntity<JSONObject>(output, HttpStatus.OK);
    }

    @RequestMapping("/clock_in/end")
    public ResponseEntity<JSONObject> clockInEnd(){
        JSONObject output = new JSONObject();
        try {
            output.put("status", "success");
            output.put("data", clockInService.end(currentUser.getCurrentUser()));
        } catch (ClockInRecordNotFound e){
            output.put("status", "error");
            output.put("message", "There is no clock in record to end.");
        }
        return new ResponseEntity<JSONObject>(output, HttpStatus.OK);
    }


    @RequestMapping("/clock_in/record")
    public RedirectView clockInRecord(
        RedirectAttributes redirectAttributes,
        @RequestParam(value = "user_id", required = false) String userIdStr
    ){
        RedirectView redirectTarget = new RedirectView();
        redirectTarget.setContextRelative(true);
        LocalDate now = LocalDate.now();
        redirectTarget.setUrl(String.format("/clock_in/record/%d/%d", now.getYear(), now.getMonthValue()));
        redirectAttributes.addAttribute("user_id", userIdStr);
        return redirectTarget;
    }


    @RequestMapping("/clock_in/record/{year}/{month}")
    public ResponseEntity<JSONObject> clockInRecord(
        @PathVariable(value = "year") String yearStr,
        @PathVariable(value = "month") String monthStr,
        @RequestParam(value = "user_id", required = false) String userIdStr
    ){
        JSONObject output = new JSONObject();
        Integer year = Integer.valueOf(yearStr);
        Integer month = Integer.valueOf(monthStr);
        Long userId;
        if(year < 1 || year > 9999 || month < 1 || month > 12){
            output.put("status", "error");
            output.put("status", "Year or Month invalid");
            return new ResponseEntity<JSONObject>(output, HttpStatus.BAD_REQUEST);
        }
        if(userIdStr == null){ // TODO: or he/she no permission
            userId = currentUser.getCurrentUser().getId();
        } else {
            userId = Long.valueOf(userIdStr);
        }
        System.out.println(yearStr);
        System.out.println(monthStr);
        System.out.println(userIdStr);
        try {
            output.put("status", "success");
            output.put("data", clockInService.getClockInList(userId, year, month));
        } catch (Exception e){
            output.put("status", "error");
            // output.put("message", "There is no clock in record to end.");
        }
        return new ResponseEntity<JSONObject>(output, HttpStatus.OK);
    }
}
