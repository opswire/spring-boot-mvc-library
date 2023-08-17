package com.opswire.springmvcpr1.dao;

import com.opswire.springmvcpr1.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class BookDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Book> findAll() {
        return jdbcTemplate.query(
                "SELECT * FROM books",
                (rs, rowNum) -> new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getInt("year_of_publication"),
                        rs.getInt("reader_id")
                )
        );
    }

    public Optional<Book> findById(int id) {
        return jdbcTemplate.query(
                "SELECT * FROM books WHERE id = ?",
                new Object[]{id},
                (rs, rowNum) -> new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getInt("year_of_publication"),
                        rs.getInt("reader_id")
                )
        ).stream().findFirst();
    }

    public List<Book> findByReaderId(int readerId) {
        return jdbcTemplate.query(
                "SELECT * FROM books WHERE reader_id = ?",
                new Object[]{readerId},
                (rs, rowNum) -> new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getInt("year_of_publication"),
                        rs.getInt("reader_id")
                )
        );
    }

    public void save(Book book) {
        jdbcTemplate.update(
                "INSERT INTO books(title, year_of_publication, author) VALUES (?, ?, ?)",
                book.getTitle(),
                book.getYearOfPublication(),
                book.getAuthor()
        );
    }

    public void update(Book book, int id) {
        jdbcTemplate.update(
                "UPDATE books SET title = ?, year_of_publication = ?, author = ?, reader_id = ? " +
                        "WHERE id = ?",
                book.getTitle(),
                book.getYearOfPublication(),
                book.getAuthor(),
                book.getReaderId() != 0 ? book.getReaderId() : null,
                id
        );
    }

    public void delete(int id) {
        jdbcTemplate.update(
                "DELETE FROM books WHERE id = ?",
                id
        );
    }

    public void assign(int id, int readerId) {
        jdbcTemplate.update(
                "UPDATE books SET reader_id = ? WHERE id = ?",
                readerId,
                id
        );
    }

    public void release(int id) {
        jdbcTemplate.update(
                "UPDATE books SET reader_id = NULL WHERE id = ?",
                id
        );
    }
}
