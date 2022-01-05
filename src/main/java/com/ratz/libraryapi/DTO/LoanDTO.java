package com.ratz.libraryapi.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanDTO {

  private Long id;
  private String isbn;
  private String clientName;
  private BookDTO book;
}
