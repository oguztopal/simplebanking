package com.assignment.simplebanking.model.dto;

import com.assignment.simplebanking.type.TransactionType;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto implements Serializable{

  private LocalDateTime date;

  private Double amount;

  private TransactionType type;

  private String approvalCode;
}
