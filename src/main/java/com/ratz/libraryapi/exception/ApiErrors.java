package com.ratz.libraryapi.exception;

import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

public class ApiErrors{

  private final List<String> errors;

  public ApiErrors(BindingResult result) {
    this.errors = new ArrayList<>();
    result.getAllErrors().forEach( error -> this.errors.add(error.getDefaultMessage()));
  }

  public List<String> getErrors() {
    return errors;
  }
}
