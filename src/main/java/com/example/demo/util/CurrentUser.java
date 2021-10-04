package com.example.demo.util;

import com.example.demo.model.User;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
public class CurrentUser {
    private User currentUser;
}
