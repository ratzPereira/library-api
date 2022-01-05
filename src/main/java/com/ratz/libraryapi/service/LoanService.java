package com.ratz.libraryapi.service;


import com.ratz.libraryapi.DTO.LoanFilterDTO;
import com.ratz.libraryapi.entity.Loan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;


public interface LoanService {

  Loan save(Loan loan);
  Optional<Loan> getById(Long id);
  Loan update(Loan loan);
  Page<Loan> find(LoanFilterDTO any, Pageable any1);
}
