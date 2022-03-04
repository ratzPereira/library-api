package com.ratz.libraryapi.contoller;


import com.ratz.libraryapi.DTO.LoanDTO;
import com.ratz.libraryapi.DTO.ReturnedLoanDTO;
import com.ratz.libraryapi.entity.Book;
import com.ratz.libraryapi.entity.Loan;
import com.ratz.libraryapi.service.BookService;
import com.ratz.libraryapi.service.LoanService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;


@RestController
@RequestMapping("api/loans")
@RequiredArgsConstructor
@Slf4j
@Api("Loan API")
public class LoanController {


  private final LoanService loanService;
  private final BookService bookService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Long createLoan(@RequestBody LoanDTO loanDTO) {

    Book book = bookService.getByIsbn(loanDTO.getIsbn()).orElseThrow(() ->
        new ResponseStatusException(HttpStatus.BAD_REQUEST, "Book not found"));

    Loan loan = Loan.builder()
        .book(book)
        .loanDate(LocalDate.now())
        .clientName(loanDTO.getClientName())
        .build();

    loan = loanService.save(loan);

    return loan.getId();
  }

  @PatchMapping("/{id}")
  public void returnBook(@PathVariable Long id, @RequestBody ReturnedLoanDTO dto){

    Loan loan = loanService.getById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    loan.setReturned(dto.isReturned());
    loanService.update(loan);

  }
}
