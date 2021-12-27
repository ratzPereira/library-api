package com.ratz.libraryapi.repository;

import com.ratz.libraryapi.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;



public interface BookRepository extends JpaRepository<Book,Long> {
  boolean existsByIsbn( String isbn );
}
