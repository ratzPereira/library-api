package com.ratz.libraryapi.contoller;

import com.ratz.libraryapi.DTO.BookDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/books")
public class BookController {

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public BookDTO createBook(@RequestBody BookDTO bookDTO) {

    return bookDTO;
  }
}
