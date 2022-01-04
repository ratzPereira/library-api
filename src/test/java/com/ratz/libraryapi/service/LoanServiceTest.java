package com.ratz.libraryapi.service;


import com.ratz.libraryapi.entity.Book;
import com.ratz.libraryapi.entity.Loan;
import com.ratz.libraryapi.repository.LoanRepository;
import com.ratz.libraryapi.service.Impl.LoanServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class LoanServiceTest {

  @MockBean
  LoanRepository repository;

  @MockBean
  LoanService loanService;

  @BeforeEach
  public void setUp(){
    this.loanService = new LoanServiceImpl(repository);
  }

  @Test
  @DisplayName("Should save one loan")
  public void saveLoanTest(){

    String customer = "Me";

    Loan loan = makeLoan();
    loan.setClientName(customer);

    Loan loanSaved = makeLoan();
    loanSaved.setClientName(customer);

    when(repository.save(loan)).thenReturn(loanSaved);

    Loan loan1 = loanService.save(loan);
    System.out.println(loan1.toString());
    assertThat(loan1.getId()).isEqualTo(loanSaved.getId());
    assertThat(loan1.getBook()).isEqualTo(loanSaved.getBook());
    assertThat(loan1.getLoanDate()).isEqualTo(loanSaved.getLoanDate());
    assertThat(loan1.getClientName()).isEqualTo(loanSaved.getClientName());
  }






  private Loan makeLoan(){
    return Loan.builder().book(makeBook()).loanDate(LocalDate.now()).clientName("Me").id(1L).build();
  }

  private Book makeBook() {
    return Book.builder().isbn("123").id(1L).title("One book").author("Me").build();
  }
}
