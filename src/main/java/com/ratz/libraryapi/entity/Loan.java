package com.ratz.libraryapi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Loan {

  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private String clientName;

  @Column
  private Boolean returned;

  @Column
  private LocalDate loanDate;

  @JoinColumn(name = "id_book")
  @ManyToOne(cascade=CascadeType.PERSIST)
  private Book book;
}
