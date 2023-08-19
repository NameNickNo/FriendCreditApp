package com.friend.app.controllers.soap;

import com.friend.app.dto.DebtDTO;
import com.friend.app.models.person.Person;
import com.friend.app.service.PersonService;
import com.friend.app.setting.HibernateQualifier;
import com.friend.app.util.exception.PersonNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final PersonService personService;

    public AdminController(@HibernateQualifier PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/{id}")
    public String getPersonalPage(@PathVariable("id") long id, Model model) {
        Person person = personService.findById(id).orElse(null);
        if (person == null)
            throw new PersonNotFoundException("Person with id: " + id + " not found");

        model.addAttribute("person", person);
        model.addAttribute("debtDTO", new DebtDTO(person.getUsername()));
        return "person/personalPage";
    }

    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable("id") long id, Model model){
        Person person = personService.findById(id).orElse(null);
        if (person == null)
            throw new PersonNotFoundException("Person with id: " + id + " not found");

        model.addAttribute("person", person);
        return "person/edit";
    }

    @PostMapping("/remove/{id}")
    public String removePage(@PathVariable("id") long id) {
        Person person = personService.findById(id).orElse(null);
        if (person == null)
            throw new PersonNotFoundException("Person with id: " + id + " not found");

        personService.remove(person);

        return "redirect:/auth/logout";
    }
}
