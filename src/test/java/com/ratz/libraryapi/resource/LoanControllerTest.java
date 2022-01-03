package com.ratz.libraryapi.resource;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.ratz.libraryapi.DTO.LoanDTO;
import com.ratz.libraryapi.contoller.LoanController;
import com.ratz.libraryapi.entity.Book;
import com.ratz.libraryapi.entity.Loan;
import com.ratz.libraryapi.exception.BusinessException;
import com.ratz.libraryapi.service.BookService;
import com.ratz.libraryapi.service.LoanService;
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

import java.time.LocalDate;
import java.util.Optional;

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
        .andExpect( jsonPath("errors", Matchers.hasSize(1)))
        .andExpect( jsonPath("errors[0]").value("Book not found"));
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
        .andExpect( jsonPath("errors", Matchers.hasSize(1)))
        .andExpect( jsonPath("errors[0]").value("Book already loaned"));
  }


  private LoanDTO createNewLoanDTO() {

    return LoanDTO.builder().clientName("Me").isbn("123").build();
  }

}
