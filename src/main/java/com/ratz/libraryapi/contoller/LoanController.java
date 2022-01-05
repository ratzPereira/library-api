package com.ratz.libraryapi.contoller;


import com.ratz.libraryapi.DTO.BookDTO;
import com.ratz.libraryapi.DTO.LoanDTO;
import com.ratz.libraryapi.DTO.LoanFilterDTO;
import com.ratz.libraryapi.DTO.ReturnedLoanDTO;
import com.ratz.libraryapi.entity.Book;
import com.ratz.libraryapi.entity.Loan;
import com.ratz.libraryapi.service.BookService;
import com.ratz.libraryapi.service.LoanService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@RestController
@RequestMapping("api/loans")
@RequiredArgsConstructor
@Slf4j
@Api("Loan API")
public class LoanController {


  private final LoanService loanService;
  private final BookService bookService;
  private final ModelMapper modelMapper;

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
  public void returnBook(@PathVariable Long id, @RequestBody ReturnedLoanDTO dto) {

    Loan loan = loanService.getById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    loan.setReturned(dto.isReturned());
    loanService.update(loan);

  }

  @GetMapping
  public Page<LoanDTO> find(LoanFilterDTO dto, Pageable pageRequest) {

    Page<Loan> result = loanService.find(dto, pageRequest);

    List<LoanDTO> loanDTOS = result.getContent().stream().map(entity -> {

      Book book = entity.getBook();
      BookDTO bookDTO = modelMapper.map(book, BookDTO.class);
      LoanDTO loanDTO = modelMapper.map(entity, LoanDTO.class);
      loanDTO.setBook(bookDTO);

      return loanDTO;

    }).collect(Collectors.toList());

    return new PageImpl<>(loanDTOS, pageRequest, result.getTotalElements());
  }
}
