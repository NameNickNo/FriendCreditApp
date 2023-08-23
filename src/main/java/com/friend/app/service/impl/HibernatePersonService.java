package com.friend.app.service.impl;

import com.friend.app.dto.PersonDTO;
import com.friend.app.models.person.Person;
import com.friend.app.dto.PersonChangePasswordDTO;
import com.friend.app.models.person.PersonStatus;
import com.friend.app.models.person.Role;
import com.friend.app.service.PersonService;
import com.friend.app.setting.HibernateQualifier;
import com.friend.app.util.exception.PersonChangePasswordException;
import com.friend.app.util.exception.PersonNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@HibernateQualifier
@Slf4j
public class HibernatePersonService implements PersonService {

    private final SessionFactory sessionFactory;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public HibernatePersonService(SessionFactory sessionFactory, PasswordEncoder passwordEncoder) {
        this.sessionFactory = sessionFactory;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Person> findAll() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("SELECT p FROM Person p JOIN FETCH p.friends", Person.class).getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Person> findById(long id) {
        Session session = sessionFactory.getCurrentSession();
        Person person = session.get(Person.class, id);

        if (person == null)
            return Optional.empty();

        initializeAllLazyAttributes(person);
        return Optional.of(person);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Person> findByUsername(String username) {
        Session session = sessionFactory.getCurrentSession();
        Query<Person> query = session.createQuery("SELECT p FROM Person p WHERE p.username = :username", Person.class);
        query.setParameter("username", username);

        return query.getResultStream().findAny();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Person> findByEmail(String email) {
        Session session = sessionFactory.getCurrentSession();
        Query<Person> query = session.createQuery("SELECT p FROM Person p WHERE p.email = :email", Person.class);
        query.setParameter("email", email);

        return query.getResultStream().findAny();
    }

    @Override
    public void save(Person person) {
        Session session = sessionFactory.getCurrentSession();
        person.setRole(Role.ROLE_USER);
        person.setStatus(PersonStatus.ACTIVE);
        person.setPassword(passwordEncoder.encode(person.getPassword()));

        session.persist(person);
        log.debug("Created new person {} : {}", person.getUsername(), person.getFullName());
    }

    @Override
    public void update(PersonDTO personDTO) {
        Session session = sessionFactory.getCurrentSession();
        Person personToUpdate = session.get(Person.class, personDTO.getId());
        if (personToUpdate == null)
            throw new PersonNotFoundException("Person with id " + personDTO.getId() + " not found!");

        if (isIdenticalAttributes(personDTO, personToUpdate)) {
            log.debug("Person not updated, nothing to update {} : {}", personToUpdate.getUsername(), personToUpdate.getFullName());
            return;
        }

        personToUpdate.setFullName(personDTO.getFullName());
        personToUpdate.setEmail(personDTO.getEmail());
        personToUpdate.setBirthDate(personDTO.getBirthDate());
        personToUpdate.setUsername(personDTO.getUsername());
        log.debug("Person was updated {} : {}", personToUpdate.getUsername(), personToUpdate.getFullName());
    }

    @Override
    public void remove(long id) {
        Session session = sessionFactory.getCurrentSession();
        Person personToRemove = session.get(Person.class, id);

        personToRemove.setStatus(PersonStatus.BANNED);
        personToRemove.setRole(Role.ROLE_REMOVED);
        log.debug("Person was removed {} : {}", personToRemove.getUsername(), personToRemove.getFullName());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Person> findOtherPersonsWithoutFriends(Person currentPerson) {
        Session session = sessionFactory.getCurrentSession();
        String peopleWithoutFriends = "SELECT per FROM Person per WHERE per.id <> :currentPersonId and per.id NOT IN (" +
                "SELECT f.friend FROM Person p JOIN Friendship f ON p.id = f.person.id where p.id = :currentPersonId)";
        Query<Person> query = session.createQuery(peopleWithoutFriends, Person.class);
        query.setParameter("currentPersonId", currentPerson.getId());
        return query.getResultList();
    }

    private boolean isIdenticalAttributes(PersonDTO personDTO, Person personToUpdate) {
        PersonDTO personToCheck = new PersonDTO(personToUpdate);
        return personDTO.equals(personToCheck);
    }

    private void initializeAllLazyAttributes(Person person) {
        Hibernate.initialize(person.getFriends());
        Hibernate.initialize(person.getDebts());
        Hibernate.initialize(person.getLends());
    }
}
