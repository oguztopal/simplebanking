package com.assignment.simplebanking.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import javax.persistence.Entity;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TransactionStatus{

  private HttpStatus status;

  private UUID approvalCode;

}
