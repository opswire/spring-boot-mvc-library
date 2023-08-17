package com.opswire.springmvcpr1.controller;

import com.opswire.springmvcpr1.dao.BookDAO;
import com.opswire.springmvcpr1.dao.PersonDAO;
import com.opswire.springmvcpr1.model.Book;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BookController {
    private final BookDAO bookDAO;
    private final PersonDAO personDAO;

    @Autowired
    public BookController(BookDAO bookDAO, PersonDAO personDAO) {
        this.bookDAO = bookDAO;
        this.personDAO = personDAO;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("books", bookDAO.findAll());
        return "books/index";
    }

    @GetMapping("/{id}")
    public String show(Model model, @PathVariable int id) {
        Optional<Book> optionalBook = bookDAO.findById(id);
        if(optionalBook.isEmpty()) {
            return "redirect:/books";
        }

        Book book = optionalBook.get();

        model.addAttribute("book", book);
        model.addAttribute("people", personDAO.findAll());

        int readerId = book.getReaderId();
        if (readerId != 0) {
            model.addAttribute("reader", personDAO.findById(readerId).get());
        }

        return "books/show";
    }

    @GetMapping("/new")
    public String create(Model model) {
        model.addAttribute("book", new Book());
        return "books/new";
    }

    @PostMapping()
    public String add(
            @ModelAttribute("book") @Valid Book book,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "books/new";
        }

        bookDAO.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable int id, Model model) {
        Optional<Book> optionalBook = bookDAO.findById(id);
        if(optionalBook.isEmpty()) {
            return "redirect:/books/" + id;
        }

        model.addAttribute("book", optionalBook.get());
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(
            @PathVariable int id,
            @ModelAttribute("book") @Valid Book book,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "books/edit";
        }
        bookDAO.update(book, id);

        return "redirect:/books/" + id;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable int id) {
        bookDAO.delete(id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable int id, @ModelAttribute Book book) {
        bookDAO.assign(id, book.getReaderId());
        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/release")
    public String release(@PathVariable int id, @ModelAttribute Book book) {
        bookDAO.release(id);
        return "redirect:/books/" + id;
    }
}
