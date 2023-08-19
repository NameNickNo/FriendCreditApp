package com.friend.app.controllers.soap;

import com.friend.app.dto.DebtDTO;
import com.friend.app.models.person.Person;
import com.friend.app.service.DebtService;
import com.friend.app.service.PersonService;
import com.friend.app.setting.HibernateQualifier;
import com.friend.app.util.exception.PersonNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/debt")
public class DebtController {

    private final DebtService debtService;
    private final PersonService personService;

    public DebtController(@HibernateQualifier DebtService debtService, @HibernateQualifier PersonService personService) {
        this.debtService = debtService;
        this.personService = personService;
    }

    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("debts", debtService.findAll());
        return "debt/all";
    }

    @GetMapping("/create/{id}")
    public String createDebt(@PathVariable("id") int id, Model model) {
        Person lender = personService.findById(id).orElse(null);
        if (lender == null)
            throw new PersonNotFoundException("Person with id: " + id + " not found");

        model.addAttribute("debtDTO", new DebtDTO(lender.getUsername()));
        return "debt/create";
    }

    @PostMapping("/create")
    public String createDebt(@ModelAttribute("debtDTO") DebtDTO debtDTO) {
        debtService.create(debtDTO);
        return "redirect:/person/mypage";
    }

    @PostMapping("/confirm/{id}")
    public String confirm(@PathVariable("id") int id) {
        debtService.confirmDebt(id); //TODO Переделать на modelAttribute (разобраться с th:object)
        return "redirect:/person/mypage";
    }

    @PostMapping("/cancel/{id}")
    public String cancel(@PathVariable("id") int id) {
        debtService.cancelDebt(id); //TODO Переделать на modelAttribute (разобраться с th:object)
        return "redirect:/person/mypage";
    }
}
