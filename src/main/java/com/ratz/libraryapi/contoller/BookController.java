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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.modelmapper.ModelMapper;
import io.swagger.annotations.Api;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;


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


    @GetMapping("/{id}")
    public BookDTO getBookById(@PathVariable Long id) {

        return bookService.getById(id).map(book -> modelMapper.map(book, BookDTO.class))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBookById(@PathVariable Long id) {

        Book book = bookService.getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        bookService.deleteBook(book);
    }

    @PutMapping("/{id}")
    public BookDTO updateBook(@PathVariable Long id, BookDTO bookDTO) {

        return bookService.getById(id).map(bookToUpdate -> {
            bookToUpdate.setAuthor(bookDTO.getAuthor());
            bookToUpdate.setTitle(bookDTO.getTitle());

            bookToUpdate = bookService.update(bookToUpdate);
            return modelMapper.map(bookToUpdate, BookDTO.class);
        })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    }

    @GetMapping("")
    public Page<BookDTO> filterBooks(BookDTO bookDTO, Pageable pageRequest) {

        Book filter = modelMapper.map(bookDTO, Book.class);
        Page<Book> result = bookService.find(filter, pageRequest);

        List<BookDTO> list = result.getContent().stream()
                .map(entity -> modelMapper.map(entity, BookDTO.class))
                .collect(Collectors.toList());

        return new PageImpl<BookDTO>(list, pageRequest, result.getTotalElements());
    }
}
