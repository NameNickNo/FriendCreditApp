package com.friend.app.service.impl;

import com.friend.app.models.TelegramAccount;
import com.friend.app.models.person.Person;
import com.friend.app.service.TelegramAccountService;
import com.friend.app.setting.HibernateQualifier;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@HibernateQualifier
public class HibernateTelegramAccountService implements TelegramAccountService {

    private final SessionFactory sessionFactory;

    public HibernateTelegramAccountService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional(readOnly = true)
    public List<TelegramAccount> findAll() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("Select t from TelegramAccount t", TelegramAccount.class).getResultList();
    }

    @Override
    public void create(TelegramAccount telegramAccount, Person person) {
        Session session = sessionFactory.getCurrentSession();
        Person personToUpdate = session.get(Person.class, person.getId());

        personToUpdate.setTelegramAccount(telegramAccount);
        session.persist(telegramAccount);
        session.persist(personToUpdate);
    }

    @Override
    public void remove(Person person) {
        Session session = sessionFactory.getCurrentSession();
        Person personToUpdate = session.get(Person.class, person.getId());
        TelegramAccount telegramAccount = personToUpdate.getTelegramAccount();
        TelegramAccount telegramAccountToRemove = session.get(TelegramAccount.class, telegramAccount.getId());

        personToUpdate.setTelegramAccount(null);
        session.remove(telegramAccountToRemove);
        session.persist(personToUpdate);
    }
}
