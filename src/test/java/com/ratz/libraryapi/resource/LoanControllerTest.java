package com.ratz.libraryapi.resource;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.ratz.libraryapi.DTO.LoanDTO;
import com.ratz.libraryapi.contoller.LoanController;
import com.ratz.libraryapi.entity.Book;
import com.ratz.libraryapi.entity.Loan;
import com.ratz.libraryapi.service.BookService;
import com.ratz.libraryapi.service.LoanService;
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
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.Optional;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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
        .andExpect((ResultMatcher) jsonPath("id").value(1L));

  }


  private LoanDTO createNewLoanDTO() {

    return LoanDTO.builder().clientName("Me").isbn("123").build();
  }

}
