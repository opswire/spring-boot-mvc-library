package com.opswire.springmvcpr1.service;

import com.opswire.springmvcpr1.model.Book;
import com.opswire.springmvcpr1.model.Person;
import com.opswire.springmvcpr1.repository.BookRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BookService {
    private final String SORT_BY_YEAR_FIELD = "yearOfPublication";

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> findAll(Boolean sortByYear) {
        if(sortByYear)
            return bookRepository.findAll(Sort.by(SORT_BY_YEAR_FIELD));

        return bookRepository.findAll();
    }

    public List<Book> findWithPagination(Integer page, Integer booksPerPage, Boolean sortByYear) {
        Pageable pageable;
        if(sortByYear)
            pageable = PageRequest.of(page, booksPerPage, Sort.by(SORT_BY_YEAR_FIELD));
        else
            pageable = PageRequest.of(page, booksPerPage);

        return bookRepository.findAll(pageable).getContent();
    }

    public Optional<Book> findById(int id) {
        return bookRepository.findById(id);
    }

    public List<Book> findByOwner(Person owner) {
        return bookRepository.findBooksByOwner(owner);
    }

    @Transactional
    public void save(Book book) {
        bookRepository.save(book);
    }

    @Transactional
    public void update(Book updatedBook, int id) {
        Book bookToBeUpdated = bookRepository.findById(id).get();

        updatedBook.setId(id);
        updatedBook.setOwner(bookToBeUpdated.getOwner());

        bookRepository.save(updatedBook);
    }

    @Transactional
    public void delete(int id) {
        bookRepository.deleteById(id);
    }

    @Transactional
    public void assign(int id, Person owner) {
        bookRepository.findById(id).ifPresent(book -> {
            book.setOwner(owner);
            book.setTakenAt(Timestamp.valueOf(LocalDateTime.now()));
        });
    }

    @Transactional
    public void release(int id) {
        bookRepository.findById(id).ifPresent(book -> {
            book.setOwner(null);
            book.setTakenAt(null);
        });
    }

    public List<Book> search(String request) {
        if(request != null)
            return bookRepository.findBooksByTitleStartingWith(request);
        return null;
    }
}
