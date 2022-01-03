package com.ratz.libraryapi.exception;

import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

public class ApiErrors{

  private List<String> errors;

  public ApiErrors(BindingResult result) {
    this.errors = new ArrayList<>();
    result.getAllErrors().forEach( error -> this.errors.add(error.getDefaultMessage()));
  }


  public ApiErrors(BusinessException ex) {
    this.errors = List.of(ex.getMessage());
  }

  public ApiErrors(ResponseStatusException ex) {
    this.errors = List.of(ex.getReason());
  }

  public List<String> getErrors() {
    return errors;
  }
}
