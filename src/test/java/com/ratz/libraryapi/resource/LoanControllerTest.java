package com.ratz.libraryapi.resource;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ratz.libraryapi.DTO.LoanDTO;
import com.ratz.libraryapi.DTO.LoanFilterDTO;
import com.ratz.libraryapi.DTO.ReturnedLoanDTO;
import com.ratz.libraryapi.contoller.LoanController;
import com.ratz.libraryapi.entity.Book;
import com.ratz.libraryapi.entity.Loan;
import com.ratz.libraryapi.exception.BusinessException;
import com.ratz.libraryapi.service.BookService;
import com.ratz.libraryapi.service.LoanService;
import com.ratz.libraryapi.service.LoanServiceTest;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = LoanController.class)
@AutoConfigureMockMvc
public class LoanControllerTest {


  private final String LOAN_API = "/api/loans";

  @Autowired
  MockMvc mockMvc;

  @MockBean
  BookService bookService;

  @MockBean
  LoanService loanService;


  @Test
  @DisplayName("Should make an loan successfully")
  public void createLoanTest() throws Exception {

    LoanDTO loanDTO = createNewLoanDTO();
    Book book = Book.builder().id(1L).isbn("123").author("Me").title("Title").build();

    String json = new ObjectMapper().writeValueAsString(loanDTO);
    Loan loan = Loan.builder().clientName("me").id(1L).book(book).loanDate(LocalDate.now()).returned(false).build();

    BDDMockito.given(bookService.getByIsbn(loanDTO.getIsbn()))
        .willReturn(Optional.of(book));

    BDDMockito.given(loanService.save(Mockito.any(Loan.class))).willReturn(loan);


    MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(LOAN_API)
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(json);

    mockMvc.perform(requestBuilder)
        .andExpect(status().isCreated())
        .andExpect(content().string("1"));

  }

  @Test
  @DisplayName("Should give error when trying to create one loan with invalid Isbn")
  public void invalidIsbnWhenCreatingLoanTest() throws Exception {

    LoanDTO dto = LoanDTO.builder().isbn("123").clientName("Me").build();
    String json = new ObjectMapper().writeValueAsString(dto);

    BDDMockito.given(bookService.getByIsbn("123")).willReturn(Optional.empty());

    MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(LOAN_API)
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(json);

    mockMvc.perform(request)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("errors", Matchers.hasSize(1)))
        .andExpect(jsonPath("errors[0]").value("Book not found"));
  }

  @Test
  @DisplayName("Should give error when trying to create one loan with one book already loaned")
  public void bookIsLoanedOnCreateLoanTest() throws Exception {

    LoanDTO dto = LoanDTO.builder().isbn("123").clientName("Me").build();
    String json = new ObjectMapper().writeValueAsString(dto);

    Book book = Book.builder().id(1L).isbn("123").build();
    BDDMockito.given(bookService.getByIsbn("123")).willReturn(Optional.of(book));

    BDDMockito.given(loanService.save(Mockito.any(Loan.class))).willThrow(new BusinessException("Book already loaned"));


    MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(LOAN_API)
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(json);

    mockMvc.perform(request)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("errors", Matchers.hasSize(1)))
        .andExpect(jsonPath("errors[0]").value("Book already loaned"));
  }

  @Test
  @DisplayName("Should return one book")
  public void returnBookTest() throws Exception {

    ReturnedLoanDTO dto = ReturnedLoanDTO.builder().returned(true).build();
    Loan loan = Loan.builder().id(1L).build();

    BDDMockito.given(loanService.getById(Mockito.anyLong()))
        .willReturn(Optional.of(loan));

    String json = new ObjectMapper().writeValueAsString(dto);

    mockMvc.perform(patch(LOAN_API.concat("/1"))
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(json))
        .andExpect(status().isOk());

    Mockito.verify(loanService, Mockito.times(1)).update(loan);

  }

  @Test
  @DisplayName("Should return error(404) if we try to return one non existent book")
  public void returnNonexistentBookTest() throws Exception {

    ReturnedLoanDTO dto = ReturnedLoanDTO.builder().returned(true).build();

    BDDMockito.given(loanService.getById(Mockito.anyLong()))
        .willReturn(Optional.empty());

    String json = new ObjectMapper().writeValueAsString(dto);

    mockMvc.perform(patch(LOAN_API.concat("/1"))
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(json))
        .andExpect(status().isNotFound());


  }

  @Test
  @DisplayName("Should filter Loans")
  public void filterLoansTest() throws Exception {

    Long id = 1L;
    Loan loan = LoanServiceTest.makeLoan();
    loan.setId(id);
    Book book = LoanServiceTest.makeBook();
    loan.setBook(book);

    BDDMockito.given(loanService.find(Mockito.any(LoanFilterDTO.class), Mockito.any(Pageable.class)))
        .willReturn(new PageImpl<Loan>(Arrays.asList(loan), PageRequest.of(0, 10), 1));

    String queryString = String.format("?isbn=%s&customer=%s&page=0&size=10", book.getIsbn(), loan.getClientName());


    MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(LOAN_API.concat(queryString))
        .accept(MediaType.APPLICATION_JSON);

    mockMvc.perform(request)
        .andExpect(status().isOk())
        .andExpect(jsonPath("content", Matchers.hasSize(1)))
        .andExpect(jsonPath("totalElements").value(1))
        .andExpect(jsonPath("pageable.pageSize").value(10))
        .andExpect(jsonPath("pageable.pageNumber").value(0));

  }


  private LoanDTO createNewLoanDTO() {

    return LoanDTO.builder().clientName("Me").isbn("123").build();
  }

}
