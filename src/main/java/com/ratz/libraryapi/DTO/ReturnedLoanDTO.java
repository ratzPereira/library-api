package com.ratz.libraryapi.DTO;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class ReturnedLoanDTO {

  private boolean returned;
}
