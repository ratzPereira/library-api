package com.ratz.libraryapi.contoller;

import com.ratz.libraryapi.DTO.BookDTO;
import com.ratz.libraryapi.entity.Book;
import com.ratz.libraryapi.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/books")
public class BookController {

  @Autowired
  private BookService bookService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public BookDTO createBook(@RequestBody BookDTO bookDTO) {

    Book book = Book.builder().author(bookDTO.getAuthor()).title(bookDTO.getTitle()).isbn(bookDTO.getIsbn()).build();

    book = bookService.save(book);

    return BookDTO.builder().id(book.getId()).author(book.getAuthor()).title(book.getTitle()).isbn(book.getIsbn()).build();
  }
}
