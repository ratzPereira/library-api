package com.ratz.libraryapi.repository;


import com.ratz.libraryapi.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan, Long> {
}