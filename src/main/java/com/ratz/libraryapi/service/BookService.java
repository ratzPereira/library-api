package com.ratz.libraryapi.service;

import com.ratz.libraryapi.entity.Book;
import org.springframework.stereotype.Service;

@Service
public interface BookService {

  Book save(Book book);
}
