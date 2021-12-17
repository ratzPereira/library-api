package com.ratz.libraryapi.contoller;

import com.ratz.libraryapi.DTO.BookDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/books")
public class BookController {

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public BookDTO createBook() {

    return new BookDTO(123L, "Me", "SomeOne", "23as2");
  }
}
