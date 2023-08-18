package com.opswire.springmvcpr1.service;

import com.opswire.springmvcpr1.model.Book;
import com.opswire.springmvcpr1.model.Person;
import com.opswire.springmvcpr1.repository.PersonRepository;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PersonService {
    private final long TEN_DAYS = 864000000;

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<Person> findAll() {
        return personRepository.findAll();
    }

    public Optional<Person> findById(int id) {
        Optional<Person> optionalOwner = personRepository.findById(id);

        optionalOwner.ifPresent(person -> {
            Hibernate.initialize(person.getBooks());
            checkIsExpired(person);
        });

        return optionalOwner;
    }

    @Transactional
    public void save(Person person) {
        personRepository.save(person);
    }

    @Transactional
    public void update(Person updatedPerson, int id) {
        updatedPerson.setId(id);
        personRepository.save(updatedPerson);
    }

    @Transactional
    public void delete(int id) {
        personRepository.deleteById(id);
    }

    private void checkIsExpired(Person person) {
        for (Book book : person.getBooks()) {
            long diff = new Date().getTime() - book.getTakenAt().getTime();
            if(diff > TEN_DAYS)
                book.setIsExpired(true);
        }
    }
}
