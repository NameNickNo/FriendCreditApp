package com.friend.app.models;

import com.friend.app.models.person.Person;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "friendship")
@Data
public class Friendship extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person person;

    @OneToOne
    @JoinColumn(name = "friend_id", referencedColumnName = "id")
    private Person friend;

    public Friendship() {
    }

    public Friendship(Person person, Person friend) {
        this.person = person;
        this.friend = friend;
    }
}
