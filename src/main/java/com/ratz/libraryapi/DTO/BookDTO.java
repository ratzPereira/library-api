package com.ratz.libraryapi.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookDTO {

  private Long id;
  private String title;
  private String author;
  private String isbn;
}
