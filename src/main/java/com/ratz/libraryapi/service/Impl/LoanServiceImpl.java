package com.ratz.libraryapi.service.Impl;


import com.ratz.libraryapi.DTO.LoanDTO;
import com.ratz.libraryapi.entity.Loan;
import com.ratz.libraryapi.repository.LoanRepository;
import com.ratz.libraryapi.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoanServiceImpl implements LoanService {

  @Autowired
  LoanRepository loanRepository;

  @Override
  public Loan save(Loan dto) {
    return loanRepository.save(dto);
  }
}
