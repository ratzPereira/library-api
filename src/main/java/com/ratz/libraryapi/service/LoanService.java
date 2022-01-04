package com.ratz.libraryapi.service;


import com.ratz.libraryapi.entity.Loan;

import java.util.Optional;


public interface LoanService {

  Loan save(Loan loan);
  Optional<Loan> getById(Long id);
  Loan update(Loan loan);
}
