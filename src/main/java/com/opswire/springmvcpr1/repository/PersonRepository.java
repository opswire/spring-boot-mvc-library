package com.opswire.springmvcpr1.repository;

import com.opswire.springmvcpr1.model.Person;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer>{
}
