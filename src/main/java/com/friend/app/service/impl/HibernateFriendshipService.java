package com.friend.app.service.impl;

import com.friend.app.models.Friendship;
import com.friend.app.models.person.Person;
import com.friend.app.security.PersonDetailsService;
import com.friend.app.service.FriendshipService;
import com.friend.app.service.PersonService;
import com.friend.app.setting.HibernateQualifier;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@HibernateQualifier
@Slf4j
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

    public void createFriendship(long id) {
        Session session = sessionFactory.getCurrentSession();
        Person friend = personService.findById(id).orElseThrow();
        Person currentPerson = personDetailsService.getAuthenticationPerson();

        session.persist(new Friendship(currentPerson, friend));
        session.persist(new Friendship(friend, currentPerson));
        log.debug("Friendship created between personId:{} and personId:{}", currentPerson.getId() , id);
    }

    public void removeFriendship(long id) {
        Session session = sessionFactory.getCurrentSession();
        Person currentPerson = personDetailsService.getAuthenticationPerson();

        List<Friendship> friendships = findFriendship(currentPerson.getId(), id);
        if (!friendships.isEmpty()) {
            friendships.forEach(session::remove);
            log.debug("Friendship removed between personId:{} and personId:{}", currentPerson.getId() , id);
        }
    }

    @Transactional(readOnly = true)
    List<Friendship> findFriendship(long firstId, long secondId) {
        Session session = sessionFactory.getCurrentSession();
        String selectFriendship = "SELECT f FROM Friendship f WHERE f.person.id = :firstId AND f.friend.id = :secondId " +
                "OR f.person.id = :secondId AND f.friend.id = :firstId";
        Query<Friendship> query = session.createQuery(selectFriendship, Friendship.class);
        query.setParameter("firstId", firstId);
        query.setParameter("secondId", secondId);

        return query.getResultList();
    }
}
