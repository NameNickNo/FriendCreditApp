package com.friend.app.controllers.soap;

import com.friend.app.dto.DebtDTO;
import com.friend.app.models.person.Person;
import com.friend.app.models.person.PersonChangePasswordEntity;
import com.friend.app.security.PersonDetails;
import com.friend.app.service.PersonService;
import com.friend.app.setting.HibernateQualifier;
import com.friend.app.util.exception.PersonNotFoundException;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/person")
public class PersonController {

    private final PersonService personService;

    public PersonController(@HibernateQualifier PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    public String getAll(@AuthenticationPrincipal PersonDetails personDetails, Model model) {
        Person authenticationPerson = personDetails.getPerson();
        Optional<Person> currentPerson = personService.findById(authenticationPerson.getId());
        if (currentPerson.isEmpty())
            throw new PersonNotFoundException("Person with id: " + authenticationPerson.getId() + " not found");

        model.addAttribute("persons", personService.findOtherPersonsWithoutFriends(authenticationPerson));
        model.addAttribute("currentPerson", currentPerson.get());
        return "person/all";
    }

    @GetMapping("/mypage")
    public String getMyPage(@AuthenticationPrincipal PersonDetails personDetails, Model model) {
        Person authenticationPerson = personDetails.getPerson();
        Optional<Person> person = personService.findById(authenticationPerson.getId());
        if (person.isEmpty())
            throw new PersonNotFoundException("Person with id: " + authenticationPerson.getId() + " not found");

        model.addAttribute("person", person.get());
        model.addAttribute("debtDTO", new DebtDTO(person.get().getUsername()));
        return "person/personalPage";
    }

    @GetMapping("/{id}")
    public String getOne(@PathVariable("id") int id, Model model) {
        Optional<Person> person = personService.findById(id);
        if (person.isEmpty())
            throw new PersonNotFoundException("Person with id: " + id + " not found");

        model.addAttribute("person", person.get());
        return "person/onePerson";
    }

    @GetMapping("/mypage/edit")
    public String editMyPage(@AuthenticationPrincipal PersonDetails personDetails, Model model) {
        Person authenticationPerson = personDetails.getPerson();
        Person person = personService.findById(authenticationPerson.getId()).orElse(null);
        if (person == null)
            throw new PersonNotFoundException("Person with id: " + authenticationPerson.getId() + " not found");

        model.addAttribute("person", person);
        return "person/edit";
    }

    @PostMapping("/mypage/edit")
    public String edit(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "person/edit";

        personService.update(person);
        return "redirect:/person/mypage";
    }

    @GetMapping("/mypage/changepass")
    public String editMyPassword(Model model) {
        model.addAttribute("personChangePass", new PersonChangePasswordEntity());
        return "person/changePass";
    }

    @PostMapping("/mypage/changepass")
    public String editPassword(@ModelAttribute("personEditPass") @Valid PersonChangePasswordEntity personChangePasswordEntity,
                               @AuthenticationPrincipal PersonDetails personDetails, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "person/editpass";

        Person authenticationPerson = personDetails.getPerson();
        personService.changePassword(authenticationPerson, personChangePasswordEntity);
        return "redirect:/person/mypage";
    }

    @PostMapping("/remove")
    public String removePage(@AuthenticationPrincipal PersonDetails personDetails) {
        Person authenticationPerson = personDetails.getPerson();
        personService.remove(authenticationPerson);

        return "redirect:/auth/logout";
    }
}
