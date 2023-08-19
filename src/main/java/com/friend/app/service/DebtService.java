package com.friend.app.service;

import com.friend.app.dto.DebtDTO;
import com.friend.app.models.Debt;

import java.util.List;
import java.util.Optional;

public interface DebtService {

    List<Debt> findAll();

    Optional<Debt> findById(int id);

    void create(DebtDTO debtDTO);

    void confirmDebt(int id);

    void cancelDebt(int id);
}
