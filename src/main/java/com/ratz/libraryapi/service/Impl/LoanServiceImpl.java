package com.ratz.libraryapi.service.Impl;


import com.ratz.libraryapi.DTO.LoanDTO;
import com.ratz.libraryapi.DTO.LoanFilterDTO;
import com.ratz.libraryapi.entity.Book;
import com.ratz.libraryapi.entity.Loan;
import com.ratz.libraryapi.exception.BusinessException;
import com.ratz.libraryapi.repository.LoanRepository;
import com.ratz.libraryapi.service.LoanService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
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
  public Page<Loan> find(LoanFilterDTO any, Pageable any1) {
//
//    Example<LoanFilterDTO> example = Example.of(any, ExampleMatcher.matching()
//        .withIgnoreCase()
//        .withIgnoreNullValues()
//        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));
//
//    return loanRepository.findAll(example, any1);
    return null;
  }
}
