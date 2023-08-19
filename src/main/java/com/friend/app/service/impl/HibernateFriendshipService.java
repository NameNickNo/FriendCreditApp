package com.friend.app.service.impl;

import com.friend.app.models.Friendship;
import com.friend.app.models.person.Person;
import com.friend.app.security.PersonDetailsService;
import com.friend.app.service.FriendshipService;
import com.friend.app.service.PersonService;
import com.friend.app.setting.HibernateQualifier;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@HibernateQualifier
public class HibernateFriendshipService implements FriendshipService {

    private final SessionFactory sessionFactory;
    private final PersonService personService;
    private final PersonDetailsService personDetailsService;

    @Autowired
    public HibernateFriendshipService(SessionFactory sessionFactory, @HibernateQualifier PersonService personService,
                                      PersonDetailsService personDetailsService) {
        this.sessionFactory = sessionFactory;
        this.personService = personService;
        this.personDetailsService = personDetailsService;
    }

    public void createFriendship(int id) {
        Session session = sessionFactory.getCurrentSession();
        Person friend = personService.findById(id).orElseThrow();
        Person currentPerson = personDetailsService.getAuthenticationPerson();

        session.persist(new Friendship(currentPerson, friend));
    }
}
