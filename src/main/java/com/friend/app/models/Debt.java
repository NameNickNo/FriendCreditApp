package com.friend.app.models;

import com.friend.app.dto.DebtDTO;
import com.friend.app.models.person.Person;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "debt")
@Data
public class Debt extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "debtor_id", referencedColumnName = "id")
    private Person debtor;

    @ManyToOne
    @JoinColumn(name = "lender_id", referencedColumnName = "id")
    private Person lender;

    @Column(name = "amount")
    private double amount;

    @Column(name = "date_of_credit")
    private LocalDate dateOfCredit;

    @Column(name = "credit_repayment_date")
    private LocalDate creditRepaymentDate;

    @Column(name = "percent")
    private int percent;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private DebtStatus status;

    @Column(name = "is_active")
    private boolean isActive;

    public Debt() {}

    public Debt(DebtDTO debtDTO) {
        this.setAmount(debtDTO.getAmount());
        this.setCreditRepaymentDate(debtDTO.getCreditRepaymentDate());
        this.setPercent(debtDTO.getPercent());
    }
}
