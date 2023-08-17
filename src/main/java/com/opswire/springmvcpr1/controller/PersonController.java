package com.opswire.springmvcpr1.controller;

import com.opswire.springmvcpr1.dao.BookDAO;
import com.opswire.springmvcpr1.dao.PersonDAO;
import com.opswire.springmvcpr1.model.Person;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/people")
public class PersonController {
    private final PersonDAO personDAO;
    private final BookDAO bookDAO;

    public PersonController(PersonDAO personDAO, BookDAO bookDAO) {
        this.personDAO = personDAO;
        this.bookDAO = bookDAO;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("people", personDAO.findAll());
        return "people/index";
    }

    @GetMapping("/{id}")
    public String show(Model model, @PathVariable int id) {
        Optional<Person> optionalPerson = personDAO.findById(id);
        if(optionalPerson.isEmpty()) {
            return "redirect:/books";
        }

        model.addAttribute("person", optionalPerson.get());
        model.addAttribute("books", bookDAO.findByReaderId(id));

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

        personDAO.save(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable int id, Model model) {
        Optional<Person> optionalPerson = personDAO.findById(id);
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
        personDAO.update(person, id);

        return "redirect:/people/" + id;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable int id) {
        personDAO.delete(id);
        return "redirect:/people";
    }
}
