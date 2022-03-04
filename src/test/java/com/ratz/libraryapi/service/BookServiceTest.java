package com.ratz.libraryapi.service;

import com.ratz.libraryapi.entity.Book;
import com.ratz.libraryapi.exception.BusinessException;
import com.ratz.libraryapi.repository.BookRepository;
import com.ratz.libraryapi.service.Impl.BookServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

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
    Book book = createValidBook();
    Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(false);

    Mockito.when(repository.save(book)).thenReturn(createValidBook());
    Book savedBook = service.save(book);

    assertThat(savedBook.getId()).isNotNull();
    assertThat(savedBook.getIsbn()).isEqualTo("123");
    assertThat(savedBook.getTitle()).isEqualTo("Save Book Test");
  }


  @Test
  @DisplayName("Should throw an error when trying to save a book with not unique Isbn")
  public void shouldThrowErrorIfIsbnNotUnique(){

    Book book = createValidBook();
    Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(true);

    Throwable exception = Assertions.catchThrowable(() -> service.save(book));

    assertThat(exception).isInstanceOf(BusinessException.class)
        .hasMessage("One book with this Isbn already exist");

    Mockito.verify(repository, Mockito.never()).save(book);

  }

  @Test
  @DisplayName("Should find one book by given id")
  public void findBookByIdTest(){

    Long id = 1L;

    Book book = createValidBook();
    book.setId(id);
    Mockito.when(repository.findById(book.getId())).thenReturn(Optional.of(book));

    Optional<Book> bookOptional = service.getById(id);

    assertThat(bookOptional.isPresent()).isTrue();
    assertThat(bookOptional.get().getId()).isEqualTo(id);
  }

  @Test
  @DisplayName("Should return empty if no book with given id is found")
  public void bookNotFoundWithNoExistentId(){

    Long id = 1L;

    Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

    Optional<Book> book = service.getById(id);

    assertThat(book.isPresent()).isFalse();

  }


  private Book createValidBook() {
    return Book.builder().id(1L).isbn("123").author("Me").title("Save Book Test").build();
  }
}
