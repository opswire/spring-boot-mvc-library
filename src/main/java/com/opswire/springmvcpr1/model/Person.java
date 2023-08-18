package com.opswire.springmvcpr1.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "people")
public class Person {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty
    @Column(name = "full_name")
    private String fullName;

    @Min(1900)
    @Max(2100)
    @Column(name = "year_of_birthday")
    private int yearOfBirthday;

    @OneToMany(mappedBy = "owner")
    private List<Book> books;

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

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
