package com.ratz.libraryapi.service.Impl;


import com.ratz.libraryapi.entity.Loan;
import com.ratz.libraryapi.repository.LoanRepository;
import com.ratz.libraryapi.service.LoanService;
import org.springframework.stereotype.Service;



@Service
public class LoanServiceImpl implements LoanService {

  private LoanRepository loanRepository;

  public LoanServiceImpl(LoanRepository loanRepository) {
    this.loanRepository = loanRepository;
  }

  @Override
  public Loan save(Loan loan) {
    return loanRepository.save(loan);
  }
}
