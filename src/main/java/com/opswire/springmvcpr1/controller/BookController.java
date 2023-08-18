package com.opswire.springmvcpr1.controller;

import com.opswire.springmvcpr1.model.Book;
import com.opswire.springmvcpr1.model.Person;
import com.opswire.springmvcpr1.service.BookService;
import com.opswire.springmvcpr1.service.PersonService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;
    private final PersonService personService;

    @Autowired
    public BookController(BookService bookService, PersonService personService) {
        this.bookService = bookService;
        this.personService = personService;
    }

    @GetMapping()
    public String index(
            Model model,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "books_per_page", required = false) Integer booksPerPage,
            @RequestParam(value = "sort_by_year", required = false) boolean sortByYear
    ) {
        if (page == null || booksPerPage == null)
            model.addAttribute("books", bookService.findAll(sortByYear));
        else
            model.addAttribute("books", bookService.findWithPagination(page, booksPerPage, sortByYear));

        return "books/index";
    }

    @GetMapping("/{id}")
    public String show(Model model, @PathVariable int id) {
        Optional<Book> optionalBook = bookService.findById(id);
        if(optionalBook.isEmpty()) {
            return "redirect:/books";
        }

        Book book = optionalBook.get();

        model.addAttribute("book", book);
        model.addAttribute("people", personService.findAll());

        Person owner = book.getOwner();
        if (owner != null) {
            model.addAttribute("reader", owner);
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

        bookService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable int id, Model model) {
        Optional<Book> optionalBook = bookService.findById(id);
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
        bookService.update(book, id);

        return "redirect:/books/" + id;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable int id) {
        bookService.delete(id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable int id, @ModelAttribute Book book) {
        bookService.assign(id, book.getOwner());
        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/release")
    public String release(@PathVariable int id, @ModelAttribute Book book) {
        bookService.release(id);
        return "redirect:/books/" + id;
    }

    @GetMapping("/search")
    public String search(
            Model model,
            @RequestParam(value = "request", required = false) String request
    ) {
        model.addAttribute("response", bookService.search(request));
        return "books/search";
    }
}
