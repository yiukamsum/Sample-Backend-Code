package com.example.demo.service;

import java.util.ArrayList;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepo;
import com.example.demo.util.Currency;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepo usersRepository;

    public ArrayList<User> all(){
        return usersRepository.findAll();
    }

    public User findByEmail(String email){
        ArrayList<User> list = usersRepository.findByEmail(email);
        if(list.size() > 0){
            return list.get(0);
        }
        return null;
    }

    public User findById(Long id){
        return usersRepository.findById(id).get();
    }

    public User save(User users){
        try {
            return usersRepository.saveAndFlush(users);
        } catch(Exception e) {
            System.out.println("----------------------------------------------");
            System.out.println(e);
            System.out.println("----------------------------------------------");
        }
        return null;
    }

    public User checkUserAfterLogin(User user){
        ArrayList<User> foundUserList = usersRepository.findByObjectguid(user.getObjectguid());
        if(foundUserList.size() > 0){
            User foundUser = foundUserList.get(0);
            foundUser.setName(user.getName());
            foundUser.setEmail(user.getEmail());
            foundUser.setUsername(user.getUsername());
            return usersRepository.save(foundUser);
        } else {
            User newOne = new User();
            newOne.setName(user.getName());
            newOne.setUsername(user.getUsername());
            newOne.setEmail(user.getEmail());
            newOne.setPassword("Unknown");
            newOne.setObjectguid(user.getObjectguid());
            newOne.setSalary_currency(Currency.HKD);
            return usersRepository.save(newOne);
        }
    }
}
