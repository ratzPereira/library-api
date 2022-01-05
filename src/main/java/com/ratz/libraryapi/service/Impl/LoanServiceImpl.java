package com.ratz.libraryapi.service.Impl;


import com.ratz.libraryapi.DTO.LoanFilterDTO;
import com.ratz.libraryapi.entity.Loan;
import com.ratz.libraryapi.exception.BusinessException;
import com.ratz.libraryapi.repository.LoanRepository;
import com.ratz.libraryapi.service.LoanService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    return loanRepository.save(loan);
  }

  @Override
  public Page<Loan> find(LoanFilterDTO filter, Pageable pageable) {
    return loanRepository.findByBookIsbnOrClientName(filter.getIsbn(),filter.getClientName(), pageable);
  }
}
