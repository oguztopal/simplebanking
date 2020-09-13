package com.assignment.simplebanking.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TransactionErrorResponse{

  private HttpStatus status;

  private String message;

}
