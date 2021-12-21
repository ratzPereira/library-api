package com.ratz.libraryapi.resource;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ratz.libraryapi.DTO.BookDTO;
import com.ratz.libraryapi.entity.Book;
import com.ratz.libraryapi.service.BookService;
import org.hamcrest.Matchers;
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

import java.util.regex.Matcher;

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

    BookDTO book = BookDTO.builder().author("Me").id(1L).isbn("123").title("Book Test").build();

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
        .andExpect(jsonPath("errors", Matchers.hasSize(3)));
  }
}
