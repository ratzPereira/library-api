package com.ratz.libraryapi.repository;


import com.ratz.libraryapi.entity.Book;
import com.ratz.libraryapi.entity.Loan;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DataJpaTest
public class LoanRepositoryTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private LoanRepository loanRepository;

  @Test
  @DisplayName("Should verify if exists one loan for given book not returned ")
  public void existsByBookAndNotReturned(){

    Book book = makeBook();
    Loan loan = Loan.builder().book(book).loanDate(LocalDate.now()).clientName("Me").build();

    entityManager.persist(book);
    entityManager.persist(loan);

    boolean exists = loanRepository.existsByBookAndNotReturned(book);

    assertThat(exists).isTrue();

  }





  private Book makeBook() {
    return Book.builder().isbn("123").build();
  }
}
