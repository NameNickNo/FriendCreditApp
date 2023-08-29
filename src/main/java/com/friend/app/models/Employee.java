package com.friend.app.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Table(name = "employee")
@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employee_seq")
    @SequenceGenerator(name="employee_seq", sequenceName="employee_id_seq", allocationSize=1)
    private long id;

    @Column(name = "eeid")
    private String eeid;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "job_title")
    private String jobTitle;

    @Column(name = "department")
    private String department;

    @Column(name = "business_unit")
    private String businessUnit;

    @Column(name = "gender")
    private String gender;

    @Column(name = "ethnicity")
    private String ethnicity;

    @Column(name = "age")
    private int age;

    @Column(name = "hire_date")
    private LocalDate hireDate;

    @Column(name = "annual_salary")
    private long annualSalary;

    @Column(name = "bonus")
    private double bonus;

    @Column(name = "country")
    private String country;

    @Column(name = "city")
    private String city;

    @Column(name = "exit_date")
    private LocalDate exitDate;
}
