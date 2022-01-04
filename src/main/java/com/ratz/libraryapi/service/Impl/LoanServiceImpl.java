package com.ratz.libraryapi.service.Impl;


import com.ratz.libraryapi.entity.Loan;
import com.ratz.libraryapi.exception.BusinessException;
import com.ratz.libraryapi.repository.LoanRepository;
import com.ratz.libraryapi.service.LoanService;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class LoanServiceImpl implements LoanService {

  private LoanRepository loanRepository;

  public LoanServiceImpl(LoanRepository loanRepository) {
    this.loanRepository = loanRepository;
  }

  @Override
  public Loan save(Loan loan) {

    if(loanRepository.existsByBookAndNotReturned(loan.getBook())) {
      throw new BusinessException("Book already loaned");
    }
    return loanRepository.save(loan);
  }

  @Override
  public Optional<Loan> getById(Long id) {
    return loanRepository.findById(id);
  }

  @Override
  public Loan update(Loan loan) {
    return null;
  }
}
