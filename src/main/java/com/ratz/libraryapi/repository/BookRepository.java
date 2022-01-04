package com.ratz.libraryapi.repository;

import com.ratz.libraryapi.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface BookRepository extends JpaRepository<Book,Long> {
  boolean existsByIsbn( String isbn );
  Optional<Book> findBookByIsbn (String isbn);
}
