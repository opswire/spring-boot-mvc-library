package com.opswire.springmvcpr1.controller;

import com.opswire.springmvcpr1.model.Person;
import com.opswire.springmvcpr1.service.BookService;
import com.opswire.springmvcpr1.service.PersonService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/people")
public class PersonController {
    private final PersonService personService;
    private final BookService bookService;

    public PersonController(PersonService personService, BookService bookService) {
        this.personService = personService;
        this.bookService = bookService;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("people", personService.findAll());
        return "people/index";
    }

    @GetMapping("/{id}")
    public String show(Model model, @PathVariable int id) {
        Optional<Person> optionalPerson = personService.findById(id);
        if(optionalPerson.isEmpty()) {
            return "redirect:/books";
        }

        model.addAttribute("person", optionalPerson.get());
        model.addAttribute("books", bookService.findByOwner(optionalPerson.get()));

        return "people/show";
    }

    @GetMapping("/new")
    public String create(Model model) {
        model.addAttribute("person", new Person());
        return "people/new";
    }

    @PostMapping()
    public String add(
            @ModelAttribute("person") @Valid Person person,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "people/new";
        }

        personService.save(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable int id, Model model) {
        Optional<Person> optionalPerson = personService.findById(id);
        if(optionalPerson.isEmpty()) {
            return "redirect:/books/" + id;
        }

        model.addAttribute("person", optionalPerson);
        return "people/edit";
    }

    @PatchMapping("/{id}")
    public String update(
            @PathVariable int id,
            @ModelAttribute("person") @Valid Person person,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "people/edit";
        }
        personService.update(person, id);

        return "redirect:/people/" + id;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable int id) {
        personService.delete(id);
        return "redirect:/people";
    }
}
