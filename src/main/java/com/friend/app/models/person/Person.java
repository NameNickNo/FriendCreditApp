package com.friend.app.models.person;

import com.friend.app.models.BaseEntity;
import com.friend.app.models.Debt;
import com.friend.app.models.Friendship;
import com.friend.app.models.TelegramAccount;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "person")
@Data
@NoArgsConstructor
public class Person extends BaseEntity {

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "email")
    private String email;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PersonStatus status;

    @OneToMany(mappedBy = "debtor", cascade = CascadeType.PERSIST)
    private List<Debt> debts;

    @OneToMany(mappedBy = "lender", cascade = CascadeType.PERSIST)
    private List<Debt> lends;

    @OneToMany(mappedBy = "person", cascade = CascadeType.PERSIST)
    private List<Friendship> friends;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "telegram_account_id", referencedColumnName = "id")
    private TelegramAccount telegramAccount;

    public Person(String fullName, String email, String username) {
        this.fullName = fullName;
        this.email = email;
        this.username = username;
    }
}
