package com.ratz.libraryapi.service.Impl;

import com.ratz.libraryapi.entity.Book;
import com.ratz.libraryapi.exception.BusinessException;
import com.ratz.libraryapi.repository.BookRepository;
import com.ratz.libraryapi.service.BookService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {


    private final BookRepository repository;

    public BookServiceImpl(BookRepository repository) {
        this.repository = repository;
    }

    @Override
    public Book save(Book book) {

        if (repository.existsByIsbn(book.getIsbn())) {
            throw new BusinessException("One book with this Isbn already exist");
        }
        return repository.save(book);
    }

    @Override
    public Optional<Book> getById(Long id) {
        return repository.findById(id);
    }


  @Override
  public Optional<Book> getById(Long id) {
    return repository.findById(id);
  }

    @Override
    public void deleteBook(Book book) {

        if (book == null || book.getId() == null) {
            throw new IllegalArgumentException("book id can not be null");
        }

        repository.delete(book);
    }

    @Override
    public Book update(Book book) {

        if (book == null || book.getId() == null) {
            throw new IllegalArgumentException("book id can not be null");
        }

        return repository.save(book);
    }

    @Override
    public Page<Book> find(Book filter, Pageable pageRequest) {

        Example<Book> example = Example.of(filter, ExampleMatcher.matching()
                .withIgnoreCase()
                .withIgnoreNullValues()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));

        return repository.findAll(example, pageRequest);
    }

    @Override
    public Optional<Book> getByIsbn(String isbn) {
        return repository.findBookByIsbn(isbn);
    }

}
