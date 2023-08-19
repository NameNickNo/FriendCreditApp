package com.friend.app.service.impl;

import com.friend.app.models.person.Person;
import com.friend.app.service.PersonService;
import org.hibernate.SessionFactory;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HibernatePersonServiceTest {

    @InjectMocks
    private SessionFactory sessionFactory;

    @Mock
    HibernatePersonService personService;


    void findAll() {
    }

    @Test
    void findById() {

    }
}