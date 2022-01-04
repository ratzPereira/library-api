package com.ratz.libraryapi.service;


import com.ratz.libraryapi.entity.Book;
import com.ratz.libraryapi.entity.Loan;
import com.ratz.libraryapi.exception.BusinessException;
import com.ratz.libraryapi.repository.LoanRepository;
import com.ratz.libraryapi.service.Impl.LoanServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

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

    when(repository.existsByBookAndNotReturned(loan.getBook())).thenReturn(false);
    when(repository.save(loan)).thenReturn(loanSaved);

    Loan loan1 = loanService.save(loan);
    System.out.println(loan1.toString());
    assertThat(loan1.getId()).isEqualTo(loanSaved.getId());
    assertThat(loan1.getBook()).isEqualTo(loanSaved.getBook());
    assertThat(loan1.getLoanDate()).isEqualTo(loanSaved.getLoanDate());
    assertThat(loan1.getClientName()).isEqualTo(loanSaved.getClientName());
  }

  @Test
  @DisplayName("Should give an error when trying to save one loan with the book already loaned")
  public void saveLoanWithBookAlreadyLoaned(){

    Loan loan = makeLoan();

    when(repository.existsByBookAndNotReturned(loan.getBook())).thenReturn(true);

    Throwable exception = catchThrowable(()-> loanService.save(loan));

    assertThat(exception).isInstanceOf(BusinessException.class).hasMessage("Book already loaned");

    verify(repository, never()).save(loan);
  }


  @Test
  @DisplayName("Should get info about loan by id")
  public void getLoanDetailsTest(){

    Long id = 1L;

    Loan loan = makeLoan();
    loan.setId(id);

    Mockito.when(loanService.getById(id)).thenReturn(Optional.of(loan));

    Optional<Loan> loanFound = loanService.getById(id);

    assertThat(loanFound.isPresent()).isTrue();
    assertThat(loanFound.get().getId()).isEqualTo(id);
    assertThat(loanFound.get().getClientName()).isEqualTo(loan.getClientName());
    assertThat(loanFound.get().getBook()).isEqualTo(loan.getBook());
    assertThat(loanFound.get().getReturned()).isEqualTo(loan.getReturned());

    verify(repository).findById(id);

  }


  @Test
  @DisplayName("Should update the info about loan by id")
  public void updateLoanDetailsTest(){

    Long id = 1L;

    Loan loan = makeLoan();
    loan.setId(id);
    loan.setReturned(true);

    when(repository.save(loan)).thenReturn(loan);

    Loan update = loanService.update(loan);

    assertThat(update.getReturned()).isTrue();
    verify(repository).save(loan);

  }

  private Loan makeLoan(){
    return Loan.builder().book(makeBook()).loanDate(LocalDate.now()).clientName("Me").id(1L).build();
  }

  private Book makeBook() {
    return Book.builder().isbn("123").id(1L).title("One book").author("Me").build();
  }
}
