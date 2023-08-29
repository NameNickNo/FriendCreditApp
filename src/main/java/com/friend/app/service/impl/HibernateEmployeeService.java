package com.friend.app.service.impl;

import com.friend.app.models.Employee;
import com.friend.app.service.EmployeeService;
import com.friend.app.setting.HibernateQualifier;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@HibernateQualifier
@Slf4j
public class HibernateEmployeeService implements EmployeeService {

    private final SessionFactory sessionFactory;

    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private int BATCH_SIZE;

    public HibernateEmployeeService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(List<Employee> employees) {
        Session session = sessionFactory.getCurrentSession();

        for (int i = 0; i < employees.size(); i++) {
            if (i > 0 && i % BATCH_SIZE == 0) {
                session.flush();
                session.clear();
                log.info("Clear");
            }
            session.persist(employees.get(i));
            log.info("Employee save {} {}", employees.get(i).getId(), employees.get(i).getFullName());
        }
    }
}
