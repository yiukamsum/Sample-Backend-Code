package com.example.demo.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.example.demo.util.Currency;
import com.fasterxml.jackson.annotation.JsonFormat;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "users")
public class User {

    public enum Role {
        admin, secretary, manager, staff;
    }

    public enum Type {
        fulltime, parttime, internship;
    }

    public enum Status {
        leave, current, hold, upcoming;
    }

    public enum Gender {
        M, F, O;
    }
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime email_verified_at;
    @Column(nullable = false)
    private String password;
    @Column(nullable = true)
    private String remember_token;
    @Column(nullable = true)
    private Float salary;
    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Role role;
    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Type type;
    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Status status;
    @Column(nullable = true)
    private String department;
    @Column(nullable = true)
    private Long position;
    @Column(nullable = true)
    private String objectguid;
    @CreationTimestamp
    private Timestamp created_at;
    @UpdateTimestamp
    private Timestamp updated_at;
    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Currency salary_currency;
    @Column(name = "identitynumber", nullable = true)
    private String identityNumber;
    @Column(nullable = true)
    private String card_id;
    @Column(nullable = true)
    private String emgerency_person;
    @Column(nullable = true)
    private String emgerency_contact;
    @Column(nullable = true)
    private String address;
    @Column(nullable = true)
    private String nationality;
    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Column(nullable = true)
    private String phone;
    @Column(name = "dob", nullable = true)
    private Date dob;
    @Column(nullable = true)
    private String marital_status;
    @Column(nullable = true)
    private String contact_email;
    @Column(nullable = true)
    private String bank_name;
    @Column(nullable = true)
    private String bank_account_number;

    public Float getSalary(){
        if(this.salary == null){
            return 0F;
        }
        return this.salary;
    }
}