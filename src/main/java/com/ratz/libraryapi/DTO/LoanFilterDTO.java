package com.ratz.libraryapi.DTO;

import lombok.Data;

@Data
public class LoanFilterDTO {

  private String isbn;
  private String clientName;
}
