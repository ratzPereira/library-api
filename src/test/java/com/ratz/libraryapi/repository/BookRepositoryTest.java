package com.ratz.libraryapi.repository;


import com.ratz.libraryapi.entity.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DataJpaTest
public class BookRepositoryTest {

  @Autowired
  TestEntityManager entityManager;

  @Autowired
  BookRepository repository;


  @Test
  @DisplayName("Should return true if find one book with a given Isbn")
  public void findBookByIsbn(){

    String isbn = "123";
    entityManager.persist(Book.builder().isbn(isbn).build());

    boolean existsByIsbn = repository.existsByIsbn(isbn);
    assertThat(existsByIsbn).isTrue();
  }

  @Test
  @DisplayName("Should return false when trying to find book by given isbn without saving it")
  public void notFindBookByGivenIsbn(){

    String isbn = "123";

    boolean existsByIsbn = repository.existsByIsbn(isbn);
    assertThat(existsByIsbn).isFalse();
  }

}
