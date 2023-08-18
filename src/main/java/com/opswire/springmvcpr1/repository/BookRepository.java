package com.opswire.springmvcpr1.repository;

import com.opswire.springmvcpr1.model.Book;
import com.opswire.springmvcpr1.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    public List<Book> findBooksByOwner(Person owner);

    public List<Book> findBooksByTitleStartingWith(String prefix);
}
