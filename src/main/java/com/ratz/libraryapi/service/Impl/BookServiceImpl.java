package com.ratz.libraryapi.service.Impl;

import com.ratz.libraryapi.entity.Book;
import com.ratz.libraryapi.repository.BookRepository;
import com.ratz.libraryapi.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {


  private BookRepository repository;

  public BookServiceImpl(BookRepository repository) {
    this.repository = repository;
  }

  @Override
  public Book save(Book book) {
    return repository.save(book);
  }
}
