package com.friend.app.repo;

import com.friend.app.models.person.Person;
import org.hibernate.annotations.SQLSelect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    Optional<Person> findByUsername(String username);

    Optional<Person> findByEmail(String email);

//    @SQLSelect(sql = "SELECT per FROM Person per WHERE per.id <> :currentPersonId and per.id NOT IN (\" +\n" +
//            "SELECT f.friend FROM Person p JOIN Friendship f ON p.id = f.person.id where p.id = :currentPersonId)")
//    List<Person> findOtherPersonsWithoutFriends(Long currentPersonId);
}
