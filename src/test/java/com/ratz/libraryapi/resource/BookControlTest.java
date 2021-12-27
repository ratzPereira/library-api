package com.ratz.libraryapi.resource;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ratz.libraryapi.DTO.BookDTO;
import com.ratz.libraryapi.entity.Book;
import com.ratz.libraryapi.exception.BusinessException;
import com.ratz.libraryapi.service.BookService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@WebMvcTest
@AutoConfigureMockMvc
public class BookControlTest {

  static String BOOK_API = "/api/books";

  @Autowired
  MockMvc mockMvc;

  @MockBean
  BookService bookService;

  @Test
  @DisplayName("Should create one book with success")
  public void createBookTest() throws Exception {

    BookDTO book = createBook();

    Book savedBook = Book.builder().author("Me").id(1L).isbn("123").title("Book Test").build();

    BDDMockito.given(bookService.save(Mockito.any(Book.class))).willReturn(savedBook);

    String json = new ObjectMapper().writeValueAsString(book);

    MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(BOOK_API)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(json);

    mockMvc.perform(request)
        .andExpect(status().isCreated())
        .andExpect(jsonPath("id").value(book.getId()))
        .andExpect(jsonPath("title").value(book.getTitle()))
        .andExpect(jsonPath("author").value(book.getAuthor()));
  }

  @Test
  @DisplayName("Should give an error when we dont have all data to create one book")
  public void createInvalidBookTest() throws Exception {

    String json = new ObjectMapper().writeValueAsString(new BookDTO());

    MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(BOOK_API)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(json);

    mockMvc.perform(request)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("errors", hasSize(3)));
  }

  @Test
  @DisplayName("Should give an error if we try to save one book with not unique Isbn")
  public void createBookWithDuplicateIsbn() throws Exception {

    BookDTO book = createBook();
    String json = new ObjectMapper().writeValueAsString(book);
    String errorMessage = "One book with this Isbn already exist";

    BDDMockito.given(bookService.save(Mockito.any(Book.class)))
        .willThrow(new BusinessException(errorMessage));

    MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(BOOK_API)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(json);

    mockMvc.perform(request).andExpect(status().isBadRequest())
        .andExpect(jsonPath("errors", hasSize(1)))
        .andExpect(jsonPath("errors[0]").value(errorMessage));

  }


  @Test
  @DisplayName("Should give book details")
  public void getBookDetailsTest() throws Exception {

    Long id = 1L;

    Book book = Book.builder().id(id)
        .title(createBook().getTitle())
        .author(createBook().getAuthor())
        .isbn(createBook().getIsbn()).build();

    BDDMockito.given(bookService.getById(id)).willReturn(Optional.of(book));

    MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(BOOK_API.concat("/" + id)).accept(MediaType.APPLICATION_JSON);

    mockMvc.perform(request)
        .andExpect(status().isOk())
        .andExpect(jsonPath("id").value(id))
        .andExpect(jsonPath("title").value(createBook().getTitle()))
        .andExpect(jsonPath("author").value(createBook().getAuthor()))
        .andExpect(jsonPath("isbn").value(createBook().getIsbn()));

  }

  @Test
  @DisplayName("Should return resource not found if no book found")
  public void getBookNotFoundTest() throws Exception {

    BDDMockito.given(bookService.getById(anyLong())).willReturn(Optional.empty());

    MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(BOOK_API.concat("/" + 1)).accept(MediaType.APPLICATION_JSON);

    mockMvc.perform(request)
        .andExpect(status().isNotFound());
  }


  @Test
  @DisplayName("Should delete a book with success")
  public void deleteBookTest() throws Exception {

    BDDMockito.given(bookService.getById(anyLong())).willReturn(Optional.of(Book.builder().id(1L).build()));

    MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete(BOOK_API.concat("/" + 1));

    mockMvc.perform(request).andExpect(status().isNoContent());

  }

  @Test
  @DisplayName("Should return resource not found when can't find one book to delete")
  public void bookToDeleteNotFoundTest() throws Exception {

    BDDMockito.given(bookService.getById(anyLong())).willReturn(Optional.empty());

    MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete(BOOK_API.concat("/" + 1));

    mockMvc.perform(request).andExpect(status().isNotFound());

  }


  private BookDTO createBook() {
    return BookDTO.builder().author("Me").id(1L).isbn("123").title("Book Test").build();
  }

}
