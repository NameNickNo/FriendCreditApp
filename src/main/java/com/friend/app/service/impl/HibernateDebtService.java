package com.friend.app.service.impl;

import com.friend.app.dto.DebtDTO;
import com.friend.app.models.Debt;
import com.friend.app.models.DebtStatus;
import com.friend.app.models.person.Person;
import com.friend.app.security.PersonDetailsService;
import com.friend.app.service.DebtService;
import com.friend.app.service.PersonService;
import com.friend.app.setting.HibernateQualifier;
import com.friend.app.util.exception.DebtCreationException;
import com.friend.app.util.exception.DebtNotFoundException;
import com.friend.app.util.exception.PersonNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@HibernateQualifier
public class HibernateDebtService implements DebtService {

    private final SessionFactory sessionFactory;
    private final PersonService personService;
    private final PersonDetailsService personDetailsService;

    @Autowired
    public HibernateDebtService(SessionFactory sessionFactory, @HibernateQualifier PersonService personService,
                                PersonDetailsService personDetailsService) {
        this.sessionFactory = sessionFactory;
        this.personService = personService;
        this.personDetailsService = personDetailsService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Debt> findAll() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("select d from Debt d", Debt.class).getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Debt> findById(int id) {
        Session session = sessionFactory.getCurrentSession();
        Debt debt = session.get(Debt.class, id);

        if (debt == null)
            return Optional.empty();

        return Optional.of(debt);
    }

    @Override
    public void create(DebtDTO debtDTO) {
        if (debtDTO == null)
            throw new DebtCreationException("Debt not created. DTO is null!");

        Session session = sessionFactory.getCurrentSession();

        Optional<Person> lender = personService.findByUsername(debtDTO.getLenderUsername());
        if (lender.isEmpty())
            throw new PersonNotFoundException("Person with username '" + debtDTO.getLenderUsername() + "' not found. Debt not created!");

        Debt debt = enrichDebt(debtDTO);
        debt.setLender(lender.get());

        session.persist(debt);
    }

    @Override
    public void confirmDebt(int id) {
        Session session = sessionFactory.getCurrentSession();
        Debt debt = session.get(Debt.class, id);

        if (debt == null)
            throw new DebtNotFoundException("Debt with id " + id + " not found!");

        debt.setStatus(DebtStatus.CONFIRMED);
        session.persist(debt);
    }

    @Override
    public void cancelDebt(int id) {
        Session session = sessionFactory.getCurrentSession();
        Debt debt = session.get(Debt.class, id);

        if (debt == null)
            throw new DebtNotFoundException("Debt with id " + id + " not found!");

        debt.setStatus(DebtStatus.CANCELED);
        debt.setUpdated(LocalDateTime.now());
        debt.setActive(false);
        session.persist(debt);
    }

    private Debt enrichDebt(DebtDTO debtDTO) {
        Debt debt = new Debt(debtDTO);
        debt.setDebtor(personDetailsService.getAuthenticationPerson());
        debt.setDateOfCredit(LocalDate.now());
        debt.setUpdated(LocalDateTime.now());
        debt.setStatus(DebtStatus.NOT_CONFIRMED);
        debt.setActive(true);
        return debt;
    }
}
