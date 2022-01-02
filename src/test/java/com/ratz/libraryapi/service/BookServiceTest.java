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
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class BookServiceTest {

  BookService service;

  @MockBean
  BookRepository repository;

  @BeforeEach
  public void setUp() {
    this.service = new BookServiceImpl(repository);
  }

  @Test
  @DisplayName("Should save the book")
  public void saveBookTest() {
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
  public void shouldThrowErrorIfIsbnNotUnique() {

    Book book = createValidBook();
    Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(true);

    Throwable exception = Assertions.catchThrowable(() -> service.save(book));

    assertThat(exception).isInstanceOf(BusinessException.class)
        .hasMessage("One book with this Isbn already exist");

    Mockito.verify(repository, Mockito.never()).save(book);

  }

  private Book createValidBook() {
    return Book.builder().id(1L).isbn("123").author("Me").title("Save Book Test").build();
  }

  @Test
  @DisplayName("Should get one book by id")
  public void getBookByIdTest() {

    Long id = 1L;

    Book book = createValidBook();
    book.setId(id);

    Mockito.when(repository.findById(id)).thenReturn(Optional.of(book));

    Optional<Book> foundBook = service.getById(id);

    assertThat(foundBook.isPresent()).isTrue();
    assertThat(foundBook.get().getId()).isEqualTo(id);
    assertThat(foundBook.get().getIsbn()).isEqualTo(book.getIsbn());
    assertThat(foundBook.get().getAuthor()).isEqualTo(book.getAuthor());
    assertThat(foundBook.get().getTitle()).isEqualTo(book.getTitle());


  }

  @Test
  @DisplayName("Should return empty when trying to find book by id that not exist")
  public void returnEmptyWhenGetBookByNoExistingIdTest() {

    Long id = 1L;

    Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

    Optional<Book> foundBook = service.getById(id);

    assertThat(foundBook.isPresent()).isFalse();
  }

  @Test
  @DisplayName("Should delete one book")
  public void deleteBookTest() {

    Book book = createValidBook();

    org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> service.deleteBook(book));

    Mockito.verify(repository, Mockito.times(1)).delete(book);
  }

  @Test
  @DisplayName("Should return error delete one book that not exist")
  public void deleteInvalidBookTest() {

    Book book = new Book();

    org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> service.deleteBook(book));

    Mockito.verify(repository, Mockito.times(0)).delete(book);
  }

  @Test
  @DisplayName("Should update one book")
  public void updateBookTest() {

    Book book = createValidBook();

    Book updatingBook = createValidBook();
    updatingBook.setAuthor("Not me");

    Mockito.when(repository.save(book)).thenReturn(updatingBook);

    Book book1 = service.update(book);


    assertThat(book1.getId()).isEqualTo(updatingBook.getId());
    assertThat(book1.getTitle()).isEqualTo(updatingBook.getTitle());
    assertThat(book1.getAuthor()).isEqualTo(updatingBook.getAuthor());
    assertThat(updatingBook.getAuthor()).isEqualTo("Not me");
  }

  @Test
  @DisplayName("Should return error when update one invalid book")
  public void updateInvalidBookTest() {

    Book book = new Book();

    org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> service.update(book));

    Mockito.verify(repository, Mockito.times(0)).save(book);
  }


  @Test
  @DisplayName("Should filter books by the given properties")
  public void findBookTest() {

    Book book = createValidBook();
    PageRequest pageRequest = PageRequest.of(0, 10);

    List<Book> list = Arrays.asList(book);

    Page<Book> page = new PageImpl<Book>(Arrays.asList(book), pageRequest, 1);
    Mockito.when(repository.findAll(Mockito.any(Example.class), Mockito.any(PageRequest.class))).thenReturn(page);

    Page<Book> books = service.find(book, pageRequest);

    assertThat(books.getTotalElements()).isEqualTo(1);
    assertThat(books.getContent()).isEqualTo(list);
    assertThat(books.getPageable().getPageNumber()).isEqualTo(0);
    assertThat(books.getPageable().getPageSize()).isEqualTo(10);

  }


}
