package com.ratz.libraryapi.contoller;

import com.ratz.libraryapi.DTO.BookDTO;
import com.ratz.libraryapi.entity.Book;
import com.ratz.libraryapi.exception.ApiErrors;
import com.ratz.libraryapi.exception.BusinessException;
import com.ratz.libraryapi.service.BookService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.modelmapper.ModelMapper;
import io.swagger.annotations.Api;

import javax.validation.Valid;


@RestController
@RequestMapping("api/books")
@RequiredArgsConstructor
@Slf4j
@Api("Book API")
public class BookController {

  private final BookService bookService;
  private final ModelMapper modelMapper;


  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public BookDTO createBook(@RequestBody @Valid BookDTO bookDTO) {

    Book book = modelMapper.map(bookDTO, Book.class);
    book = bookService.save(book);

    return modelMapper.map(book, BookDTO.class);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ApiErrors handleValidationException(MethodArgumentNotValidException ex) {

    BindingResult bindingResult = ex.getBindingResult();
    return new ApiErrors(bindingResult);
  }

  @ExceptionHandler(BusinessException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ApiErrors handleBusinessException(BusinessException ex){
    return  new ApiErrors(ex);
  }

  @GetMapping("/{id}")
  public BookDTO getBookById(@PathVariable Long id){

    Book book = bookService.getById(id).get();
    return modelMapper.map(book,BookDTO.class);
  }


}
