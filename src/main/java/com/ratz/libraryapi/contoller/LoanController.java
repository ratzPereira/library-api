package com.ratz.libraryapi.contoller;


import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/loans")
@RequiredArgsConstructor
@Slf4j
@Api("Loan API")
public class LoanController {


}
