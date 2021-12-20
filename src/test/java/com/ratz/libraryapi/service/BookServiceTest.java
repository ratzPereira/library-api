package com.ratz.libraryapi.service;

import com.ratz.libraryapi.entity.Book;
import com.ratz.libraryapi.repository.BookRepository;
import com.ratz.libraryapi.service.Impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class BookServiceTest {

  BookService service;

  @MockBean
  BookRepository repository;

  @BeforeEach
  public void setUp(){
    this.service = new BookServiceImpl(repository);
  }

  @Test
  @DisplayName("Should save the book")
  public void saveBookTest(){
    Book book = Book.builder().id(1L).isbn("123").author("Me").title("Save Book Test").build();

    Mockito.when(repository.save(book)).thenReturn(Book.builder().id(1L).isbn("123").author("Me").title("Save Book Test").build());
    Book savedBook = service.save(book);

    assertThat(savedBook.getId()).isNotNull();
    assertThat(savedBook.getIsbn()).isEqualTo("123");
    assertThat(savedBook.getTitle()).isEqualTo("Save Book Test");
  }
}
