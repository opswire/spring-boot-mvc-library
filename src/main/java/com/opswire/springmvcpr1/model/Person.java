package com.opswire.springmvcpr1.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class Person {

    private int id;

    @NotEmpty
    private String fullName;

    @Min(1900)
    @Max(2100)
    private int yearOfBirthday;

    public Person() {
    }

    public Person(int id, String fullName, int yearOfBirthday) {
        this.id = id;
        this.fullName = fullName;
        this.yearOfBirthday = yearOfBirthday;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getYearOfBirthday() {
        return yearOfBirthday;
    }

    public void setYearOfBirthday(int yearOfBirthday) {
        this.yearOfBirthday = yearOfBirthday;
    }
}
