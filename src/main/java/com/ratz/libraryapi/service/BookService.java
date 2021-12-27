package com.ratz.libraryapi.service;

import com.ratz.libraryapi.entity.Book;

import java.util.Optional;


public interface BookService {

  Book save(Book book);
  Optional<Book> getById(Long id);
  void deleteBook(Book book);
  Book update(Book book);
}
