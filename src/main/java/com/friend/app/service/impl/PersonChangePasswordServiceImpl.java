package com.friend.app.service.impl;

import com.friend.app.dto.PersonChangePasswordDTO;
import com.friend.app.models.person.Person;
import com.friend.app.service.PersonChangePasswordService;
import com.friend.app.util.exception.PersonChangePasswordException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class PersonChangePasswordServiceImpl implements PersonChangePasswordService {

    private final SessionFactory sessionFactory;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PersonChangePasswordServiceImpl(SessionFactory sessionFactory, PasswordEncoder passwordEncoder) {
        this.sessionFactory = sessionFactory;
        this.passwordEncoder = passwordEncoder;
    }

    public void changePassword(Person person, PersonChangePasswordDTO changePassEntity) {
        Session session = sessionFactory.getCurrentSession();
        Person personToChangePass = session.get(Person.class, person.getId());

        String oldPassword = changePassEntity.getOldPassword();
        String newPassword = changePassEntity.getNewPassword();

        if (!passwordEncoder.matches(oldPassword, personToChangePass.getPassword()))
            throw new PersonChangePasswordException("Old password not correct!");

        if (!newPassword.equals(changePassEntity.getRepeatNewPassword()))
            throw new PersonChangePasswordException("Repeat password not correct!");

        if (oldPassword.equals(newPassword))
            throw new PersonChangePasswordException("Old and new password must be different!");

        personToChangePass.setPassword(passwordEncoder.encode(newPassword));
        log.debug("Password changed, user: " + person.getUsername());
    }
}
