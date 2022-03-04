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

import java.util.Optional;

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

  @Test
  @DisplayName("Should get one book by Id")
  public void findByIdTest(){

    Book book = createNewBook();

    entityManager.persist(book);

    Optional<Book> bookFound = repository.findById(book.getId());

    assertThat(bookFound.isPresent()).isTrue();
    assertThat(bookFound.get().getId()).isEqualTo(book.getId());

  }

  @Test
  @DisplayName("Should save one book")
  public void saveBookTest(){

    Book book = createNewBook();

    Book savedBook = repository.save(book);

    assertThat(savedBook.getAuthor()).isEqualTo("Me");
    assertThat(savedBook.getId()).isNotNull();
  }

  @Test
  @DisplayName("Should delete one book by Id")
  public void deleteBookTest(){

    Book book = createNewBook();

    entityManager.persist(book);
    Book savedBook = entityManager.find(Book.class, book.getId());
    assertThat(savedBook.getId()).isNotNull();

    repository.delete(savedBook);
    Book deletedBook = entityManager.find(Book.class, book.getId());

    assertThat(deletedBook).isNull();
  }

  private Book createNewBook(){
    return Book.builder().title("One title").author("Me").isbn("123").build();
  }

}
