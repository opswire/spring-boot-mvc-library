package com.opswire.springmvcpr1.dao;

import com.opswire.springmvcpr1.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PersonDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Person> findAll() {
        return jdbcTemplate.query(
                "SELECT * FROM people",
                new BeanPropertyRowMapper<>(Person.class)
        );
    }

    public Optional<Person> findById(int id) {
        return jdbcTemplate.query(
                "SELECT * FROM people WHERE id = ?",
                new Object[]{id},
                new BeanPropertyRowMapper<>(Person.class)
        ).stream().findFirst();
    }

    public void save(Person person) {
        jdbcTemplate.update(
                "INSERT INTO people(full_name, year_of_birthday) VALUES (?, ?)",
                person.getFullName(),
                person.getYearOfBirthday()
        );
    }

    public void update(Person person, int id) {
        jdbcTemplate.update(
                "UPDATE people SET full_name = ?, year_of_birthday = ? WHERE id = ?",
                person.getFullName(),
                person.getYearOfBirthday(),
                id
        );
    }

    public void delete(int id) {
        jdbcTemplate.update(
                "DELETE FROM people WHERE id = ?",
                id
        );
    }
}
