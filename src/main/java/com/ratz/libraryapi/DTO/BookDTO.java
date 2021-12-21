package com.ratz.libraryapi.DTO;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookDTO {

  private Long id;

  @NotEmpty
  private String title;

  @NotEmpty
  private String author;

  @NotEmpty
  private String isbn;
}
