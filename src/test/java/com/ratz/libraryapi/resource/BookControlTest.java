package com.ratz.libraryapi.resource;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@WebMvcTest
@AutoConfigureMockMvc
public class BookControlTest {

  @Autowired
  MockMvc mockMvc;

  @Test
  @DisplayName("Should create one book with success")
  public void createBookTest(){

  }

  @Test
  @DisplayName("Should give an error when we dont have all data to create one book")
  public void createInvalidBookTest(){

  }
}
